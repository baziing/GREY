package com.example.grey.post;

import android.widget.Toast;

import com.example.grey.post.Post;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class PostList{

    public List<Post> postList=new ArrayList<>();
    public boolean postIsCurrentUser;

    public PostList(){
    }

    public void initData(){
        Bmob.initialize(Bmob.getApplicationContext(), "796037cf0d5cb806545e84bed5238df5");
        BmobQuery<Post>postBmobQuery=new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    //添加到PostList中
                    addPost(list);
                }else {

                }
            }
        });
    }

    public void initData(int number){
        BmobQuery<Post>postBmobQuery=new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    //添加到PostList中
                    addPost(list,20);
                }else {

                }
            }
        });
    }

    public void initMyData(){
        BmobQuery<Post> postBmobQuery = new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.addWhereEqualTo("bmobUser", BmobUser.getCurrentUser());
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    //添加
                    addPost(list);
                } else {
                }
            }
        });
    }

    public void initData(String string){
        final String key=string;
        BmobQuery<Post> postBmobQuery=new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                List<Post> aPostList=new ArrayList<>();
                for (int i=0;i<list.size();i++){
                    if(list.get(i).getContent().contains(key))
                        aPostList.add(list.get(i));
                }
                //添加
                addPost(aPostList);
            }
        });
    }

    public void initMyData(String string){
        final String key=string;
        BmobQuery<Post> postBmobQuery=new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.addWhereEqualTo("bmobUser", BmobUser.getCurrentUser());
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                List<Post> aPostList=new ArrayList<>();
                for (int i=0;i<list.size();i++){
                    if(list.get(i).getContent().contains(key))
                        aPostList.add(list.get(i));
                }
                //添加
                addPost(aPostList);
            }
        });
    }

    public void refreshData(){
        BmobQuery<Post> postBmobQuery=new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (equalList(list,postList)!=3){
                    postList.clear();
                    initData();
                }else {

                }
            }
        });
    }

    public void refreshData(final int number){
        BmobQuery<Post> postBmobQuery=new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (equalList(list,postList)!=3){
                    postList.clear();
                    initData(number);
                }
            }
        });
    }

    public void refreshData(String string){
        final String key=string;
        BmobQuery<Post> postBmobQuery=new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                List<Post> aPostList=new ArrayList<>();
                for (int i=0;i<list.size();i++){
                    if(list.get(i).getContent().contains(key))
                        aPostList.add(list.get(i));
                }
                //添加
                if (equalList(aPostList,postList)!=3){
                    postList.clear();
                    initData(key);
                }
            }
        });
    }

    public void refreshMyData(){
        BmobQuery<Post> postBmobQuery=new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.addWhereEqualTo("bmobUser", BmobUser.getCurrentUser());
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (equalList(list,postList)!=3){
                    postList.clear();
                    initMyData();
                }
            }
        });
    }

    public void refreshMyData(String string){
        final String key=string;
        BmobQuery<Post> postBmobQuery=new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.addWhereEqualTo("bmobUser", BmobUser.getCurrentUser());
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                List<Post> aPostList=new ArrayList<>();
                for (int i=0;i<list.size();i++){
                    if(list.get(i).getContent().contains(key))
                        aPostList.add(list.get(i));
                }
                //添加
                if (equalList(aPostList,postList)!=3){
                    postList.clear();
                    initMyData(key);
                }
            }
        });
    }

    public void addPost(List<Post>list){
        int listSize=list.size();

        for (int i=0;i<listSize;i++){
//            postList.add(list.get(listSize-i-1));
            this.postList.add(list.get(i));
        }
    }

    public void addPost(List<Post>list,int number){
        int liseSize=number;

        if (list.size()<number){
            liseSize=list.size();
        }

        for (int i=0;i<liseSize;i++){
            this.postList.add(list.get(i));
        }
    }

    public void updateAgree(int position){
        final Post post=this.postList.get(position);
        BmobQuery<Post>postBmobQuery=new BmobQuery<>();
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
                            }
                            else {
                            }
                        }
                    });
                }
                else {
                }
            }
        });
    }

    public void updateDisagree(int position){
        final Post post=this.postList.get(position);
        BmobQuery<Post>postBmobQuery=new BmobQuery<>();
        postBmobQuery.addWhereEqualTo("time",post.getTime());
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
                            }
                            else {
                            }
                        }
                    });
                }
                else {
                }
            }
        });
    }

    public void deletePost(int position){
        final Post post=this.postList.get(position);
        BmobQuery<Post>postBmobQuery=new BmobQuery<>();
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
                            }else {
                            }
                        }
                    });
                }else {
                }
            }
        });
    }

    public void repeatPost(int position){
        final Post post=this.postList.get(position);
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
            }
        });
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

    public boolean isCurrentUser(int position){
        BmobQuery<Post>postBmobQuery=new BmobQuery<>();
        postBmobQuery.addWhereEqualTo("time",this.postList.get(position).getTime());
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                String objectId=list.get(0).getObjectId();
                boolean flag;
                if (list.get(0).getName().equals(BmobUser.getCurrentUser().getUsername())){
                    flag=true;
                    isFlag(flag);
                }else {
                    flag=false;
                    isFlag(flag);
                }
            }
        });
        return postIsCurrentUser;
    }

    public void isFlag(boolean flag){
        postIsCurrentUser=flag;
    }

}
