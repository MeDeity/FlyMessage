package com.deity.flymessage;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deity.flymessage.adapter.ChatAdapter;
import com.deity.flymessage.adapter.CommonFragmentPagerAdapter;
import com.deity.flymessage.dao.MessageDaoImpl;
import com.deity.flymessage.entity.MessageInfo;
import com.deity.flymessage.fragment.ChatEmotionFragment;
import com.deity.flymessage.fragment.ChatFunctionFragment;
import com.deity.flymessage.utils.ChatUtils;
import com.deity.flymessage.utils.Constants;
import com.deity.flymessage.utils.GlobalOnItemClickManagerUtils;
import com.deity.flymessage.utils.MediaManager;
import com.deity.flymessage.widget.EmotionInputDetector;
import com.deity.flymessage.widget.NoScrollViewPager;
import com.deity.flymessage.widget.StateButton;
import com.jude.easyrecyclerview.EasyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import io.realm.RealmResults;

/**
 * 聊天界面
 * Created by Deity on 2018/1/29.
 */

public class ChatActivity extends AppCompatActivity {
    @BindView(R.id.chat_list)
    EasyRecyclerView chatList;
    @BindView(R.id.emotion_voice)
    ImageView emotionVoice;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.voice_text)
    TextView voiceText;
    @BindView(R.id.emotion_button)
    ImageView emotionButton;
    @BindView(R.id.emotion_add)
    ImageView emotionAdd;
    @BindView(R.id.emotion_send)
    StateButton emotionSend;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.emotion_layout)
    RelativeLayout emotionLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;
    private ChatEmotionFragment chatEmotionFragment;
    private ChatFunctionFragment chatFunctionFragment;
    private CommonFragmentPagerAdapter adapter;

    private ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private List<MessageInfo> messageInfos = new ArrayList<>();
    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;

    //----------------
    public static final int PAGE_MESSAGE_COUNT = 18;
    private int mOffset = PAGE_MESSAGE_COUNT;
    private String mTargetId;
    private UserInfo mMyInfo;
    private Conversation mConv;//会话
    private final static String TAG = "ChatActivity";
    private List<Message> mMsgList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initWidget();
        initToolbar();
        JMessageClient.registerEventReceiver(this);
        mTargetId = getIntent().getStringExtra("targetId");
        mMyInfo = JMessageClient.getMyInfo();
        mConv = JMessageClient.getSingleConversation(mTargetId);
        if (mConv == null) {
            Log.i(TAG, "create new conversation");
            mConv = Conversation.createSingleConversation(mTargetId);
        }

        LoadData();
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
    }



    private void initWidget() {
        fragments = new ArrayList<>();
        chatEmotionFragment = new ChatEmotionFragment();
        fragments.add(chatEmotionFragment);
        chatFunctionFragment = new ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(emotionLayout)
                .setViewPager(viewpager)
                .bindToContent(chatList)
                .bindToEditText(editText)
                .bindToEmotionButton(emotionButton)
                .bindToAddButton(emotionAdd)
                .bindToSendButton(emotionSend)
                .bindToVoiceButton(emotionVoice)
                .bindToVoiceText(voiceText)
                .build();

        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(editText);

        chatAdapter = new ChatAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);
        chatList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        chatAdapter.notifyDataSetChanged();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        mDetector.hideEmotionLayout(false);
                        mDetector.hideSoftInput();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        chatAdapter.addItemClickListener(itemClickListener);
    }

    /**
     * item点击事件
     */
    private ChatAdapter.onItemClickListener itemClickListener = new ChatAdapter.onItemClickListener() {
        @Override
        public void onHeaderClick(int position) {
            Toast.makeText(ChatActivity.this, "onHeaderClick", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onImageClick(View view, int position) {
//            int location[] = new int[2];
//            view.getLocationOnScreen(location);
//            FullImageInfo fullImageInfo = new FullImageInfo();
//            fullImageInfo.setLocationX(location[0]);
//            fullImageInfo.setLocationY(location[1]);
//            fullImageInfo.setWidth(view.getWidth());
//            fullImageInfo.setHeight(view.getHeight());
//            fullImageInfo.setImageUrl(messageInfos.get(position).getImageUrl());
//            EventBus.getDefault().postSticky(fullImageInfo);
//            startActivity(new Intent(MainActivity.this, FullImageActivity.class));
//            overridePendingTransition(0, 0);
        }

        @Override
        public void onVoiceClick(final ImageView imageView, final int position) {
            if (animView != null) {
                animView.setImageResource(res);
                animView = null;
            }
            switch (messageInfos.get(position).getType()) {
                case 1:
                    animationRes = R.drawable.voice_left;
                    res = R.mipmap.icon_voice_left3;
                    break;
                case 2:
                    animationRes = R.drawable.voice_right;
                    res = R.mipmap.icon_voice_right3;
                    break;
            }
            animView = imageView;
            animView.setImageResource(animationRes);
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
            MediaManager.playSound(messageInfos.get(position).getFilepath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animView.setImageResource(res);
                }
            });
        }
    };

    private void loadHistory(){
        RealmResults<MessageInfo> messageResults = MessageDaoImpl.getInstance().queryMessage(mTargetId);
        for (MessageInfo message:messageResults){
            messageInfos.add(message);
        }
    }
    /**
     * 构造聊天数据
     */
    private void LoadData() {
        loadHistory();
        chatAdapter.addAll(messageInfos);
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final MessageInfo messageInfo) {
        messageInfo.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
        ChatUtils.sendSingleMessage(mTargetId, messageInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.i("ChatActivity:",s);
            }
        });
        MessageDaoImpl.getInstance().saveNewMessage(messageInfo);
        messageInfos.add(messageInfo);
        chatAdapter.add(messageInfo);
        chatList.scrollToPosition(chatAdapter.getCount() - 1);
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
        Log.i(TAG,"ChatActivity onDestroy:"+ChatActivity.this.hashCode());
    }

    private void receiveMessage(final Message msg, final int code, final String imageUrl, final String mFilePath, final long voiceTime){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserInfo userInfo = (UserInfo) msg.getTargetInfo();
                String targetId = userInfo.getUserName();
                if (targetId.equals(mTargetId)) {//判断消息是否在当前会话中
                    MessageInfo message = new MessageInfo(msg,code,imageUrl, mFilePath, voiceTime);
                    MessageDaoImpl.getInstance().saveNewMessage(message);
                    Log.i(TAG,"ChatActivity hashCode:"+ChatActivity.this.hashCode());
                    messageInfos.add(message);
                    chatAdapter.add(message);
                    chatList.scrollToPosition(chatAdapter.getCount() - 1);
                }
            }
        });

    }

    @SuppressWarnings("unused")
    public void onEvent(MessageEvent event){
        final Message eventMessage = event.getMessage();
        switch (eventMessage.getContentType()) {
            case voice://TODO 下载在哪里呢 其实sdk是会自动下载语音的.本方法是当sdk自动下载失败时可以手动调用进行下载而设计的.同理于缩略图下载
                final VoiceContent voiceContent = (VoiceContent) eventMessage.getContent();
                final int duration = voiceContent.getDuration();
                final String format = voiceContent.getFormat();
                //=================     下载语音文件    =================
                voiceContent.downloadVoiceFile(eventMessage, new DownloadCompletionCallback() {
                    @Override
                    public void onComplete(int i, String s, File file) {
                        receiveMessage(eventMessage,i,"",file.getPath(),duration);
                    }
                });
                break;
            case image:
                final ImageContent imageContent = (ImageContent) eventMessage.getContent();
                //=================     下载图片消息中的原图    =================
                imageContent.downloadOriginImage(eventMessage, new DownloadCompletionCallback() {
                    @Override
                    public void onComplete(int i, String s, File file) {
                        receiveMessage(eventMessage,i,file.getPath(),"",0);
                    }
                });
                break;
            case text:
                receiveMessage(eventMessage,0,"","",0);
                break;
            default:
                break;
        }
    }

}
