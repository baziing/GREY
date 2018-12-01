package com.example.grey.personalHomepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.grey.R;
import com.example.grey.edit.EditActivity;
import com.example.grey.home.DrawerActivity;

import cn.bmob.v3.BmobUser;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
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

    }

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
}
