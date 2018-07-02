package cn.com.boomhope.common.web.listener;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import cn.com.boomhope.common.define.SettingCxt;
import cn.com.boomhope.common.util.SysEnvUtil;

/**
 * 初始化系统
 * 
 * @author 郑铭生
 *
 */
public class SystemInitListener implements ServletContextListener
{
	static Logger logger = Logger.getLogger(SystemInitListener.class);
	private static final String CONFIGENVKEY = "configEnvKey";
	private String configPath = "";
	/** 配置文件夹路径**/
	public static String FOLDERPATH = "config"+File.separator+"admin-conf";

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		ServletContext servletContext = sce.getServletContext();
		initSettingCxt(servletContext);
	}

	/**
	 * 加载配置文件
	 * 
	 * @param servletContext
	 */
	private void initSettingCxt(ServletContext servletContext)
	{
		String configEnvKey = servletContext.getInitParameter(CONFIGENVKEY);
		configPath = SysEnvUtil.getEnv(configEnvKey);
		if (logger.isDebugEnabled())
		{
			logger.debug("bioauth-admin config path：" + configPath + File.separator + FOLDERPATH );
		}
		SettingCxt.init(configPath + File.separator + FOLDERPATH + File.separator + "setting.properties");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
	}
}
