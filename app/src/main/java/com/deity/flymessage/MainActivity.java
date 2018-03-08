package com.deity.flymessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.deity.flymessage.viewholder.ReceiverBottleViewHolder;
import com.deity.flymessage.viewholder.SendBottleViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.beach_shui)
    public ImageView beach_shui;

    @BindView(R.id.beach_lang)
    public ImageView beach_lang;

    @BindView(R.id.personal_setting)
    public ImageView personal_setting;

    @BindView(R.id.bottle_getting)
    public ImageView bottle_getting;

    @BindView(R.id.bottle_sending)
    public ImageView bottle_sending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    //动画设置
    private void initViews(){
        beach_shui.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_shake));
        beach_lang.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_shake));
    }

    //扔漂流瓶
    private void actionSendBottleMsg(){
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View sendBottleView = layoutInflater.inflate(R.layout.dialog_bottle_send,null);
        final SendBottleViewHolder sendBottleViewHolder = new SendBottleViewHolder(sendBottleView);
        final AlertDialog dialog  = new AlertDialog.Builder(MainActivity.this).setView(sendBottleView).create();
        sendBottleViewHolder.getBtn_cancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        sendBottleViewHolder.getBtn_send().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = sendBottleViewHolder.getEt_message().getText().toString().trim();
                //TODO 发送消息
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    //扔漂流瓶
    private void actionGetBottleMsg(){
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View receiverBottleView = layoutInflater.inflate(R.layout.dialog_bottle_receiver,null);
        final ReceiverBottleViewHolder receiverBottleViewHolder = new ReceiverBottleViewHolder(receiverBottleView);
        final AlertDialog dialog  = new AlertDialog.Builder(MainActivity.this).setView(receiverBottleView).create();
        receiverBottleViewHolder.getBtn_cancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        receiverBottleViewHolder.getBtn_response().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = receiverBottleViewHolder.getTv_message().getText().toString().trim();
                //TODO 回复消息
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressWarnings("unused")
    @OnClick({R.id.send_nav_image,R.id.personal_setting})
    public void sendBottleMsg(View view){
        switch (view.getId()){
            case R.id.personal_setting:
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                break;
            case R.id.send_nav_image:
//                new AlertDialog.Builder(MainActivity.this).setView(R.layout.dialog_bottle_send).show();
                actionSendBottleMsg();
                break;
        }

//        bottle_sending.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_move_up));
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.get_nav_image)
    public void getBottleMsg(View view){
//        new AlertDialog.Builder(MainActivity.this).setView(R.layout.dialog_bottle_receiver).show();
//        bottle_getting.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_move_up));
        actionGetBottleMsg();
    }


    @SuppressWarnings("unused")
    @OnClick(R.id.item_bottle_msg)
    public void getBottle(View view){
        startActivity(new Intent(MainActivity.this,ConvertionActivity.class));
//        startActivity(new Intent(MainActivity.this,ChatActivity.class));
    }
}
