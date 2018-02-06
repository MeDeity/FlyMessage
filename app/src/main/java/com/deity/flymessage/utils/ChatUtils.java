package com.deity.flymessage.utils;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;

/**
 * 极光IM工具类
 * Created by Deity on 2018/2/5.
 */

public class ChatUtils {

    //极光推送消息相关设置
    public MessageSendingOptions initMessageOptions(){
        //设置消息发送时的一些控制参数
        MessageSendingOptions options = new MessageSendingOptions();
        options.setNeedReadReceipt(true);//是否需要对方用户发送消息已读回执
        options.setRetainOffline(true);//是否当对方用户不在线时让后台服务区保存这条消息的离线消息
        options.setShowNotification(true);//是否让对方展示sdk默认的通知栏通知
        return options;
    }

    //发送单聊消息
    public void sendSingleTextMessage(String userName,String text,BasicCallback basicCallback){
        //通过username和appkey拿到会话对象，通过指定appkey可以创建一个和跨应用用户的会话对象，从而实现跨应用的消息发送
        Conversation mConversation = JMessageClient.getSingleConversation(userName);
        if (mConversation == null) {
            mConversation = Conversation.createSingleConversation(userName);
        }
        //构造message content对象
        TextContent textContent = new TextContent(text);
        //创建message实体，设置消息发送回调。
        Message message = mConversation.createSendMessage(textContent);
        message.setOnSendCompleteCallback(basicCallback);
//        message.setOnSendCompleteCallback(new BasicCallback() {
//            @Override
//            public void gotResult(int i, String s) {
//                if (i == 0) {
//                    //"发送成功"
//                } else {
//                    //"发送失败"
//                }
//            }
//        });
        //发送消息
        JMessageClient.sendMessage(message, initMessageOptions());
    }


}
