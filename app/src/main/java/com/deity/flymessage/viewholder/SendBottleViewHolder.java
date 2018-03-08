package com.deity.flymessage.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.deity.flymessage.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发送飘信的界面
 * Created by Deity on 2018/2/24.
 */

public class SendBottleViewHolder {
    /**编辑框*/
    @BindView(R.id.et_message)
    protected EditText et_message;
    /**取消*/
    @BindView(R.id.btn_cancel)
    protected Button btn_cancel;
    /**发送按钮*/
    @BindView(R.id.btn_send)
    protected Button btn_send;

    public SendBottleViewHolder(View view){
        ButterKnife.bind(this,view);
    }

    public EditText getEt_message() {
        return et_message;
    }

    public Button getBtn_cancel() {
        return btn_cancel;
    }

    public Button getBtn_send() {
        return btn_send;
    }
}
