package com.deity.flymessage.utils;

/**
 * 作者：Rance on 2016/12/20 16:51
 * 邮箱：rance935@163.com
 */
public class Constants {
    public static final String TAG = "rance";
    /** 0x001-接受消息  0x002-发送消息**/
    public static final int CHAT_ITEM_TYPE_LEFT = 0x001;
    public static final int CHAT_ITEM_TYPE_RIGHT = 0x002;
    /** 0x002-发送中  0x001-发送失败  0x000-发送成功**/
    public static final int CHAT_ITEM_SENDING = 0x0002;
    public static final int CHAT_ITEM_SEND_ERROR = 0x001;
    public static final int CHAT_ITEM_SEND_SUCCESS = 0x000;
}
