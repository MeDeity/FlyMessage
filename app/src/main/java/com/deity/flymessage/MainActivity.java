package com.deity.flymessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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

    private void initViews(){
        beach_shui.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_shake));
        beach_lang.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_shake));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        String userStr = "用户名:%s,appKey:%s,版本号:%s";
//        UserInfo info = JMessageClient.getMyInfo();
//        tv_user_info.setText(String.format(userStr,info.getUserName(),info.getAppKey(),JMessageClient.getSdkVersionString()));
    }

    @SuppressWarnings("unused")
    @OnClick({R.id.send_nav_image,R.id.personal_setting})
    public void sendBottleMsg(View view){
        switch (view.getId()){
            case R.id.personal_setting:
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                break;
            case R.id.send_nav_image:
                new AlertDialog.Builder(MainActivity.this).setView(R.layout.dialog_bottle_send)
                        .show();
                break;
        }

//        bottle_sending.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_move_up));
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.get_nav_image)
    public void getBottleMsg(View view){
        new AlertDialog.Builder(MainActivity.this).setView(R.layout.dialog_bottle_receiver)
                .show();
//        bottle_getting.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_move_up));
    }


    @SuppressWarnings("unused")
    @OnClick(R.id.item_bottle_msg)
    public void getBottle(View view){
//        startActivity(new Intent(MainActivity.this,ConvertionActivity.class));
        startActivity(new Intent(MainActivity.this,ChatActivity.class));
    }
}
