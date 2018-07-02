package cn.com.boomhope.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.PropertyPlaceholderHelper;

public class CustomPropertyConfigurer extends PropertyPlaceholderConfigurer {
	private static Map<String, String> properties = new HashMap<String, String>();

	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		// cache the properties
		PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(
				DEFAULT_PLACEHOLDER_PREFIX, DEFAULT_PLACEHOLDER_SUFFIX,
				DEFAULT_VALUE_SEPARATOR, false);
		for (Entry<Object, Object> entry : props.entrySet()) {
			String stringKey = String.valueOf(entry.getKey());
			String stringValue = String.valueOf(entry.getValue());
			stringValue = helper.replacePlaceholders(stringValue, props);
			properties.put(stringKey, stringValue);
		}
		super.processProperties(beanFactoryToProcess, props);
	}

	public static Map<String, String> getProperties() {
		return properties;
	}

	public static String getProperty(String key) {
		return properties.get(key);
	}
	
	public static <T> Object getProperty(String key, Class<T> t){
		String value = getProperty(key);
		if(t.getSimpleName().equals(String.class.getSimpleName()))
		{
			return value;
		}
		else if(t.getSimpleName().equals(Integer.class.getSimpleName()))
		{
			return value==null?0:Integer.valueOf(value);
		}
		else if(t.getSimpleName().equals(Boolean.class.getSimpleName()))
		{
			return value==null?Boolean.FALSE:Boolean.valueOf(value);
		}
		else if(t.getSimpleName().equals(Double.class.getSimpleName()))
		{
			return value==null?0d:Double.valueOf(value);
		}
		else if(t.getSimpleName().equals(Float.class.getSimpleName()))
		{
			return value==null?0l:Float.valueOf(value);
		}
		else
		{
			return  value;
		}
	}
}
