package com.example.grey.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class Background extends BmobObject {
    private BmobFile background;
    private String Url;
    private BmobUser user;

    public Background(){

    }

    public Background(BmobFile file){
        this.background=file;
    }

    public Background(BmobFile file,BmobUser user){
        this.background=file;
        this.user=user;
    }

    public void setBackground(BmobFile background) {
        this.background = background;
    }

    public void setUser(BmobUser user){
        this.user=user;
    }

    public BmobFile getBackground() {
        return background;
    }

    public BmobUser getUser(){
        return user;
    }

    public String getUrl() {
        return background.getFileUrl();
    }
}
