package com.renniji.mytask.net;
/**
 * 配置URL
 * @author heshicaihao
 *
 */
public class ConfigURL {
	
	public static final String URL = getUrl();
	public static final String TEST_URL = "http://120.76.21.33/";
	public static final String ONLINE_URL = "http://120.76.21.33/";
	
	private static String getUrl() {
		if (ReadPropertyFile.isTestEnvironment()) {
			return TEST_URL;
		}
		return ONLINE_URL;
	}

}
