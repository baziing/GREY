package com.example.grey.edit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grey.R;
import com.example.grey.post.Post;
import com.example.grey.sensor.ChangeOrientationHandler;
import com.example.grey.sensor.OrientationSensorListener;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class EditActivity extends AppCompatActivity {

    private EditText editText;
    private Toolbar toolbar;
    private Handler handler;
    private OrientationSensorListener listener;
    private SensorManager sm;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        handler = new ChangeOrientationHandler(this);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(handler);
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditActivity.this.finish();
            }
        });

        editText=(EditText)findViewById(R.id.edit_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.trush:
                Toast.makeText(EditActivity.this,"trush",Toast.LENGTH_SHORT).show();
                editText.getText().clear();
                break;
            case R.id.gps:
                Toast.makeText(EditActivity.this,"gps",Toast.LENGTH_SHORT).show();
                //介入地图api
                break;
            case R.id.sendup:
                String inputText=editText.getText().toString();//获取输入的文字
                //获取当前的时间

                long time = System.currentTimeMillis();

                Post post=new Post(inputText, BmobUser.getCurrentUser(),0,0,BmobUser.getCurrentUser().getUsername());
                post.setTime(time);
                post.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null){
                    Toast.makeText(EditActivity.this, "建帖成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EditActivity.this, "建帖失败", Toast.LENGTH_SHORT).show();
                }
                    }
                });

//                Toast.makeText(EditActivity.this,inputText,Toast.LENGTH_SHORT).show();
                editText.getText().clear();
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
