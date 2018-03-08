package com.deity.flymessage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.deity.flymessage.data.Params;
import com.deity.flymessage.utils.FlyMessageUtils;
import com.deity.flymessage.viewholder.PersonViewHolder;
import com.github.clans.fab.FloatingActionButton;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 会话记录
 * Created by Deity on 2018/2/6.
 */

public class ConvertionActivity extends AppCompatActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.recyclerView)
    protected EasyRecyclerView recyclerView;

    @BindView(R.id.top)
    protected FloatingActionButton top;

    private RecyclerArrayAdapter<UserInfo> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convertion);
        ButterKnife.bind(this);
        initRecycleView();
    }

    /**获取会话消息*/
    private void obtainConversationList(){
        List<Conversation> conversationList = JMessageClient.getConversationList();
        if (conversationList != null) {
            for (Conversation convList : conversationList) {
                List<UserInfo> userInfoList = new ArrayList<>();
                if (convList.getType().toString().equals(Params.SINGLE_CHAT)) {//单聊类型
                    UserInfo userInfo = (UserInfo) convList.getTargetInfo();
                    userInfoList.add(userInfo);
                    adapter.addAll(userInfoList);
                }
            }
        }
    }

    private void initRecycleView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, FlyMessageUtils.dip2px(this,0.5f), FlyMessageUtils.dip2px(this,72),0);
        itemDecoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<UserInfo>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new PersonViewHolder(parent);
            }
        });

        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position) {
                adapter.remove(position);
                return true;
            }
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ConvertionActivity.this,ChatActivity.class);
                intent.putExtra("targetId", adapter.getItem(position).getUserName());
                startActivity(intent);
            }
        });
        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }

        });

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
            }
        });
        recyclerView.setRefreshListener(this);
        onRefresh();
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        //刷新
        adapter.clear();
        obtainConversationList();
    }

    @Override
    public void onLoadMore() {
        //加载更多，一次性加载所有，不分页
        adapter.stopMore();
    }
}
