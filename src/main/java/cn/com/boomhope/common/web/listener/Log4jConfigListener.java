package cn.com.boomhope.common.web.listener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.springframework.util.Log4jConfigurer;

/**
 * log4j初始化监听器，从系统环境变量中读取log文件的输出目录
 * 
 * @author 郑铭生
 */
public class Log4jConfigListener implements ServletContextListener
{
	public static final String LOG4JCONFIGLOCATION = "log4jConfigLocation";
	public static final String REFRESH_INTERVAL_PARAM = "log4jRefreshInterval";

	public Log4jConfigListener()
	{
	}

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		ServletContext servletContext = sce.getServletContext();
		
		String log4jlocation = servletContext.getInitParameter("log4jConfigLocation");
		
		if (log4jlocation == null || log4jlocation.trim().length() == 0) {
			servletContext.log("web.xml中日志配置【" + LOG4JCONFIGLOCATION + "】不存在");
			return;
		}
		
		if (log4jlocation != null)
		{
			try
			{
				
				//log4jlocation = ResourceUtils.getFile(log4jlocation).getPath();
				
				servletContext.log("初始化log4j日志 [" + log4jlocation + "]");
				String intervalString = servletContext.getInitParameter(REFRESH_INTERVAL_PARAM);
				if (intervalString != null)
				{
					try
					{
						long refreshInterval = Long.parseLong(intervalString);
						Log4jConfigurer.initLogging(log4jlocation, refreshInterval);
					}
					catch (NumberFormatException ex)
					{
						throw new IllegalArgumentException("Invalid 'log4jRefreshInterval' parameter: " + ex.getMessage());
					}
				}
				else
				{
					Log4jConfigurer.initLogging(log4jlocation);
				}
			}
			catch (Exception ex)
			{
				throw new IllegalArgumentException("Invalid 'log4jConfigLocation' parameter: " + ex.getMessage());
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		LogManager.shutdown();
	}
}
