package com.example.grey.personalHomepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
import com.example.grey.sensor.ChangeOrientationHandler;
import com.example.grey.sensor.OrientationSensorListener;
import com.example.grey.setting.Background;
import com.example.grey.setting.BackgroundActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ScrollingActivity extends AppCompatActivity {

    private ImageView imageView;
    private Handler handler;
    private OrientationSensorListener listener;
    private SensorManager sm;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

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
                default:
        }
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
