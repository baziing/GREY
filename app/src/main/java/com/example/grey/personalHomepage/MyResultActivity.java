package com.example.grey.personalHomepage;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.grey.R;
import com.example.grey.post.MyRecyclerViewAdapter;
import com.example.grey.post.Post;
import com.example.grey.post.PostItemDecoration;
import com.example.grey.search.ResultActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyResultActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private List<Post> postList=new ArrayList<>();
    private String string;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_result);

        Intent intent=getIntent();
        string=intent.getStringExtra("key");
        Toast.makeText(MyResultActivity.this,string,Toast.LENGTH_SHORT).show();

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MyResultActivity.this.finish();
            }
        });

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                BmobQuery<Post>postBmobQuery=new BmobQuery<>();
                postBmobQuery.order("-createdAt");
                postBmobQuery.addWhereEqualTo("bmobUser",BmobUser.getCurrentUser());
                postBmobQuery.findObjects(new FindListener<Post>() {
                    @Override
                    public void done(List<Post> list, BmobException e) {
                        Toast.makeText(MyResultActivity.this,String.valueOf(equalList(postList,list)),Toast.LENGTH_SHORT).show();
                        if (equalList(list,postList)!=3){
                            Toast.makeText(MyResultActivity.this,String.valueOf(list.size())+"+"+String.valueOf(postList.size()),Toast.LENGTH_SHORT).show();
                            postList.clear();
                            addPost(list);
                        }else {
                            Toast.makeText(MyResultActivity.this,"刷新",Toast.LENGTH_SHORT).show();
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

        BmobQuery<Post> postBmobQuery=new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.addWhereEqualTo("bmobUser", BmobUser.getCurrentUser());
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                List<Post>aPostList=getmPostList(list);
                addPost(aPostList);
            }
        });
    }

    public List<Post> getmPostList(List<Post>mPostList) {

        String key=string;
        List<Post> aPostList=new ArrayList<>();

        for (int i=0;i<mPostList.size();i++){
//            if (matchContent(mPostList.get(i).getContent(),key))
//                aPostList.add(mPostList.get(i));
            if(mPostList.get(i).getContent().contains(key))
                aPostList.add(mPostList.get(i));
//            aPostList.add(mPostList.get(i));
        }

        return aPostList;
    }

    private boolean matchContent(String parent,String child){
        String[]array=parent.split(child);
        if ((array.length-1)==child.length())
            return true;
        else
            return false;
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

    private void addPost(List<Post>list){
        int listSize=list.size();

        for (int i=0;i<listSize;i++){
//            postList.add(list.get(listSize-i-1));
            postList.add(list.get(i));
        }
    }


    public void deletePost(final Post post){

        //提示框，是否要进行删除
        AlertDialog.Builder dialog=new AlertDialog.Builder(MyResultActivity.this);
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
                                        Toast.makeText(MyResultActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(MyResultActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(MyResultActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
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
                    post.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                Toast.makeText(MyResultActivity.this,"成功",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(MyResultActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MyResultActivity.this,"信息不存在",Toast.LENGTH_SHORT).show();
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
                    post.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                Toast.makeText(MyResultActivity.this,"成功",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(MyResultActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MyResultActivity.this,"信息不存在",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
