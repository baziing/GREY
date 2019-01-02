package com.example.grey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

public class ConversationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
//        conversationListFragment.setConversationListItemClickListener(new EaseConversationListItemClickListener() {
//
//            @Override
//            public void onListItemClicked(EMConversation conversation) {
//                startActivity(new Intent(ConversationListActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
//            }
//        });


        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(ConversationListActivity.this, ECChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
            }
        });

        //加载EaseUi封装的聊天界面Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.ec_layout_container, conversationListFragment).commit();
    }
}
