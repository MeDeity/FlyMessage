package com.deity.flymessage.entity;

import com.deity.flymessage.utils.Constants;

import cn.jpush.im.android.api.content.MediaContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;

/**
 * 作者：Rance on 2016/12/14 14:13
 * 邮箱：rance935@163.com
 */
public class MessageInfo {
    private int type;
    private String content;
    private String filepath;
    private int sendState;
    private String time;
    private String header;
    private String imageUrl;
    private long voiceTime;
    private String msgId;

    //扩展
    private Message message;
    private String mediaFilePath;
    private String progress;

    public MessageInfo(){}

    public MessageInfo(Message message,int code,String imageUrl,String filepath,long voiceTime){
        if (message.getDirect().equals(MessageDirect.receive)){
            this.type = Constants.CHAT_ITEM_TYPE_LEFT;
        }else {
            this.type = Constants.CHAT_ITEM_TYPE_RIGHT;
        }
        this.sendState = code;
        switch (message.getContentType()){
            case text:
                this.content = ((TextContent) message.getContent()).getText();
                break;
            case image:
                this.imageUrl = imageUrl;
                break;
            case voice:
                this.filepath = filepath;
                this.voiceTime = voiceTime;
                break;
        }

        this.message = message;
        ContentType contentType = message.getContentType();
        if (contentType == ContentType.voice || contentType == ContentType.image
                || contentType == ContentType.video || contentType == ContentType.file) {
            mediaFilePath = ((MediaContent) message.getContent()).getLocalPath();
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(long voiceTime) {
        this.voiceTime = voiceTime;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", filepath='" + filepath + '\'' +
                ", sendState=" + sendState +
                ", time='" + time + '\'' +
                ", header='" + header + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", voiceTime=" + voiceTime +
                ", msgId='" + msgId + '\'' +
                '}';
    }
}
