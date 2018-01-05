package com.renniji.mytask;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.renniji.mytask.constants.MyConstants;
import com.renniji.mytask.frame.MainTab;
import com.renniji.mytask.frame.MyFragmentTabHost;
import com.renniji.mytask.frame.OnTabReselectListener;
import com.renniji.mytask.net.AsyncCallBack;
import com.renniji.mytask.net.NetHelper;
import com.renniji.mytask.update.UpdateManager;
import com.renniji.mytask.utils.FileUtil;
import com.renniji.mytask.utils.LogUtils;
import com.renniji.mytask.utils.ScreenUtils;
import com.renniji.mytask.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener,
        View.OnTouchListener {

    public String TAG = getClass().getName();
    private static Boolean mIsExit = false;

    public int CurrentTabNum = 0;
    private MyFragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDataIntent();
        initView();
        initData();

        onClickUpdate();
    }

    private void initData() {
        ScreenUtils.getScreenInfo(this);
        String area_dataPath = FileUtil.getFilePath(MyConstants.AREA_DATA_DIR,
                MyConstants.AREA_DATA, MyConstants.TXT);
        if (!FileUtil.fileIsExists(area_dataPath)) {
            getAreaData();
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            if (i == mTabHost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouchEvent(event);
        boolean mIsConsumed = false;
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && v.equals(mTabHost.getCurrentTabView())) {
            Fragment mCurrentFragment = getCurrentFragment();
            if (mCurrentFragment != null
                    && mCurrentFragment instanceof OnTabReselectListener) {
                OnTabReselectListener listener = (OnTabReselectListener) mCurrentFragment;
                listener.onTabReselect();
                mIsConsumed = true;
            }
        }
        return mIsConsumed;
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }

    private void initView() {
        mTabHost = (MyFragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, this.getSupportFragmentManager(), R.id.myrealtabcontent);

        initTabs();
        mTabHost.setCurrentTab(CurrentTabNum);
        mTabHost.setOnTabChangedListener(this);
    }

    private void initTabs() {
        if (Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }
        MainTab[] mTabs = MainTab.values();
        final int size = mTabs.length;
        for (int i = 0; i < size; i++) {
            MainTab mainTab = mTabs[i];
            TabHost.TabSpec mTab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            View mIndicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.view_home_tab_indicator, null);
            TextView mTitle = (TextView) mIndicator
                    .findViewById(R.id.tab_title);
            ImageView mIcon = (ImageView) mIndicator
                    .findViewById(R.id.tab_icon);
            Drawable mDrawable = this.getResources().getDrawable(
                    mainTab.getResIcon());
            mIcon.setImageDrawable(mDrawable);
            mTitle.setText(getString(mainTab.getResName()));
            mTab.setIndicator(mIndicator);
            mTab.setContent(new TabHost.TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });
            mTabHost.addTab(mTab, mainTab.getClz(), null);

            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }

    private Fragment getCurrentFragment() {
        return this.getSupportFragmentManager().findFragmentByTag(
                mTabHost.getCurrentTabTag());
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        CurrentTabNum = intent.getIntExtra("CurrentTabNum", 0);

    }

    /**
     * 自动检查更新App
     */
    private void onClickUpdate() {
        new UpdateManager(this, true);
    }

    /**
     * 双击退出函数
     */
    private void exitBy2Click() {
        Timer tExit = null;
        if (mIsExit == false) {
            mIsExit = true; // 准备退出
            ToastUtils.show(R.string.tip_double_click_exit);
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    mIsExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     *
     * 获取地址信息
     */
    private void getAreaData() {
        NetHelper.getAreaInfo(new AsyncCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(String result) {
                resolveAreaData(result);
            }
        });

    }

    /**
     *
     * 解析地址信息
     *
     * @param json
     */
    private void resolveAreaData(String json) {
        try {
            LogUtils.logd(TAG, "resolveAreaDatajson:" + json);
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONArray areadata = obj.optJSONArray("result");
                FileUtil.saveFile(areadata.toString(),
                        MyConstants.AREA_DATA_DIR, MyConstants.AREA_DATA,
                        MyConstants.TXT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
