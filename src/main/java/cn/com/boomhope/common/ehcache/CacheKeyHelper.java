package cn.com.boomhope.common.ehcache;

public class CacheKeyHelper
{
	/**
	 * 用于1比1等有自己crup的缓存机制的缓存
	 */
	public final static String TMP_CACHE_NAME = "TMP_CACHE";
	public final static String TMP_CACHE = "tmpcache";

	/**
	 * 获得cache key 的方法，cache key 是Cache 中一个Element 的唯一标识 55 * cache key
	 * 包括包名+类名+方法名，如 com.co.cache.service.UserServiceImpl.getAllUser 56
	 */
	public static String getCacheKey(String targetName, String methodName, Object...arguments)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(targetName);
		if(methodName!=null&&methodName.trim().length()>0)
		{
			sb.append(".").append(methodName);
		}
		if ((arguments != null) && (arguments.length != 0))
		{
			for (int i = 0; i < arguments.length; i++)
			{
				sb.append(".").append(arguments[i]);
			}
		}
		return sb.toString();
	}
}
