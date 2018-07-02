package cn.com.boomhope.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanToMapUtil {
	
	// 将java bean转成map
	public static Map<String, Object> beanToMap(Object obj)
	{
		// 如果类为空，则返回空
		if (obj == null)
		{
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try 
		{
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			
			for (PropertyDescriptor property : propertyDescriptors)
			{
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class"))
				{
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);
					map.put(key, value);
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		// 最后将结果返回
		return map;
		
	}

}
