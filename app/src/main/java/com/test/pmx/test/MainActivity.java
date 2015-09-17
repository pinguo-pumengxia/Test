package com.test.pmx.test;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.test.pmx.two.FragActivity;


public class MainActivity extends ActionBarActivity implements Toolbar.OnMenuItemClickListener {
    private Button button;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FragActivity.class));
            }
        });

        final TextInputLayout inputLayout = (TextInputLayout) findViewById(R.id.textInput);
        inputLayout.setHint("请输入姓名：");

        EditText editText = inputLayout.getEditText();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 4) {
                    inputLayout.setErrorEnabled(true);
                    inputLayout.setError("姓名长度不超过4个：");
                } else {
                    inputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floating);

        final Snackbar snackbar = Snackbar.make(floatingActionButton, "测试弹出提示", Snackbar.LENGTH_LONG);
        snackbar.show();
        snackbar.setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        //设置Tool
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        //    setSupportActionBar(toolbar);
        //隐藏默认title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("主标题");
        toolbar.setSubtitle("副标题");
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setNavigationIcon(android.R.drawable.ic_input_delete);
        toolbar.setOnMenuItemClickListener(this);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Toast.makeText(this, "查找按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                Toast.makeText(this, "分享按钮", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
