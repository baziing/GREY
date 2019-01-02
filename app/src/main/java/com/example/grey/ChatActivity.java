package com.example.grey;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener,EMMessageListener {

    private EditText input_text;
    private Button send;
    private TextView text_content;

    private String userName;// 当前聊天的 ID

    // 当前会话对象
    private EMConversation mConversation;

    // 消息监听器
    private EMMessageListener mMessageListener;

    private final String TAG = "Chat";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        Intent intent = getIntent();
//        userName = intent.getStringExtra("userName");
        userName="qaz";
        mMessageListener = this;

        initView();
        initConversation();
    }

    private void initView() {


        input_text = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);

        text_content = (TextView) findViewById(R.id.text_content);
        // 设置textview可滚动，需配合xml布局设置
        text_content.setMovementMethod(new ScrollingMovementMethod());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.send:
                sendMessage();
                break;

            default:

                break;

        }
    }

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    private void initConversation() {

        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是绘画类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        mConversation = EMClient.getInstance().chatManager().getConversation(userName, null, true);

        for (int i = 0; i < mConversation.getAllMessages().size(); i++) {
            Log.e(TAG, "mConversation=" + mConversation.getAllMessages().get(i).toString() + "\n");
        }
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId, 20 - count);
        }
        // 打开聊天界面获取最后一条消息内容并显示
        if (mConversation.getAllMessages().size() > 0) {
            EMMessage messge = mConversation.getLastMessage();
            //  EMTextMessageBody body = (EMTextMessageBody) messge.getBody();
            // 将消息内容和时间显示出来
            //text_content.setText("聊天记录：" + body.getMessage() + " - time: " + mConversation.getLastMessage().getMsgTime());
        }
    }

    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                    // 将新的消息内容和时间加入到下边
                    Log.e("nsc", body.getMessage());
                    //text_content.setText(body.getMessage());
                    text_content.setText(text_content.getText() + "\n" + mConversation.getAllMessages().get(0).getTo() + ":" + body.getMessage() + " - time: " + message.getMsgTime());
                    break;
            }
        }
    };

    private void sendMessage() {

        String content = input_text.getText().toString();

        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, userName);
        //  text_content.setText(content);
//        text_content.setText(text_content.getText() + "\n" + mConversation.getAllMessages().get(0).getFrom() + ":" + content + " - time: " + message.getMsgTime());
        text_content.setText("1111111");
        //如果是群聊，设置chattype，默认是单聊
        // if (chatType == CHATTYPE_GROUP)
        //message.setChatType(EMMessage.ChatType.GroupChat);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        // 为消息设置回调
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                // 消息发送成功，打印下日志，正常操作应该去刷新ui
                Log.e(TAG, "send message on success");
            }

            @Override
            public void onError(int i, String s) {
                // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                Log.e(TAG, "send message on error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {
                // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }

    /**
     * 收到新消息
     *
     * @param messages
     */
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        //收到消息
        // 循环遍历当前收到的消息
        for (EMMessage message : messages) {
            if (message.getFrom().equals(userName)) {
                // 设置消息为已读
                mConversation.markMessageAsRead(message.getMsgId());

                // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            } else {
                // 如果消息不是当前会话的消息发送通知栏通知
            }
        }
    }

    /**
     * 收到新的 CMD 消息
     *
     * @param list
     */
    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            // 透传消息
            EMMessage cmdMessage = list.get(i);
            EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
            Log.e("nsc", body.action());
        }
    }

    /**
     * 收到新的已读回执
     *
     * @param messages
     */

    @Override
    public void onMessageRead(List<EMMessage> messages) {

    }

    /**
     * 收到新的发送回执
     *
     * @param messages
     */

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {

    }

    /**
     * 消息的状态改变
     *
     * @param emMessage
     * @param o
     */
    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {

    }
}