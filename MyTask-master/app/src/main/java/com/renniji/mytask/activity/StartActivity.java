package com.renniji.mytask.activity;

import android.content.Intent;
import android.os.Bundle;

import com.renniji.mytask.MainActivity;
import com.renniji.mytask.R;
import com.renniji.mytask.base.BaseActivity;
import com.renniji.mytask.utils.AndroidUtils;
import com.renniji.mytask.utils.ScreenUtils;
import com.renniji.mytask.utils.SharedpreferncesUtil;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initView();
        initData();

    }

    protected void initView() {
        DelayedJumpNext();
    }

    protected void initData() {
        ScreenUtils.getScreenInfo(this);
    }

    /**
     * 延时跳转下一页
     */
    private void DelayedJumpNext() {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    public void run() {
                        if (SharedpreferncesUtil
                                .getGuided(getApplicationContext())) {
                            gotoMainActivity();
                        } else {
                            gotoWelcomeActivity();
                        }
                    }
                });
            }
        }, 2000);
    }

    private void gotoMainActivity() {
        int CurrentTabNum = 0;
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("CurrentTabNum", CurrentTabNum);
        startActivity(intent);
        StartActivity.this.finish();
    }

    private void gotoWelcomeActivity() {
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        StartActivity.this.startActivity(intent);
        StartActivity.this.finish();
    }

    @Override
    protected void onResume() {
//        JPushInterface.onResume(this);
        super.onResume();

    }

    @Override
    protected void onPause() {
//        JPushInterface.onPause(this);
        super.onPause();

    }
}
