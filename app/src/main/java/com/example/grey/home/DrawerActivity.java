package com.example.grey.home;

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

import com.example.grey.R;
import com.example.grey.personalHomepage.ScrollingActivity;
import com.example.grey.accountList.FanActivity;
import com.example.grey.accountList.FollowerActivity;
import com.example.grey.edit.EditActivity;
import com.example.grey.post.MyRecyclerViewAdapter;
import com.example.grey.post.Post;
import com.example.grey.post.PostItemDecoration;
import com.example.grey.search.SearchActivity;
import com.example.grey.sensor.ChangeOrientationHandler;
import com.example.grey.sensor.OrientationSensorListener;
import com.example.grey.setting.SettingActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private List<Post>postList=new ArrayList<>();

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

                BmobQuery<Post>postBmobQuery=new BmobQuery<>();
                postBmobQuery.order("-createdAt");
                postBmobQuery.findObjects(new FindListener<Post>() {
                    @Override
                    public void done(List<Post> list, BmobException e) {
                        Toast.makeText(DrawerActivity.this,String.valueOf(equalList(postList,list)),Toast.LENGTH_SHORT).show();
                        if (equalList(list,postList)!=3){
                            Toast.makeText(DrawerActivity.this,String.valueOf(list.size())+"+"+String.valueOf(postList.size()),Toast.LENGTH_SHORT).show();
                            postList.clear();
                            addPost(list);
                        }else {
                            Toast.makeText(DrawerActivity.this,"刷新",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                myRecyclerViewAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        initView();
        initData();

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        myRecyclerViewAdapter=new MyRecyclerViewAdapter(postList,this);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public int equalList(List<Post> list1, List<Post> list2) {
        if (list1.size() != list2.size())
            return 1;
        for(int i=0;i<list1.size();i++){
            if (!list1.get(i).getObjectId().equals(list2.get(i).getObjectId()))
                return 2;
        }
        return 3;
    }

    private void initView(){
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
    }

    private void initData(){

        BmobQuery<Post>postBmobQuery=new BmobQuery<>();
        postBmobQuery.order("-createdAt");
//        postBmobQuery.addWhereEqualTo("bmobUser",BmobUser.getCurrentUser());
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    Toast.makeText(DrawerActivity.this,String.valueOf(list.size()),Toast.LENGTH_SHORT).show();
                    addPost(list);
                }else {
                    Toast.makeText(DrawerActivity.this,"失败",Toast.LENGTH_SHORT).show();
                }
            }
        });


//        postList.add(new Post("123111111111111111111111111111111111111111111111111111111111111111111111111111111111111",0,0));
//        postList.add(new Post("123",0,0));
//        postList.add(new Post("123111111111111111111111111111111111111111111111111111111111111111111111111111111111111",0,0));
//        postList.add(new Post("123",0,0));
//        postList.add(new Post("123111111111111111111111111111111111111111111111111111111111111111111111111111111111111",0,0));
//        postList.add(new Post("123",0,0));
//        postList.add(new Post("123111111111111111111111111111111111111111111111111111111111111111111111111111111111111",0,0));
//        postList.add(new Post("123",0,0));
//        postList.add(new Post("123111111111111111111111111111111111111111111111111111111111111111111111111111111111111",0,0));
//        postList.add(new Post("123",0,0));
//        postList.add(new Post("123111111111111111111111111111111111111111111111111111111111111111111111111111111111111",0,0));
//        postList.add(new Post("123",0,0));

//        取出云端所有的信息
//        BmobQuery<Post>postBmobQuery=new BmobQuery<>();
//        postBmobQuery.addWhereEqualTo("bmobUser",BmobUser.getCurrentUser());
//        postBmobQuery.findObjects(new FindListener<Post>() {
//            @Override
//            public void done(List<Post> list, BmobException e) {
//                if (e==null){
//                    Toast.makeText(DrawerActivity.this,String.valueOf(list.size()),Toast.LENGTH_SHORT).show();
//                    postList.add(list.get(0));
//                }else {
//                    Toast.makeText(DrawerActivity.this,"失败",Toast.LENGTH_SHORT).show();
//                }
////                Post post1=list.get(0);
////                postList.add(post1);
//            }
//        });
    }

    private void addPost(List<Post>list){
        int listSize=list.size();

        for (int i=0;i<listSize;i++){
//            postList.add(list.get(listSize-i-1));
            postList.add(list.get(i));
        }
    }

    private MyRecyclerViewAdapter.OnItemClickListener MyItemClickListener=new MyRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            //给按钮赋值
            switch (v.getId()){
                case R.id.agree:
//                    postList.get(position).addAgree();

                    //查询数据并且进行更新修改
                    updateAgree(postList.get(position));
                    myRecyclerViewAdapter.notifyDataSetChanged();

                    break;
                case R.id.disagree:
                    updateDisagree(postList.get(position));
                    myRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                case R.id.trush:
                    BmobQuery<Post>postBmobQuery=new BmobQuery<>();
                    postBmobQuery.addWhereEqualTo("time",postList.get(position).getTime());
                    postBmobQuery.findObjects(new FindListener<Post>() {
                        @Override
                        public void done(List<Post> list, BmobException e) {
                            if (e==null){
                                String objectId=list.get(0).getObjectId();
                                if (list.get(0).getName().equals(BmobUser.getCurrentUser().getUsername()))
                                    deletePost(list.get(0));//删除
                                else {
//                                    Toast.makeText(DrawerActivity.this,BmobUser.getCurrentUser().getUsername(),Toast.LENGTH_SHORT).show();
                                    repeatPost(list.get(0));//转发功能
                                }
                            }else {
                                Toast.makeText(DrawerActivity.this,"获取信息失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v) {

        }
    };

    public void repeatPost(Post post){
        if (!post.getContent().startsWith("来自转发："))
        {
            post.setContent("来自转发：\r\n"+post.getContent());
        }
        Post mpost=new Post(post.getContent(), BmobUser.getCurrentUser(),0,0,BmobUser.getCurrentUser().getUsername());
        long time = System.currentTimeMillis();
        mpost.setTime(time);
        mpost.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
//                    Toast.makeText(DrawerActivity.this,"转发成功",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(DrawerActivity.this,"转发失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deletePost(final Post post){

        //提示框，是否要进行删除
        AlertDialog.Builder dialog=new AlertDialog.Builder(DrawerActivity.this);
        dialog.setTitle("确认信息");
        dialog.setMessage("确定删除信息？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BmobQuery<Post>postBmobQuery=new BmobQuery<>();
//                postBmobQuery.addWhereEqualTo("bmobUser",BmobUser.getCurrentUser());
                postBmobQuery.addWhereEqualTo("time",post.getTime());
                postBmobQuery.findObjects(new FindListener<Post>() {
                    @Override
                    public void done(List<Post> list, BmobException e) {
                        if (e==null){
                            //找到对应的objectId
                            String objectId=list.get(0).getObjectId();

                            //删除对应的数据
                            Post mpost=new Post();
                            mpost.setObjectId(objectId);
                            mpost.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        Toast.makeText(DrawerActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(DrawerActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(DrawerActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.setNegativeButton("取消删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();


//        BmobQuery<Post>postBmobQuery=new BmobQuery<>();
//        postBmobQuery.addWhereEqualTo("bmobUser",BmobUser.getCurrentUser());
//        postBmobQuery.addWhereEqualTo("time",post.getTime());
//        postBmobQuery.findObjects(new FindListener<Post>() {
//            @Override
//            public void done(List<Post> list, BmobException e) {
//                if (e==null){
//                    Toast.makeText(DrawerActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(DrawerActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    public void updateAgree(final Post post){
        //查询数据
        BmobQuery<Post>postBmobQuery=new BmobQuery<>();
        postBmobQuery.addWhereEqualTo("bmobUser",BmobUser.getCurrentUser());
        postBmobQuery.addWhereEqualTo("time",post.getTime());
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    String objectId=list.get(0).getObjectId();
                    post.addAgree();
                    post.addAgreeList();
                    post.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                Toast.makeText(DrawerActivity.this,"成功",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(DrawerActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(DrawerActivity.this,"信息不存在",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void updateDisagree(final Post post){
        //查询数据
        BmobQuery<Post>postBmobQuery=new BmobQuery<>();
        postBmobQuery.addWhereEqualTo("bmobUser",BmobUser.getCurrentUser());
        postBmobQuery.addWhereEqualTo("time",post.getTime());
//        postBmobQuery.addWhereEqualTo("content",post.getContent());
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    String objectId=list.get(0).getObjectId();
                    post.addDisagree();
                    post.addDisagreeList();
                    post.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                Toast.makeText(DrawerActivity.this,"成功",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(DrawerActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(DrawerActivity.this,"信息不存在",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


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
            BmobQuery<Post>postBmobQuery=new BmobQuery<>();
            postBmobQuery.order("-createdAt");
            postBmobQuery.findObjects(new FindListener<Post>() {
                @Override
                public void done(List<Post> list, BmobException e) {
                    Toast.makeText(DrawerActivity.this,String.valueOf(equalList(postList,list)),Toast.LENGTH_SHORT).show();
                    if (equalList(list,postList)!=3){
                        Toast.makeText(DrawerActivity.this,String.valueOf(list.size())+"+"+String.valueOf(postList.size()),Toast.LENGTH_SHORT).show();
                        postList.clear();
                        addPost(list);
                    }else {
                        Toast.makeText(DrawerActivity.this,"刷新",Toast.LENGTH_SHORT).show();
                    }
                }
            });

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
        } else if (id == R.id.nav_fans) {
            Intent intent=new Intent(DrawerActivity.this,FanActivity.class);
            startActivity(intent);
            DrawerActivity.this.finish();
        } else if (id == R.id.nav_settings) {
            Intent intent=new Intent(DrawerActivity.this,SettingActivity.class);
            startActivity(intent);
            DrawerActivity.this.finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if(id==R.id.nav_square){
            Intent intent=new Intent(DrawerActivity.this, SquareActivity.class);
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
