package com.example.grey.personalHomepage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.grey.edit.EditActivity;
import com.example.grey.home.DrawerActivity;
import com.example.grey.post.MyRecyclerViewAdapter;
import com.example.grey.post.Post;
import com.example.grey.post.PostItemDecoration;
import com.example.grey.search.ResultActivity;
import com.example.grey.sensor.ChangeOrientationHandler;
import com.example.grey.sensor.OrientationSensorListener;
import com.example.grey.setting.Background;
import com.example.grey.setting.BackgroundActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ScrollingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private List<Post> postList=new ArrayList<>();

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

        myRecyclerViewAdapter=new MyRecyclerViewAdapter(postList,this);
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

        BmobQuery<Post> postBmobQuery = new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.addWhereEqualTo("bmobUser", BmobUser.getCurrentUser());
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    Toast.makeText(ScrollingActivity.this, String.valueOf(list.size()), Toast.LENGTH_SHORT).show();
                    addPost(list);
                } else {
                    Toast.makeText(ScrollingActivity.this, "失败", Toast.LENGTH_SHORT).show();
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
                    deletePost(postList.get(position));
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v) {

        }
    };

    public void deletePost(final Post post){

        //提示框，是否要进行删除
        AlertDialog.Builder dialog=new AlertDialog.Builder(ScrollingActivity.this);
        dialog.setTitle("确认信息");
        dialog.setMessage("确定删除信息？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BmobQuery<Post> postBmobQuery=new BmobQuery<>();
                postBmobQuery.addWhereEqualTo("bmobUser", BmobUser.getCurrentUser());
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
                                        Toast.makeText(ScrollingActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(ScrollingActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(ScrollingActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(ScrollingActivity.this,"成功",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ScrollingActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(ScrollingActivity.this,"信息不存在",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(ScrollingActivity.this,"成功",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ScrollingActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(ScrollingActivity.this,"信息不存在",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//
//    private Bitmap getURLimage(String imageUrl) {
//        Bitmap bmp = null;
//        try {
//            URL myurl = new URL(imageUrl);
//            // 获得连接
//            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
//            conn.setConnectTimeout(6000);//设置超时
//            conn.setDoInput(true);
//            conn.setUseCaches(false);//不缓存
//            conn.connect();
//            InputStream is = conn.getInputStream();//获得图片的数据流
//            bmp = BitmapFactory.decodeStream(is);
//            is.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bmp;
//
//    }

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
                BmobQuery<Post>postBmobQuery=new BmobQuery<>();
                postBmobQuery.order("-createdAt");
                postBmobQuery.addWhereEqualTo("bmobUser",BmobUser.getCurrentUser());
                postBmobQuery.findObjects(new FindListener<Post>() {
                    @Override
                    public void done(List<Post> list, BmobException e) {
                        Toast.makeText(ScrollingActivity.this,String.valueOf(equalList(postList,list)),Toast.LENGTH_SHORT).show();
                        if (equalList(list,postList)!=3){
                            Toast.makeText(ScrollingActivity.this,String.valueOf(list.size())+"+"+String.valueOf(postList.size()),Toast.LENGTH_SHORT).show();
                            postList.clear();
                            addPost(list);
                        }else {
                            Toast.makeText(ScrollingActivity.this,"刷新",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                myRecyclerViewAdapter.notifyDataSetChanged();
                Toast.makeText(this,"refresh",Toast.LENGTH_SHORT).show();
                break;
                default:
        }
        return true;
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
