package cn.com.boomhope.common.util;

import com.google.gson.Gson;

public class GsonUtil {
	private static Gson gson = new Gson();
	
	public static String toJson(Object obj)
	{
		return gson.toJson(obj);
	}
	
	@SuppressWarnings("unchecked")
	public static <T>T fromJson(String json,Class<?> clz)
	{
		return (T) gson.fromJson(json, clz);
	}
}
