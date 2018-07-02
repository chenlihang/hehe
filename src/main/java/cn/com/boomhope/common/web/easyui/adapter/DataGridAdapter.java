package cn.com.boomhope.common.web.easyui.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cn.com.boomhope.common.web.easyui.view.Page;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 表格数据适配器,将查询的数据转换为可以由easyui展示的数据格式
 * 
 * @author Administrator
 *
 */
public class DataGridAdapter
{
	protected static Logger log = Logger.getLogger(DataGridAdapter.class);
	private static final String OPTION_DATEFORMAT = "dateFormat";
	private static final String OPTION_SERIALNULL = "serialNull";
	private Map<String, String> defaultConfig;
	private Gson defaultGson;

	public void init()
	{
		defaultConfig = new HashMap<String, String>(2);
		defaultConfig.put(OPTION_DATEFORMAT, "yyyy-MM-dd");
		defaultConfig.put(OPTION_SERIALNULL, "true");
		defaultGson = createGson(defaultConfig);
	}

	private Gson createGson(Map<String, String> defaultConfig)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		Iterator<Entry<String, String>> it = defaultConfig.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, String> optionEntry = it.next();
			String optionName = optionEntry.getKey();
			if (OPTION_DATEFORMAT.equals(optionName))
			{
				String dateFormat = optionEntry.getValue();
				gsonBuilder.setDateFormat(dateFormat);
			}
			else if (OPTION_SERIALNULL.equals(optionName))
			{
				String serialNullValue = optionEntry.getValue();
				boolean serialNull = true;
				try
				{
					serialNull = Boolean.parseBoolean(serialNullValue);
				}
				catch (Exception e)
				{
					log.error(String.format("初始化配置项%s出现异常但不影响使用,适配器默认将空值序列化", OPTION_SERIALNULL), e);
				}
				if (serialNull)
				{
					gsonBuilder.serializeNulls();
				}
			}
		}
		return gsonBuilder.create();
	}

	// 静态状态
	public String toJson(Page page)
	{
		return toJson(page, defaultGson);
	}

	public String toJson(Page page, Map<String, String> config)
	{
		Gson gson = createGson(config);
		return toJson(page, gson);
	}

	private String toJson(Page page, Gson gson)
	{
		return gson.toJson(page);
	}
}
