package cn.com.boomhope.extend;

import java.text.DateFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.FactoryBean;

public class JacksonObjectMapper implements FactoryBean<ObjectMapper> {
	
	private DateFormat dateFormat;

	@SuppressWarnings("deprecation")
	@Override
	public ObjectMapper getObject() throws Exception {
		// TODO Auto-generated method stub
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getDeserializationConfig().setDateFormat(dateFormat);
		objectMapper.getSerializationConfig().setDateFormat(dateFormat);
		return objectMapper;
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return ObjectMapper.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

}
