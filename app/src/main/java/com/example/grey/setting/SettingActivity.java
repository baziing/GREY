package com.example.grey.setting;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.grey.R;
import com.example.grey.home.DrawerActivity;
import com.example.grey.sensor.ChangeOrientationHandler;
import com.example.grey.sensor.OrientationSensorListener;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

//    private String[]option={"修改头像"};

    private List<Option>optionList=new ArrayList<>();
    private Handler handler;
    private OrientationSensorListener listener;
    private SensorManager sm;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        handler = new ChangeOrientationHandler(this);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(handler);
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);

        initOptions();
        OptionAdapter adapter=new OptionAdapter(SettingActivity.this,R.layout.option_item,optionList);
        ListView listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Option option=optionList.get(position);
                switch (position){
                    case 0:
                        Toast.makeText(SettingActivity.this,option.getName(),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(SettingActivity.this,BackgroundActivity.class);
                        startActivity(intent);
//                        SettingActivity.this.finish();
                        break;
                        default:
                }
            }
        });

        //初始化toolbar
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(SettingActivity.this,DrawerActivity.class);
                startActivity(intent);
                SettingActivity.this.finish();
            }
        });
    }


    //添加对应的选项
    private void initOptions(){
        Option option=new Option("修改背景",R.mipmap.ic_account_box_grey600_36dp);
        optionList.add(option);
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
