package com.example.grey.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.grey.R;
import com.example.grey.adapter.MyRecyclerViewAdapter;
import com.example.grey.widget.PostItemDecoration;
import com.example.grey.model.PostList;
import com.example.grey.sensor.ChangeOrientationHandler;
import com.example.grey.sensor.OrientationSensorListener;
import com.example.grey.model.Background;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ScrollingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private PostList postList=new PostList();

    private ImageView imageView;
    private Handler handler;
    private OrientationSensorListener listener;
    private SensorManager sm;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        initView();
        initData();

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        myRecyclerViewAdapter=new MyRecyclerViewAdapter(postList.postList,this);
        recyclerView.setAdapter(myRecyclerViewAdapter);

        recyclerView.addItemDecoration(new PostItemDecoration(20));
        myRecyclerViewAdapter.setOnItemClickListener(MyItemClickListener);

        handler = new ChangeOrientationHandler(this);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(handler);
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_scrolling);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ScrollingActivity.this,DrawerActivity.class);
                startActivity(intent);
                ScrollingActivity.this.finish();
            }
        });


        //个人主页显示用户名
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            // 允许用户使用应用
            Toast.makeText(ScrollingActivity.this,bmobUser.getUsername()+"个人界面",Toast.LENGTH_SHORT).show();
            getSupportActionBar().setTitle(bmobUser.getUsername());
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            Toast.makeText(ScrollingActivity.this,"无用户消息",Toast.LENGTH_SHORT).show();
        }

        imageView=findViewById(R.id.img_scrolling);

        //查询数据
        BmobQuery<Background> backgroundBmobQuery=new BmobQuery<>();
        backgroundBmobQuery.addWhereEqualTo("user",BmobUser.getCurrentUser());
        backgroundBmobQuery.findObjects(new FindListener<Background>() {
            @Override
            public void done(List<Background> list, BmobException e) {
                if(e==null){
                    String imageUrl=list.get(0).getUrl();
                    Toast.makeText(ScrollingActivity.this, imageUrl, Toast.LENGTH_SHORT).show();
                    Glide.with(ScrollingActivity.this).load(imageUrl).into(imageView);
                }else{
                    Toast.makeText(ScrollingActivity.this, "进行添加", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void initView(){
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
    }

    private void initData() {
        postList.initMyData();
    }

    private MyRecyclerViewAdapter.OnItemClickListener MyItemClickListener=new MyRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, final int position) {
            //给按钮赋值
            switch (v.getId()){
                case R.id.agree:
//                    postList.get(position).addAgree();

                    //查询数据并且进行更新修改
                    postList.updateAgree(position);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                case R.id.disagree:
                    postList.updateDisagree(position);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                case R.id.trush:
                    AlertDialog.Builder dialog=new AlertDialog.Builder(ScrollingActivity.this);
                    dialog.setTitle("确认信息");
                    dialog.setMessage("确定删除信息？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            postList.deletePost(position);
                        }
                    });
                    dialog.setNegativeButton("取消删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_scrolling,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_search:
                Toast.makeText(this,"search",Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(ScrollingActivity.this,MySearchActivity.class);
                startActivity(intent);
//                LoginActivity.this.finish();

                break;
            case R.id.toolbar_edit:
                Toast.makeText(this,"edit",Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(ScrollingActivity.this,EditActivity.class);
                startActivity(intent1);
                break;
            case R.id.toolbar_follow:
                Toast.makeText(this,"follow",Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_refresh:
                postList.refreshMyData();
                myRecyclerViewAdapter.notifyDataSetChanged();
                Toast.makeText(this,"refresh",Toast.LENGTH_SHORT).show();
                break;
                default:
        }
        return true;
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
