package com.deity.flymessage.viewholder;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deity.flymessage.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by Mr.Jude on 2015/2/22.
 */
public class PersonViewHolder extends BaseViewHolder<UserInfo> {
    private TextView mTv_name;
    private ImageView mImg_face;
    private TextView mTv_sign;


    public PersonViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_person);
        mTv_name = $(R.id.person_name);
        mTv_sign = $(R.id.person_sign);
        mImg_face = $(R.id.person_face);
    }

    /**获取会话消息*/
    private Conversation obtainCov(String userName){
        Conversation conversation = JMessageClient.getSingleConversation(userName);
        return conversation;
    }

    /**未读消息数*/
    private int unReadNum(String userName){
        int unReadMsgCnt = 0;
        Conversation conversation = obtainCov(userName);
        if (null!=conversation){
            unReadMsgCnt = conversation.getUnReadMsgCnt();
        }
        return unReadMsgCnt;
    }

    private String lastMessage(String userName){
        String message="";
        Message latestMessage = null;
        Conversation conversation = obtainCov(userName);
        if (null!=conversation){
            latestMessage = conversation.getLatestMessage();
        }
        if (latestMessage != null) {
            MessageContent content = latestMessage.getContent();
            if (content.getContentType() == ContentType.text) {
                TextContent stringExtra = (TextContent) content;
                message = stringExtra.getText();
            }
        }
        return message;
    }

    private boolean resetUnReadNum(String userName){
        boolean resetUnReadMsgCnt = false;
        Conversation conversation = obtainCov(userName);
        if (null!=conversation){
            resetUnReadMsgCnt = conversation.resetUnreadCount();
        }
        return resetUnReadMsgCnt;
    }

    @Override
    public void setData(final UserInfo person){
        Log.i("ViewHolder","position"+getDataPosition());
        mTv_name.setText(person.getUserName());
        mTv_sign.setText(lastMessage(person.getUserName()));//未读:unReadNum(person.getUserName())
        Glide.with(getContext())
                .load(person.getAvatar())
                .placeholder(R.drawable.default_image)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(mImg_face);
    }
}
