package com.example.grey.model;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class Post extends BmobObject {

    private /*static*/ String content;
    private /*static*/ BmobUser author;

    private int agree;
    private int disagree;

    private BmobUser bmobUser;

    private String name;

    private long time;

    private List<String>agreeList=new ArrayList<>();
    private List<String>disagreeList=new ArrayList<>();

    public Post(){

    }

    public Post(String content,BmobUser bmobUser,int agree,int disagree){
        this.content=content;
        this.agree=agree;
        this.disagree=agree;
        this.bmobUser=bmobUser;
    }

    public Post(String content,BmobUser bmobUser,int agree,int disagree,String name){
        this.content=content;
        this.agree=agree;
        this.disagree=agree;
        this.bmobUser=bmobUser;
        this.name=name;
    }

    public Post(String content,int agree,int disagree){
        this.content=content;
        this.agree=agree;
        this.disagree=agree;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public BmobUser getAuthor() {
        return author;
    }

    public void setAuthor(BmobUser author) {
        this.author = author;
    }

    public void setAgree(int i){
        agree=i;
    }

    public void setDisagree(int i){
        disagree=i;
    }

    public int getAgree(){
        return agree;
    }

    public int getDisagree(){
        return disagree;
    }

    public void addAgree(){
        agree=agree+1;
    }

    public void deleteAgree(){
        agree=agree-1;
    }

    public void addDisagree(){
        disagree=disagree+1;
    }

    public void deleteDisagree(){
        disagree=disagree-1;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time){
        this.time=time;
    }

    public boolean isFoundAgree(){
        //搜索
        BmobUser user = BmobUser.getCurrentUser();
        String id=user.getObjectId();
        return this.agreeList.contains(id);
    }

    public boolean isFoundDisagree(){
        //搜索
        BmobUser user = BmobUser.getCurrentUser();
        String id=user.getObjectId();
        return this.disagreeList.contains(id);
    }

    public void addAgreeList(){
        BmobUser user = BmobUser.getCurrentUser();
        String id=user.getObjectId();
        this.agreeList.add(id);
    }

    public void addDisagreeList(){
        BmobUser user = BmobUser.getCurrentUser();
        String id=user.getObjectId();
        this.disagreeList.add(id);
    }
}
