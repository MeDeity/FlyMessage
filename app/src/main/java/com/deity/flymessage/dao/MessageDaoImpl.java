package com.deity.flymessage.dao;

import com.deity.flymessage.entity.MessageInfo;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 消息保存
 * Created by Deity on 2018/3/8.
 */

public class MessageDaoImpl {
    private static MessageDaoImpl messageDao;

    private MessageDaoImpl(){};

    public static MessageDaoImpl getInstance(){
        if (null== messageDao){
            synchronized (MessageDaoImpl.class){
                if (null== messageDao){
                    messageDao = new MessageDaoImpl();
                }
            }
        }
        return messageDao;
    }
    //保存信息
    public void saveNewMessage(MessageInfo saveMessage) {
        try {
            Realm.getDefaultInstance().beginTransaction();
            Realm.getDefaultInstance().copyToRealmOrUpdate(saveMessage);
//            MessageInfo message = Realm.getDefaultInstance().where(MessageInfo.class).equalTo("id",saveMessage.getId()).findFirst();
//            if (null==message){
//                message = Realm.getDefaultInstance().createObject(MessageInfo.class);
//            }
            System.out.println("saveNewMessage>>>"+saveMessage.toString());
            Realm.getDefaultInstance().commitTransaction();
        }catch (Exception e){
            System.out.println("save NewMessage Exception>>>"+e.getMessage());
            Realm.getDefaultInstance().cancelTransaction();
        }
    }


    public void removeMessage(String id) {
        try {
            Realm.getDefaultInstance().beginTransaction();
            MessageInfo removeMessage = Realm.getDefaultInstance().where(MessageInfo.class).equalTo("id",id).findFirst();
            if (null!=removeMessage){
                System.out.println("removeMessage>>>"+removeMessage.toString());
                removeMessage.deleteFromRealm();
            }
            Realm.getDefaultInstance().commitTransaction();
        }catch (Exception e){
            System.out.println("removeMessage Exception>>>"+e.getMessage());
            Realm.getDefaultInstance().cancelTransaction();
        }
    }


    public void removeMessageTx() {
        try {
            Realm.getDefaultInstance().beginTransaction();
            RealmResults<MessageInfo> messageResults = Realm.getDefaultInstance().where(MessageInfo.class).findAll();
            if (!messageResults.isEmpty()){
                System.out.println("存在需要清理的消息数据,数量:"+messageResults.size());
                messageResults.deleteAllFromRealm();
            }
            Realm.getDefaultInstance().commitTransaction();
        }catch (Exception e){
            System.out.println("removeMessageTx Exception>>>"+e.getMessage());
            Realm.getDefaultInstance().cancelTransaction();
        }
    }

    public RealmResults<MessageInfo> queryMessage(String targetUserName) {
        RealmResults<MessageInfo> messageResults = Realm.getDefaultInstance().where(MessageInfo.class).equalTo("targetUserName",targetUserName).findAll();
        return messageResults;
    }
}
