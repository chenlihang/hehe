package cn.com.boomhope.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import cn.com.boomhope.common.exception.LogicException;

public class RequestUtil
{
	@SuppressWarnings("rawtypes")
	public static Map<String, String> parse(HttpServletRequest request, boolean ignoreEmpty)
	{
		Map<String, String> params = new HashMap<String, String>();
		Entry entry;
		String key;
		Object val;
		Iterator iterator = request.getParameterMap().entrySet().iterator();
		while (iterator.hasNext())
		{
			entry = (Entry) iterator.next();
			key = (String) entry.getKey();
			val = entry.getValue();
			if (key == null || key.equalsIgnoreCase("_") || key.equalsIgnoreCase("idField") || key.equalsIgnoreCase("nameField"))
			{
				continue;
			}
			// if (ignoreEmpty && (val ==
			// null||"".equals(val.toString().trim()))) {
			//
			// }
			if (val == null)
			{
				if (ignoreEmpty)
				{
					continue;
				}
				params.put(key, "");
			}
			else if (val instanceof String[])
			{
				StringBuffer buf = new StringBuffer();
				String[] vals = (String[]) val;
				for (int i = 0; i < vals.length; i++)
				{
					if (i > 0)
					{
						buf.append(",");
					}
					buf.append(vals[i]);
				}
				if (ignoreEmpty && (val == null || vals.length == 0 || "".equals(buf.toString().trim())))
				{
					continue;
				}
				params.put(key, buf.toString());
			}
			else
			{
				params.put(key, val.toString());
			}
		}
		return params;
	}

	public static Map<String, String> parse(HttpServletRequest request)
	{
		return parse(request, true);
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Object> asMap(HttpServletRequest request, boolean ignoreEmpty)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, String> _param = parse(request, ignoreEmpty);
		Entry entry;
		String key;
		String val;
		Iterator iterator = _param.entrySet().iterator();
		while (iterator.hasNext())
		{
			entry = (Entry) iterator.next();
			key = (String) entry.getKey();
			val = (String) entry.getValue();
			params.put(key, val);
		}
		return params;
	}

	public static Map<String, Object> asMap(HttpServletRequest request)
	{
		return asMap(request, true);
	}

	public static <T> T applyUpdate(T newObj, T oldObj) throws ServletException
	{
		try
		{
			BeanInfo info = Introspector.getBeanInfo(newObj.getClass());
			PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : descriptors)
			{
				Method read = descriptor.getReadMethod();
				if (read != null && read.invoke(newObj) == null)
				{
					Method write = descriptor.getWriteMethod();
					if (write != null)
					{
						write.invoke(newObj, read.invoke(oldObj));
					}
				}
			}
			return newObj;
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
	}

	/**
	 * 将数组元素转化为带逗号的值
	 * 
	 * @param <T>
	 * @param params
	 *            请求参数
	 * @param key
	 *            要获取的变量名
	 * @return
	 */
	public static List<String> eachToList(Map<String, Object> params, String key)
	{
		if (params.containsKey(key))
		{
			Object obj = params.get(key);
			String objVal = obj == null ? null : obj.toString();
			if (objVal != null && objVal.trim().length() > 0)
			{
				String[] vals = objVal.split(",");
				List<String> list = new ArrayList<String>(vals.length);
				for (String val : vals)
				{
					list.add(val);
				}
				return list;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getParameter(HttpServletRequest request, String key, Class<?> type, boolean nullable, int length) throws LogicException
	{
		String value = request.getParameter(key);
		if (value == null || value.length() == 0)
		{
			if (nullable)
			{
				return null;
			}
			throw new LogicException(key + "值为空");
		}
		else if (value.length() > length)
		{
			throw new LogicException(key + "值超过最大值" + length);
		}
		if (String.class.equals(type))
		{
			return (T) value;
		}
		else if (Integer.class.equals(type))
		{
			return (T) new Integer(value);
		}
		else if (Long.class.equals(type))
		{
			return (T) new Long(value);
		}
		else if (Float.class.equals(type))
		{
			return (T) new Float(value);
		}
		else if (Double.class.equals(type))
		{
			return (T) new Double(value);
		}
		else
		{
			throw new LogicException(RequestUtil.class.getName() + "中未找到对应的参数转换方式,请在getParameter方法中补充");
		}
	}
}
