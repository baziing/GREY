package com.example.grey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

import cn.bmob.v3.Bmob;

public class ECChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecchat);

        // 这里直接使用EaseUI封装好的聊天界面
        EaseChatFragment chatFragment = new EaseChatFragment();
        // 将参数传递给聊天界面
        chatFragment.setArguments(getIntent().getExtras());
        //加载EaseUi封装的聊天界面Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.ec_layout_container, chatFragment).commit();

//        //new出EaseChatFragment或其子类的实例
//        EaseChatFragment chatFragment = new EaseChatFragment();
//        //传入参数
//        Bundle args = new Bundle();
//        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
//        args.putString(EaseConstant.EXTRA_USER_ID, "qaz");
//        chatFragment.setArguments(args);
//        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }
}