package cn.com.boomhope.common.initializing;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Log4jConfigurer;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.ServletContextAware;

public class Log4jInitializing implements InitializingBean, ServletContextAware{
	
	@SuppressWarnings("unused")
	private ServletContext servletContext;
	private static final Long REFRESH_INTERVAL_PARAM = 60000L;
	private String logOutPath;
	private String logProperties;
	private String logOutPathKey;

	@Override
	public void setServletContext(ServletContext sct) {
		servletContext = sct;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String propertiesPath = ResourceUtils.getFile(logProperties).getPath();
		if (propertiesPath == null || propertiesPath.trim().length() == 0) {
			System.err.println("类路径中不存在log4j.properties配置文件,log4j无法正常工作");
			return;
		}
		System.out.println("log4j.properties配置文件路径为:"+propertiesPath);
		
		if (logOutPath == null || logOutPath.trim().length() == 0) {
			System.out.println("没有规定日志存放目录,log4j无法正常工作");
		}
		System.out.println("日志存放目录:" + logOutPath);
		
		if (logOutPathKey == null || logOutPathKey.trim().length() == 0) {
			System.out.println("没有规定日志存放目录的key,log4j无法正常工作");
		}
		System.out.println("日志存放目录key:" + logOutPathKey);
		
		System.setProperty(logOutPathKey, logOutPath);
		
		Log4jConfigurer.initLogging(propertiesPath, REFRESH_INTERVAL_PARAM);
	}

	public String getLogOutPath() {
		return logOutPath;
	}

	public void setLogOutPath(String logOutPath) {
		this.logOutPath = logOutPath;
	}

	public String getLogProperties() {
		return logProperties;
	}

	public void setLogProperties(String logProperties) {
		this.logProperties = logProperties;
	}

	public String getLogOutPathKey() {
		return logOutPathKey;
	}

	public void setLogOutPathKey(String logOutPathKey) {
		this.logOutPathKey = logOutPathKey;
	}


}
