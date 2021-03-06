package com.example.grey.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.grey.model.EMUser;
import com.example.grey.R;
import com.example.grey.adapter.MyRecyclerViewAdapter;
import com.example.grey.widget.PostItemDecoration;
import com.example.grey.model.PostList;
import com.example.grey.sensor.ChangeOrientationHandler;
import com.example.grey.sensor.OrientationSensorListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private PostList postList=new PostList();


    private Handler handler;
    private OrientationSensorListener listener;
    private SensorManager sm;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                BmobQuery<EMUser> emUserBmobQuery=new BmobQuery<>();
                emUserBmobQuery.addWhereEqualTo("bmobUser", BmobUser.getCurrentUser());
                emUserBmobQuery.findObjects(new FindListener<EMUser>() {
                    @Override
                    public void done(List<EMUser> list, BmobException e) {
                        if (e==null){
                            postList.refreshData(list.get(0).followerList);
                        }
                        else {
                            Toast.makeText(DrawerActivity.this,"登录"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//                postList.refreshData();

                myRecyclerViewAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void initView(){
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
    }

    private void initData(){
        BmobQuery<EMUser> emUserBmobQuery=new BmobQuery<>();
        emUserBmobQuery.addWhereEqualTo("bmobUser", BmobUser.getCurrentUser());
        emUserBmobQuery.findObjects(new FindListener<EMUser>() {
            @Override
            public void done(List<EMUser> list, BmobException e) {
                if (e==null){
                    postList.initData(list.get(0).followerList);
//                    Toast.makeText(DrawerActivity.this,list.get(0).followerList.get(0),Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DrawerActivity.this,"加载失败"+e,Toast.LENGTH_SHORT).show();
                }
            }
        });
//        postList.initData();
    }


    private MyRecyclerViewAdapter.OnItemClickListener MyItemClickListener=new MyRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, final int position) {
            //给按钮赋值
            switch (v.getId()){
                case R.id.agree:
                    postList.updateAgree(position);
                    myRecyclerViewAdapter.notifyDataSetChanged();

                    break;
                case R.id.disagree:
                    postList.updateDisagree(position);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                case R.id.trush:
                    if (postList.isCurrentUser(position)){
                        AlertDialog.Builder dialog=new AlertDialog.Builder(DrawerActivity.this);
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
                    }else {
                        postList.repeatPost(position);
                    }
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v) {

        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(DrawerActivity.this,EditActivity.class);
            startActivity(intent);
            return true;
        }else if (id==R.id.action_search){
            Intent intent=new Intent(DrawerActivity.this,SearchActivity.class);
            startActivity(intent);
            return true;
        }else if (id==R.id.action_refresh){
//            postList.refreshData();
//            myRecyclerViewAdapter.notifyDataSetChanged();
            BmobQuery<EMUser> emUserBmobQuery=new BmobQuery<>();
            emUserBmobQuery.addWhereEqualTo("bmobUser", BmobUser.getCurrentUser());
            emUserBmobQuery.findObjects(new FindListener<EMUser>() {
                @Override
                public void done(List<EMUser> list, BmobException e) {
                    if (e==null){
                        postList.refreshData(list.get(0).followerList);
                    }
                    else {
                        Toast.makeText(DrawerActivity.this,"登录"+e,Toast.LENGTH_SHORT).show();
                    }
                }
            });
//                postList.refreshData();

            myRecyclerViewAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_information) {
            // Handle the camera action
            Intent intent=new Intent(DrawerActivity.this,ScrollingActivity.class);
            startActivity(intent);
            DrawerActivity.this.finish();
        } else if (id == R.id.nav_followers) {
            Intent intent=new Intent(DrawerActivity.this,FollowerActivity.class);
            startActivity(intent);
            DrawerActivity.this.finish();
        } else if (id == R.id.nav_settings) {
            Intent intent=new Intent(DrawerActivity.this,SettingActivity.class);
            startActivity(intent);
            DrawerActivity.this.finish();
        } else if (id == R.id.nav_share) {
//            Bmob.initialize(this, "796037cf0d5cb806545e84bed5238df5");
//            String string="qaz";
//            Intent intent=new Intent(DrawerActivity.this,ECChatActivity.class);
//            intent.putExtra(EaseConstant.EXTRA_USER_ID,string.trim());
//            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EMMessage.ChatType.Chat);
//            startActivity(intent);
            Intent intent=new Intent(DrawerActivity.this, ConversationListActivity.class);
            startActivity(intent);

        } else if(id==R.id.nav_square){
            Intent intent=new Intent(DrawerActivity.this, SquareActivity.class);
            startActivity(intent);
            DrawerActivity.this.finish();
        } else if (id==R.id.nav_search_friends){
            Intent intent=new Intent(DrawerActivity.this, SearchFriendsActivity.class);
            startActivity(intent);
            DrawerActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
