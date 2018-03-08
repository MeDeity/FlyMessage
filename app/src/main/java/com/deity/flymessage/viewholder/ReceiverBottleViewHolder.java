package com.deity.flymessage.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deity.flymessage.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 接收飘信的界面
 * Created by Deity on 2018/2/27.
 */

public class ReceiverBottleViewHolder {
    @BindView(R.id.tv_message)
    protected TextView tv_message;
    @BindView(R.id.btn_cancel)
    protected Button btn_cancel;
    @BindView(R.id.btn_response)
    protected Button btn_response;

    public ReceiverBottleViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public TextView getTv_message() {
        return tv_message;
    }

    public Button getBtn_cancel() {
        return btn_cancel;
    }

    public Button getBtn_response() {
        return btn_response;
    }
}
