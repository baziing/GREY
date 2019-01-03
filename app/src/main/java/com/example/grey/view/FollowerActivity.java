package com.example.grey.view;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.grey.adapter.SideSlipFollowerAdapter;
import com.example.grey.model.EMUser;
import com.example.grey.R;
import com.example.grey.sensor.ChangeOrientationHandler;
import com.example.grey.sensor.OrientationSensorListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FollowerActivity extends Activity{

    private ListView lsv_side_slip_delete;
    private List<String> list = new ArrayList<>();
    private EMUser emUser;
    private SideSlipFollowerAdapter adapter;
    private Toolbar toolbar;
    private Handler handler;
    private OrientationSensorListener listener;
    private SensorManager sm;
    private Sensor sensor;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        handler = new ChangeOrientationHandler(this);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(handler);
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);

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

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
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
//        list.add("1");
//        Toast.makeText(FollowerActivity.this,list.size(),Toast.LENGTH_SHORT).show();
        BmobQuery<EMUser>emUserBmobQuery=new BmobQuery<>();
        emUserBmobQuery.addWhereEqualTo("bmobUser", BmobUser.getCurrentUser());
        emUserBmobQuery.findObjects(new FindListener<EMUser>() {
            @Override
            public void done(List<EMUser> list, BmobException e) {
                if (e==null){
                    addUsername(list.get(0).followerList);
                }
                else {
                    Toast.makeText(FollowerActivity.this,"登录"+e,Toast.LENGTH_SHORT).show();
                }
            }
        });
//        emUser.initData();
//        Toast.makeText(FollowerActivity.this,String.valueOf(emUser.followerList.size()),Toast.LENGTH_SHORT).show();
//        emUser.initData();
//        for (int i=0;i<emUser.followerList.size();i++){
//            list.add(emUser.followerList.get(i));
//        }
    }

    private void addUsername(List<String>followerList){
        for (int i=0;i<followerList.size();i++){
            list.add(followerList.get(i));
        }
    }


    private void setViews() {
        lsv_side_slip_delete = findViewById(R.id.lsv_side_slip_delete);
    }

    @Override
    protected void onPause() {
        sm.unregisterListener(listener);
        super.onPause();
    }

    @Override
    protected void onResume() {
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }
}
