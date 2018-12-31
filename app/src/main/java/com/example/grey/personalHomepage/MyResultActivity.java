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
import com.example.grey.post.PostList;
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
    private PostList postList=new PostList();
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

                postList.refreshMyData(string);

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
    }

    private void initView(){
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
    }

    private void initData(){
        postList.initMyData(string);
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
                    AlertDialog.Builder dialog=new AlertDialog.Builder(MyResultActivity.this);
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
}
