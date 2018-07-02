package cn.com.boomhope.common.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class PropertiesUtil
{
	protected static Logger log = Logger.getLogger(PropertiesUtil.class);
	private static Hashtable<String, Properties> pptContainer = new Hashtable<String, Properties>();

	/**
	 * 获取某个properties文件的所有key
	 * 
	 * @param propertyFilePath
	 * @param isAbsolutePath
	 * @return
	 */
	public final static String[] getAllKey(String propertyFilePath, boolean isAbsolutePath)
	{
		Properties ppts = null;
		if (isAbsolutePath)
		{
			ppts = getPropertiesByFs(propertyFilePath);
		}
		if (ppts == null)
		{
			ppts = getProperties(propertyFilePath);
		}
		if (ppts == null)
		{
			return null;
		}
		return ppts.keySet().toArray(new String[] {});
	}

	/**
	 * 获取properties的值
	 * 
	 * @param propertyFilePath
	 * @param key
	 * @param isAbsolutePath
	 *            传入的文件地址是否为全路径
	 * @return
	 */
	public final static String getValue(String propertyFilePath, String key, boolean isAbsolutePath)
	{
		if (isAbsolutePath)
		{
			Properties ppts = getPropertiesByFs(propertyFilePath);
			return ppts == null ? null : ppts.getProperty(key);
		}
		return getValue(propertyFilePath, key);
	}

	public final static String getValue(String propertyFilePath, String key)
	{
		Properties ppts = getProperties(propertyFilePath);
		return ppts == null ? null : ppts.getProperty(key);
	}

	public final static Properties getProperties(String propertyFilePath)
	{
		if (propertyFilePath == null)
		{
			return null;
		}
		Properties ppts = pptContainer.get(propertyFilePath);
		if (ppts == null)
		{
			ppts = loadPropertyFile(propertyFilePath);
			if (ppts == null)
			{
				ppts = loadPropertyFileByClassPathResource(propertyFilePath);
			}
			if (ppts != null)
			{
				pptContainer.put(propertyFilePath, ppts);
			}
		}
		return ppts;
	}

	public final static Properties getPropertiesByFs(String propertyFilePath)
	{
		if (propertyFilePath == null)
		{
			return null;
		}
		Properties ppts = pptContainer.get(propertyFilePath);
		if (ppts == null)
		{
			ppts = loadPropertyFileByFileSystem(propertyFilePath);
			if (ppts == null)
			{
				loadPropertyFileByClassPathResource(propertyFilePath);
			}
			if (ppts != null)
			{
				pptContainer.put(propertyFilePath, ppts);
			}
		}
		return ppts;
	}

	private static Properties loadPropertyFileByClassPathResource(String propertyFilePath)
	{
		if (propertyFilePath == null)
		{
			return null;
		}
		Resource resource = new ClassPathResource(propertyFilePath);
		Properties properties = new Properties();
		try
		{
			properties.load(resource.getInputStream());
			return properties;
		}
		catch (IOException e)
		{
			return null;
		}
	}

	private static Properties loadPropertyFile(String propertyFilePath)
	{
		java.io.InputStream is = PropertiesUtil.class.getResourceAsStream(propertyFilePath);
		if (is == null)
		{
			return loadPropertyFileByFileSystem(propertyFilePath);
		}
		Properties ppts = new Properties();
		try
		{
			ppts.load(is);
			return ppts;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	private static Properties loadPropertyFileByFileSystem(final String propertyFilePath)
	{
		try
		{
			Properties ppts = new Properties();
			ppts.load(new java.io.FileInputStream(propertyFilePath));
			return ppts;
		}
		catch (java.io.FileNotFoundException e)
		{
			return null;
		}
		catch (java.io.IOException e)
		{
			return null;
		}
	}

	public final static boolean setValueAndStore(String propertyFilePath, java.util.Hashtable<String, String> htKeyValue)
	{
		return setValueAndStore(propertyFilePath, htKeyValue, null);
	}

	public final static boolean setValueAndStore(String propertyFilePath, java.util.Hashtable<String, String> htKeyValue, String storeMsg)
	{
		Properties ppts = getProperties(propertyFilePath);
		if (ppts == null || htKeyValue == null)
		{
			return false;
		}
		ppts.putAll(htKeyValue);
		java.io.OutputStream stream = null;
		try
		{
			stream = new java.io.FileOutputStream(propertyFilePath);
		}
		catch (FileNotFoundException e)
		{
			String path = PropertiesUtil.class.getResource(propertyFilePath).getPath();
			try
			{
				stream = new java.io.FileOutputStream(path);
			}
			catch (FileNotFoundException e1)
			{
				return false;
			}
		}
		try
		{
			ppts.store(stream, storeMsg != null ? storeMsg : "set value and store.");
			return true;
		}
		catch (java.io.IOException e)
		{
			log.error(e);
			return false;
		}
		finally
		{
			if (stream != null)
			{
				try
				{
					stream.close();
				}
				catch (IOException e)
				{
					log.error(e);
				}
			}
		}
	}

	/**
	 * 
	 * 创建properties文件
	 * 
	 * @param propertyFilePath
	 *            文件路径
	 * @param htKeyValue
	 *            键值对
	 * @return
	 * @since wapportal_manager version(2.0)
	 */
	public final static boolean createPropertiesFile(String propertyFilePath, java.util.Hashtable<String, String> htKeyValue)
	{
		java.io.File file = new java.io.File(propertyFilePath);
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (java.io.IOException e)
			{
				log.error(e);
			}
		}
		return setValueAndStore(propertyFilePath, htKeyValue, "create properties file:" + file.getName());
	}

	public final static boolean setValue(String propertyFilePath, String key, String value)
	{
		Properties ppts = getProperties(propertyFilePath);
		if (ppts == null)
		{
			return false;
		}
		ppts.put(key, value);
		return true;
	}

	public final static void store(Properties properties, String propertyFilePath, String msg)
	{
		try
		{
			java.io.OutputStream stream = new java.io.FileOutputStream(propertyFilePath);
			properties.store(stream, msg);
		}
		catch (java.io.FileNotFoundException e)
		{
			log.error(e);
		}
		catch (java.io.IOException e)
		{
			log.error(e);
		}
	}

	public final static String removeValue(String propertyFilePath, String key)
	{
		Properties ppts = getProperties(propertyFilePath);
		if (ppts == null)
		{
			return null;
		}
		return (String) ppts.remove(key);
	}

	public final static Properties removeValue(String propertyFilePath, String[] key)
	{
		if (key == null)
		{
			return null;
		}
		Properties ppts = getProperties(propertyFilePath);
		if (ppts == null)
		{
			return null;
		}
		for (String strKey : key)
		{
			ppts.remove(strKey);
		}
		return ppts;
	}

	public final static boolean removeValueAndStore(String propertyFilePath, String[] key)
	{
		Properties ppts = removeValue(propertyFilePath, key);
		if (ppts == null)
		{
			return false;
		}
		store(ppts, propertyFilePath, "batch remove key value!");
		return true;
	}

	public final static boolean updateValue(String propertyFilePath, String key, String newValue)
	{
		if (key == null || newValue == null)
		{
			return false;
		}
		java.util.Hashtable<String, String> ht = new java.util.Hashtable<String, String>();
		ht.put(key, newValue);
		return setValueAndStore(propertyFilePath, ht, "update " + key + "'s value!");
	}

	public final static boolean batchUpdateValue(String propertyFilePath, java.util.Hashtable<String, String> htKeyValue)
	{
		if (propertyFilePath == null || htKeyValue == null)
		{
			return false;
		}
		return setValueAndStore(propertyFilePath, htKeyValue, "batch update key value!");
	}

	public final static Properties removePropertyFile(String propertyFilePath)
	{
		return pptContainer.remove(propertyFilePath);
	}

	public final static void reloadPropertyFile(String propertyFilePath)
	{
		pptContainer.remove(propertyFilePath);
		loadPropertyFile(propertyFilePath);
	}

	public final static String getPpropertyFilePath(String pkg, String propertyFileName)
	{
		pkg = pkg == null ? "" : pkg.replaceAll("\\.", "/");
		propertyFileName = propertyFileName.endsWith(".properties") ? propertyFileName : (propertyFileName + ".properties");
		return "/" + pkg + "/" + propertyFileName;
	}
}