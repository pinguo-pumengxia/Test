package com.test.pmx.test;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

public class CompatActivity extends ActionBarActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compat);

        initView();
    }

    private void initView(){
        recyclerView = (RecyclerView)findViewById(R.id.recycle);
        //设置布局显示方式
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL,true));
        //设置添加删除item时的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

}
