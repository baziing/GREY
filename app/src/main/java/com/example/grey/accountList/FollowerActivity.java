package com.example.grey.accountList;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.grey.home.DrawerActivity;
import com.example.grey.R;

import java.util.ArrayList;
import java.util.List;

public class FollowerActivity extends Activity {

    private ListView lsv_side_slip_delete;
    private List<String> list = new ArrayList<>();
    private SideSlipFollowerAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(FollowerActivity.this,DrawerActivity.class);
                startActivity(intent);
                FollowerActivity.this.finish();
            }
        });

        setViews();// 控件初始化
        setData();// 初始化模拟数据
        setAdapter();// 创建adapter，listview设置adapter
        setListeners();// 设置监听
    }


    private void setListeners() {
        if (adapter != null){
            // 注册监听器,回调用来刷新数据显示
            adapter.setDelItemListener(new SideSlipFollowerAdapter.DeleteItem() {
                @Override
                public void delete(int pos) {
                    list.remove(pos);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }


    private void setAdapter() {
        adapter = new SideSlipFollowerAdapter(this, list);
        lsv_side_slip_delete.setAdapter(adapter);

    }


    private void setData() {
        for (int i = 0;i < 16;i++){
            list.add("侧滑删除" + (i + 1));
        }
    }


    private void setViews() {
        lsv_side_slip_delete = findViewById(R.id.lsv_side_slip_delete);
    }
}
