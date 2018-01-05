package com.renniji.mytask.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.renniji.mytask.R;
import com.renniji.mytask.activity.LoginActivity;
import com.renniji.mytask.activity.OtherWebActivity;
import com.renniji.mytask.bean.UserBean;
import com.renniji.mytask.common.AppManager;
import com.renniji.mytask.common.UserController;
import com.renniji.mytask.dialog.CustomProgressDialog;
import com.renniji.mytask.dialog.FrameProgressDialog;
import com.renniji.mytask.dialog.UpdateDialog;
import com.renniji.mytask.utils.AndroidUtils;

import java.util.List;


public abstract class BaseActivity extends AppCompatActivity {

    public String TAG = getClass().getName();
    public UserController mUserController;
    public UserBean user;
    public CustomProgressDialog dialog;
    public FrameProgressDialog frameDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AndroidUtils.exitActvityAnim(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0
                    || index >= fm.getFragments().size()) {
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {
            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }

    }

    private void init() {
        AppManager.getAppManager().addActivity(this);
        mUserController = UserController.getInstance(this);
        user = new UserBean();
        dialog = new CustomProgressDialog(this);
        frameDialog = new FrameProgressDialog(this);
    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

    /**
     * 打开H5界面
     *
     * @param context
     */
    public void startOtherWeb(Context context, String title, String url) {
        Intent intent = new Intent(context, OtherWebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public void startActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        AndroidUtils.enterActvityAnim(this);

    }

    public void showFrameDialog() {
        frameDialog.show();
    }

    public void dismissFrameDialog() {
        frameDialog.dismiss();
    }

    public void showmeidialog() {

        dialog.show();
    }

    public void dismissmeidialog() {
        dialog.dismiss();
    }

    /**
     * 提示登录
     */
    public void hintLogin(Context context) {
        showLoginDialog(context);
    }


    /**
     * 显示对话框
     *
     * @param context
     */
    public void showLoginDialog(final Context context) {

        UpdateDialog.Builder builder = new UpdateDialog.Builder(context);
        builder.setMessage(context.getString(R.string.you_no_login));
        builder.setTitle(context.getString(R.string.prompt_message));
        builder.setPositiveButton(context.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        builder.setNegativeButton(context.getString(R.string.now_login),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 设置你的操作事项
                        startActivity(context, LoginActivity.class);
                        dialog.dismiss();

                    }
                });

        builder.create().show();
    }



}
