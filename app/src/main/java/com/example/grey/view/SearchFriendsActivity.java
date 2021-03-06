package com.example.grey.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.grey.R;
import com.example.grey.model.User;
import com.example.grey.model.EMUser;
import com.example.grey.sensor.ChangeOrientationHandler;
import com.example.grey.sensor.OrientationSensorListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

public class SearchFriendsActivity extends AppCompatActivity {

    private SearchView searchView;

    private Handler handler;
    private OrientationSensorListener listener;
    private SensorManager sm;
    private Sensor sensor;

    boolean flag=false;
    boolean flag1=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        handler = new ChangeOrientationHandler(this);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(handler);
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);

        // 绑定组件
        searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(final String string) {
                System.out.println("我收到了" + string);

                if (!isExit(string)){
//            Toast.makeText(SearchFriendsActivity.this,string+"不存在",Toast.LENGTH_LONG).show();
                    return;
                }
                if (isFollow(string)){
//            Toast.makeText(SearchFriendsActivity.this, string+"已经存在",Toast.LENGTH_LONG).show();
                    return;
                }

                AlertDialog.Builder dialog=new AlertDialog.Builder(SearchFriendsActivity.this);
                dialog.setTitle("确认信息");
                dialog.setMessage("确定关注 "+ string +" ？" );
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //确定关注对方
                        addFollowerList(string);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

        // 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                Intent intent=new Intent(SearchFriendsActivity.this, DrawerActivity.class);
                startActivity(intent);
                SearchFriendsActivity.this.finish();
            }
        });
    }

    public void addFollowerList(final String string){
//        if (!isExit(string)){
////            Toast.makeText(SearchFriendsActivity.this,string+"不存在",Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (isFollow(string)){
////            Toast.makeText(SearchFriendsActivity.this, string+"已经存在",Toast.LENGTH_LONG).show();
//            return;
//        }
        BmobQuery<EMUser>emUserBmobQuery=new BmobQuery<>();
        emUserBmobQuery.addWhereEqualTo("bmobUser", BmobUser.getCurrentUser());
        emUserBmobQuery.findObjects(new FindListener<EMUser>() {
            @Override
            public void done(List<EMUser> list, BmobException e) {
                if (e==null){
                    String objectId=list.get(0).getObjectId();
                    EMUser emUser=list.get(0);
                    emUser.addFollowerList(string);
                    emUser.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                //更新成功
                                Toast.makeText(SearchFriendsActivity.this,"关注成功",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //更新失败
                                Toast.makeText(SearchFriendsActivity.this,"关注失败"+e,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SearchFriendsActivity.this,"关注失败"+e,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isExit(String string){
        BmobQuery<User>userBmobQuery=new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("username",string);
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e==null){
                    flag=true;
                }else {
                    flag=false;
                }
            }
        });
        return flag;
    }

    public boolean isFollow(final String string){
        BmobQuery<EMUser>emUserBmobQuery=new BmobQuery<>();
        emUserBmobQuery.addWhereEqualTo("bmobUser",BmobUser.getCurrentUser());
        emUserBmobQuery.findObjects(new FindListener<EMUser>() {
            @Override
            public void done(List<EMUser> list, BmobException e) {
                if (e==null){
                    List<String>strings=new ArrayList<>();
                    strings.addAll(list.get(0).followerList);
                    if (strings.contains(string)){
                        flag1=true;
                    }
                }
            }
        });
        return flag1;
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
