package com.renniji.mytask.utils;

import java.util.UUID;

public class GUID {
	public static String getGUID() {
		String guid = UUID.randomUUID().toString();
		return guid;
	}

}
