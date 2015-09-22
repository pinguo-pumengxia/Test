package com.jenkins.pmx.mp3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ws-pumengxia on 15-9-21.
 */
public class PlayMusicService extends Service {
    private MediaPlayer mediaPlayer;
    private static final int SET_BAR_MAX = 3;
    private static final int UPDATE_PROGRESS = 1;
    private static final int LOADING_DATA = 2;
    //通知管理
    private NotificationManager notificationManager;
    //通知
    private Notification notification;
    private Intent intent;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (intent.getAction().equals("play")) { //播放音乐

            try {
                Toast.makeText(getApplicationContext(), "开始播放", Toast.LENGTH_LONG).show();
                playMusic(intent.getStringExtra("uri"));
                showNotification(intent.getStringExtra("title"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (intent.getAction().equals("changed")) {// 如果是拖动的Action,就跳至拖动的进度
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(intent.getIntExtra("progress", 0));
            }
        } else if (intent.getAction().equals("click_play")) {// 如果是播放/暂停Action,就执行相应的播放或者暂停
            Toast.makeText(getApplicationContext(), "播放被点击了", Toast.LENGTH_LONG).show();
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) { //如果音乐播放器正在播放
                    //暂停
                    mediaPlayer.pause();
                    this.intent = new Intent("pause.png");
                    sendBroadcast(this.intent);
                } else {
                    //否则播放
                    mediaPlayer.start();
                    //发送播放广播
                    this.intent = new Intent("set_play");
                    sendBroadcast(this.intent);
                }
            }
        } else if (intent.getAction().equals("loops")) {// 如果是loop循环Action,就把歌曲设置为循环播放
            Toast.makeText(getApplicationContext(), "循环被点击了", Toast.LENGTH_LONG).show();
            if (mediaPlayer != null) {
                if (mediaPlayer.isLooping()) {
                    mediaPlayer.setLooping(false);
                    this.intent = new Intent("stop_loop");
                    sendBroadcast(this.intent);
                }
            }else {
                // 否则就循环播放它
                mediaPlayer.setLooping(true);
                // 发送开始循环广播
                this.intent = new Intent("start_loop");
                sendBroadcast(this.intent);
            }
        } else if (intent.getAction().equals("stop_notification")) {
            notificationManager.cancelAll();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }


    public void playMusic(String path) throws IOException {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        mediaPlayer.reset();
        mediaPlayer.setDataSource(path);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                handler.sendEmptyMessage(SET_BAR_MAX);
                handler.sendEmptyMessage(UPDATE_PROGRESS);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mp.isLooping()) {
                    mp.start();
                } else {
                    intent = new Intent("play_next_song");
                    sendBroadcast(intent);
                }
            }
        });
    }

    private void showNotification(String title) {
        notification = new Notification(R.drawable.notification_template_icon_bg, "百度音乐", System.currentTimeMillis());
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;

        Intent intent1 = new Intent(this, MainActivity.class);
        intent1.setAction(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        notification.setLatestEventInfo(this, "正在播放", title, pendingIntent);
        notificationManager.notify(1, notification);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    intent = new Intent("progress");
                    intent.putExtra("progress", mediaPlayer.getCurrentPosition());
                    sendBroadcast(intent);
                    handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 1000);
                    break;
                case LOADING_DATA:
                    break;
                case SET_BAR_MAX:
                    intent = new Intent("max_progress");
                    intent.putExtra("max_progress", mediaPlayer.getDuration());
                    sendBroadcast(intent);
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
