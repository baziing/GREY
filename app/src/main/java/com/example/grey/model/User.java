package com.example.grey.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

//用户信息
public class User extends BmobUser{
    private BmobFile image;

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public BmobFile getImage() {
        return image;
    }
}
