package com.renniji.mytask.update;

import android.content.Context;
import android.content.DialogInterface;

import com.renniji.mytask.R;
import com.renniji.mytask.common.MyApplication;
import com.renniji.mytask.constants.MyConstants;
import com.renniji.mytask.dialog.UpdateDialog;
import com.renniji.mytask.net.AsyncCallBack;
import com.renniji.mytask.net.NetHelper;
import com.renniji.mytask.utils.AndroidUtils;
import com.renniji.mytask.utils.JSONUtil;
import com.renniji.mytask.utils.LogUtils;
import com.renniji.mytask.utils.StringUtils;
import com.renniji.mytask.utils.ToastUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;


/**
 * 更新管理类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年11月18日 下午4:21:00
 */
@SuppressWarnings("deprecation")
public class UpdateManager {

    private String TAG = "UpdateManager";
    private String mAppType = MyConstants.ANDROID;
    private boolean isShow = false;
    private double curVersionCode;
    private double netVersionCode;

    private Context mContext;
    private String version;
    private String download;
    private String content;


    public UpdateManager(Context context, boolean isShow) {
        this.mContext = context;
        this.isShow = isShow;
        curVersionCode = AndroidUtils.getVersionCode(MyApplication.getContext()
                .getPackageName());
        LogUtils.logd(TAG, "curVersionCode:" + curVersionCode);
        getNetVersionInfo();
    }

    public boolean haveNew() {
        boolean haveNew = false;
        if (curVersionCode < netVersionCode) {
            haveNew = true;
        }
        return haveNew;
    }

    public void checkUpdate() {
        if (curVersionCode < netVersionCode) {
            LogUtils.logd(TAG, "showAlertDialog:前");
            showAlertDialog(mContext);
            LogUtils.logd(TAG, "showAlertDialog:前");
        }

    }

    public void AppUpdate() {
        if (curVersionCode < netVersionCode) {

            if (!StringUtils.isEmpty(download)) {
                UIHelper.openDownLoadService(mContext, download, version);
            }

        } else {
            ToastUtils.show(R.string.is_the_latest_version);
        }
    }

    private void getNetVersionInfo() {
        NetHelper.getVersionFromNet("" + curVersionCode, mAppType, new AsyncCallBack() {
            @Override
            public void onSuccess(String result) {
                resolveVersionFromNetResponse(result);
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });

    }

    private void resolveVersionFromNetResponse(String responseBody) {
        try {
            JSONObject result = JSONUtil.resolveResult(responseBody);
            LogUtils.logd(TAG, "result:" + result.toString());
            if (result != null) {
                version = result.optString("version");
                download = result.optString("download");
                content = result.optString("content");
                if (!StringUtils.isEmpty(version)) {
                    netVersionCode = Double.parseDouble(version);
                    LogUtils.logd(TAG, "netVersionCode:" + netVersionCode);
                } else {
                    netVersionCode = 1;
                }

                if (isShow) {
                    checkUpdate();
                } else {
                    AppUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlertDialog(Context context) {

        UpdateDialog.Builder builder = new UpdateDialog.Builder(context);
        builder.setMessage(context.getString(R.string.no_immediate_update));
        builder.setTitle(context.getString(R.string.check_for_updates));
        builder.setPositiveButton(context.getString(R.string.later_on),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 设置你的操作事项
                    }
                });

        builder.setNegativeButton(context.getString(R.string.now_updates),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtils.logd(TAG, "download:" + download);
                        LogUtils.logd(TAG, "version:" + version);
                        if (!StringUtils.isEmpty(download)) {
                            UIHelper.openDownLoadService(mContext, download,
                                    version);
                        }
                        dialog.dismiss();
                    }
                });

        builder.create().show();

    }

}
