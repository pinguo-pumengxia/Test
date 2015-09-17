package com.test.pmx.two;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.pmx.test.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FragActivity extends ActionBarActivity implements HeadlinesFragment.OnHeadlinesSelectedListener {
    HeadlinesFragment headlinesFragment;
    private EditText editText;
    private TextView showTextView;
    private Button addButton;
    private Button showButton;
    private Button callButton;
    // 要保存的文件名
    private String fileName = "pmx_java.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        initView();

    }

    @Override
    public void onArticleSelected(int position) {
        ArticleFragment articleFrag = (ArticleFragment)
                getSupportFragmentManager().findFragmentById(R.id.article_fragment);
        if (articleFrag != null) {
            articleFrag.updateArticleView(position);
        } else {
            ArticleFragment newFragment = new ArticleFragment();
            Bundle args = new Bundle();
            args.putInt(ArticleFragment.ARG_POSITION, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.article_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void initView() {
        headlinesFragment = (HeadlinesFragment) getSupportFragmentManager().findFragmentById(R.id.headlines_fragment);
        headlinesFragment.setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1,
                new String[]{"1", "2", "3", "4", "5"})); //使用静态数组填充列表
        editText = (EditText) findViewById(R.id.addText);
        showTextView = (TextView) findViewById(R.id.showText);
        addButton = (Button) this.findViewById(R.id.addButton);
        showButton = (Button) this.findViewById(R.id.showButton);
        callButton = (Button) this.findViewById(R.id.call_phone);
        // 绑定单击事件
        addButton.setOnClickListener(listener);
        showButton.setOnClickListener(listener);
        callButton.setOnClickListener(listener);
    }

    //声明监听器
    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            Button view = (Button) v;
            switch (view.getId()) {
                case R.id.addButton:
                    save();
                    break;
                case R.id.showButton:
                    read();
                    break;
                case R.id.call_phone:
                    call();
                    break;

            }

        }

    };

    private void save() {
        String content = editText.getText().toString();
        try {
            FileOutputStream outputStream = openFileOutput(fileName, Activity.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
            Toast.makeText(FragActivity.this, "save success", Toast.LENGTH_LONG);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read() {
        try {
            FileInputStream inputStream = this.openFileInput(fileName);
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            while (inputStream.read(bytes) != -1) {
                arrayOutputStream.write(bytes, 0, bytes.length);
            }
            inputStream.close();
            arrayOutputStream.close();
            String content = new String(arrayOutputStream.toByteArray());
            showTextView.setText(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void call() {
        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
// Or map point based on latitude/longitude
// Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }
}
