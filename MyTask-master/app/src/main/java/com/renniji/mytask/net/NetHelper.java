package com.renniji.mytask.net;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by heshicaihao on 2017/2/9.
 */
public class NetHelper {
    private static final String TAG = "NetHelper";

    /**
     * @param version
     * @param app_type (String类型, 分别 "android", 或者是"ios")
     * @param callback
     */
    public static void getVersionFromNet(String version, String app_type,
                                         AsyncCallBack callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("version", version);//用户名
        params.put("app_type", app_type);
        AsyncHttp.postAsync(MyURL.GET_VERSION_URL, params, callback);

    }

    /**
     * 获取地址信息
     *
     * @param callback
     */
    public static void getAreaInfo(AsyncCallBack callback) {
        AsyncHttp.postAsync(MyURL.AREA_INFO_URL,callback);
    }

    /**
     * 测试 post 有参数
     *
     * @param callback
     */
    public static void login(String account_id, String account_pw, AsyncCallBack callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("account_id", account_id);//用户名
        params.put("account_pw", account_pw);
        AsyncHttp.postAsync(MyURL.GET_VERSION_URL, params, callback);
    }

}
