package com.renniji.mytask.dialog;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.renniji.mytask.R;
import com.renniji.mytask.utils.StringUtils;


/**
 * @author http://blog.csdn.net/finddreams
 * @Description:自定义对话框
 */
public class CustomProgressDialog extends ProgressDialog {

    private TextView mLoadingTv;
    private String mLoadingTip;

    public CustomProgressDialog(Context context, String content) {
        super(context);
        this.mLoadingTip = content;
        setCanceledOnTouchOutside(true);
    }

    public CustomProgressDialog(Context context) {
        super(context);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        initView();
        initData();
    }

    private void initData() {
        if (!StringUtils.isEmpty(mLoadingTip)) {
            mLoadingTv.setText(mLoadingTip);
        }
    }

    public void setContent(String str) {
        mLoadingTv.setText(str);
    }

    private void initView() {
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
    }
}
