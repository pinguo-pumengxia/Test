package com.jenkins.pmx.mp3;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{
    // ListView
    private ListView listView;
    // 适配器
    private SimpleAdapter adapter;
    // 数据源
    private ArrayList<HashMap<String, String>> list;
    // 当前播放的曲目
    private int currentPositionMusic = -1;

    // 上一首
    private ImageView lastImageButton;
    // 播放
    private ImageView playImageButton;
    // 下一首
    private ImageView nextImageButton;
    // 循环
    private ImageView loopImageButton;
    // 播放进度
    private SeekBar playSeekBar;
    // 当前播放曲目
    private TextView currentPlayingSong;
    // 是否是第一次进来
    private boolean ifFirstIn = true;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_music);
        lastImageButton = (ImageView) findViewById(R.id.previous_music);
        playImageButton = (ImageView) findViewById(R.id.play_music);
        nextImageButton = (ImageView) findViewById(R.id.previous_music);
        loopImageButton = (ImageView) findViewById(R.id.repeat_music);
        playSeekBar = (SeekBar) findViewById(R.id.audioTrack);
        currentPlayingSong = (TextView) findViewById(R.id.musicTitle);
        list = new ArrayList<HashMap<String, String>>();

        adapter = new SimpleAdapter(this, list, R.layout.list_item,
                new String[] { "title" }, new int[] { R.id.textview_item });
        listView.setAdapter(adapter);

        // 为listView设置监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                try {
                    currentPositionMusic = position;
                    playMusic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        lastImageButton.setOnClickListener(this);
        playImageButton.setOnClickListener(this);
        nextImageButton.setOnClickListener(this);
        loopImageButton.setOnClickListener(this);
        playSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    // 改变进度条
                    intent = new Intent("changed");
                    intent.putExtra("progress", progress);
                    startService(intent);
                }
            }
        });
    }

    private void playMusic() {
        // 启动播放音乐的服务
        intent = new Intent("play");
        intent.putExtra("uri", ((HashMap<String, String>) list
                .get(currentPositionMusic)).get("path"));
        intent.putExtra("title", ((HashMap<String, String>) list
                .get(currentPositionMusic)).get("title"));
        startService(intent);
        // 把图片改为播放的图片
     //   playImageButton.setImageResource(R.drawable.play.png);
        // 同时更改SeekBar的进度,因为进度是不断变化的,所以需要一个子线程来刷新下
        // playSeekBar.setMax(mp.getDuration());
        // 设置当前播放曲目信息
        currentPlayingSong.setTextColor(Color.GREEN);
        currentPlayingSong.setText(list.get(currentPositionMusic).get("title"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 得到所有音频
        if (ifFirstIn) {
            ifFirstIn = false;
            scanMusic();
        }
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("max_progress");
        filter.addAction("progress");
        filter.addAction("play_next_song");
        filter.addAction("pause.png");
        filter.addAction("set_play");
        filter.addAction("stop_loop");
        filter.addAction("start_loop");
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭通知
        intent = new Intent("stop_notification");
        startService(intent);

        // 停止服务
        stopService(intent);
        // 取消广播的注册
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("你确定要退出吗?");
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 得多所有的音频
     */
    private void scanMusic() {
        // 置空list集合中的所有元素,放置反复启动导致的数据重复,其实这个部分是因为每次进来都会重新获得焦点,执行onResume造成的
        // 这个修改虽然可以,但是每次进来都有重新加载,增加了手机的符合,所以可以设置一个标志,只有在第一进来的时候才会加载数据
//		list.clear();
        new Thread() {
            public void run() {
                Cursor cursor = getContentResolver().query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                        null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

                while (cursor.moveToNext()) {
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    String size = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                    if (Long.parseLong(size) > 1024 * 1024) {
                        HashMap<String, String> hashMap1 = new HashMap<String, String>();
                        hashMap1.put("title", title);
                        hashMap1.put("path", path);
                        list.add(hashMap1);
                    }
                }
                cursor.close();
            };
        }.start();

    }

    /**
     * 监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous_music:
                playLastSong();
                break;
            case R.id.play_music:
                intent = new Intent("click_play");
                startService(intent);
                break;
            case R.id.next_music:
                playNextSong();
                break;
            case R.id.repeat_music:
                intent = new Intent("loops");
                startService(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 播放下一曲
     */
    private void playNextSong() {
        // 只有当有音乐在播放的时候才可以点击下一首
        if (currentPositionMusic != -1) {
            // 如果当前歌曲为最后一首,就播放第一首歌曲
            if (currentPositionMusic == list.size() - 1) {
                // 设置当前歌曲为第一首
                currentPositionMusic = 0;
                try {
                    // 播放第一首歌曲
                    playMusic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }// 否则就播放下一首歌曲
            else {
                // 设置当前歌曲为下一首
                currentPositionMusic++;
                try {
                    // 播放下一首歌曲
                    playMusic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 播放上一曲
     */
    private void playLastSong() {
        // 只有当有音乐在播放的时候才可以点击上一首
        if (currentPositionMusic != -1) {
            // 如果当前歌曲为第一首,就播放最后一首歌曲
            if (currentPositionMusic == 0) {
                // 设置当前歌曲为最后一首
                currentPositionMusic = list.size() - 1;
                try {
                    // 播放最后一首歌曲
                    playMusic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }// 否则就播放上一首歌曲
            else {
                // 设置当前歌曲为前一首
                currentPositionMusic--;
                try {
                    // 播放前一首歌曲
                    playMusic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 广播对象,动态注册,用来接收从Service传过来的消息,根据不同的消息做不同的事情
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("max_progress")) {
                playSeekBar.setMax(intent
                        .getIntExtra("max_progress", 100));
            } else if (intent.getAction().equals("max_progress")) {
                playSeekBar.setProgress(intent
                        .getIntExtra("max_progress", 0));
            } else if (intent.getAction().equals("play_next_song")) {
                playNextSong();
            } else if (intent.getAction().equals("pause.png")) {
                // 还要把图片改为暂停的图片
           //     playImageButton.setImageResource(R.drawable.pause.png);
            } else if (intent.getAction().equals("set_play")) {
                // 把图片设置成播放的图片
           //     playImageButton.setImageResource(R.drawable.play.png);
            } else if (intent.getAction().equals("stop_loop")) {
                // 还要把图片改为不循环的图片
            //    loopImageButton.setImageResource(R.drawable.loop_false);
            } else if (intent.getAction().equals("start_loop")) {
                // 把图片设置成循环播放的图片
             //   loopImageButton.setImageResource(R.drawable.loop_true);
            }

        }
    };

}
