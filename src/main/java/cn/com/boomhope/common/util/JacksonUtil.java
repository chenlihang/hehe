package cn.com.boomhope.common.util;

import org.codehaus.jackson.map.ObjectMapper;

public class JacksonUtil {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	public static <T> T jsonToObject(String json, Class<T> clazz) throws Exception{
		return mapper.readValue(json, clazz);
	}
}
