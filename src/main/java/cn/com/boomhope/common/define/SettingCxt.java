package cn.com.boomhope.common.define;

import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import cn.com.boomhope.common.util.PropertiesUtil;

/**
 * 配置文件读取工具类
 * @author 郑铭生
 *
 */
public class SettingCxt
{
	protected static Logger log = Logger.getLogger(SettingCxt.class);
	private static SettingCxt instance;
	private static Properties properties;
	private static Lock lock = new ReentrantLock();

	/** 单例模式 */
	private SettingCxt()
	{
	}

	public static void init(String filePath)
	{
		if (instance == null)
		{
			lock.lock();
			try
			{
				properties = PropertiesUtil.getProperties(filePath);
			}
			catch (Exception e)
			{
				log.error("读取配置文件失败", e);
			}
			finally
			{
				lock.unlock();
			}
		}
	}

	public static String getProperties(String key)
	{
		return properties.getProperty("project.url") + properties.getProperty(key);
	}
}
