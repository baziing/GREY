package com.example.grey.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.grey.R;
import com.example.grey.home.DrawerActivity;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

//    private String[]option={"修改头像"};

    private List<Option>optionList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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
        Option option=new Option("修改头像",R.mipmap.ic_account_box_grey600_36dp);
        optionList.add(option);
    }
}
