package com.example.grey.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grey.R;
import com.example.grey.account.LoginActivity;

import cn.bmob.v3.BmobUser;

public class NavHeaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_drawer);

        TextView name_header=(TextView)findViewById(R.id.textView_nav_header_title);
        TextView email_header=(TextView)findViewById(R.id.textView_nav_header);

//        Intent intent=getIntent();
//        name_header.setText(intent.getStringExtra("username"));
//        email_header.setText(intent.getStringExtra("email"));

//        BmobUser bmobUser = BmobUser.getCurrentUser();
//        if(bmobUser != null){
//            // 允许用户使用应用
//            Toast.makeText(NavHeaderActivity.this,bmobUser.getUsername()+"欢迎登陆",Toast.LENGTH_SHORT).show();
//            username.setText(bmobUser.getUsername());
//            email.setText(bmobUser.getEmail());
//            Intent intent=new Intent(NavHeaderActivity.this,DrawerActivity.class);
//            startActivity(intent);
////            NavHeaderActivity.this.finish();
//        }else{
//            //缓存用户对象为空时， 可打开用户注册界面…
//            Toast.makeText(NavHeaderActivity.this,"无用户消息",Toast.LENGTH_SHORT).show();
//        }

    }
}
