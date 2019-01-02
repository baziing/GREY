package com.example.grey;

import com.example.grey.account.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class EMUser extends BmobObject {

    public BmobUser bmobUser;
    public List<String>followerList=new ArrayList<>();

    public EMUser(){}
    public EMUser(BmobUser bmobUser){
        this.bmobUser=bmobUser;
    }

    public void addFollowerList(String string){
        if (!followerList.contains(string)){
            followerList.add(string);
        }
    }

    public void deleteFollowerList(String string){
        if (followerList.contains(string)){
            Iterator<String> it = followerList.iterator();
            while(it.hasNext()){
                String x = it.next();
                if(x.equals(string)){
                    it.remove();
                }
            }
        }
    }

}
