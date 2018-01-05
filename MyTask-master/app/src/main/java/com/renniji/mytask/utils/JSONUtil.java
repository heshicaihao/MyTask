package com.renniji.mytask.utils;

import org.json.JSONException;
import org.json.JSONObject;


public class JSONUtil {

	/**
	 *解析请求成功后的结果JSONObject
	 * @param json
	 * @return
	 * @throws JSONException
     */
	public static JSONObject resolveResult(String json) throws JSONException {
		JSONObject JSONObject = new JSONObject(json);
		JSONObject result = JSONObject.optJSONObject("result");
		return result;
	}


}
