package cn.com.boomhope.bioauth.helper;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.springframework.util.Log4jConfigurer;

/**
 * 
 * @author 罗理俊
 */
public class Log4jHelper
{
	// static Logger logger = Logger.getLogger(Log4jHelper.class);
	/**
	 * Parameter specifying the refresh interval for checking the log4j config
	 * file
	 */
	public static final String REFRESH_INTERVAL_PARAM = "60000";
	/**
	 * Parameter specifying the output path of the log file while is define in
	 * system environment variables
	 */
	// private static final String LOG4JOUTPUTENVKEY = "bioauth_conf";
	/**
	 * Parameter specifying the config path of the log file while is define in
	 * system environment variables
	 */
	// private static final String LOG4JCONFIGENVKEY = "bioauth_conf";
	/** Parameter specifying the location of the log4j config file */
	// public static final String LOG4JFILENAME = "log4j.properties";
	/** 配置文件夹路径 **/
	// public static String OUTFOLDERPATH = "log"+File.separator+"rmi-log";
	// public static String CONFFOLDERPATH = "config"+File.separator+"rmi-conf";
	// private static final String LOG4JOUTPUT = "bioauth_log";

	public Log4jHelper()
	{
	}

	public static void init(Log4jSetting log4jSetting)
	{
		System.out.println("日志存放目录=" + log4jSetting.getLogPath());
		if (log4jSetting.getLogPath() == null || log4jSetting.getLogPath().trim().length() == 0)
		{
			System.out.println("没有规定日志存放目录" + log4jSetting.getLogPath() + ",log4j无法正常工作");
			return;
		}
		System.out.println("log4j配置指定变量=" + log4jSetting.getLogVariables());
		if (log4jSetting.getLogVariables() == null || log4jSetting.getLogVariables().trim().length() == 0)
		{
			System.out.println("没有配置log4j配置指定变量" + log4jSetting.getLogVariables() + ",log4j无法正常工作");
			return;
		}
		System.out.println("log4j配置文件=" + log4jSetting.getPropertiesPath());
		if (log4jSetting.getPropertiesPath() == null || log4jSetting.getPropertiesPath().trim().length() == 0)
		{
			System.out.println("没有配置log4j文件目录" + log4jSetting.getPropertiesPath() + ",log4j无法正常工作");
			return;
		}
		System.setProperty(log4jSetting.getLogVariables(), log4jSetting.getLogPath());
		String location = log4jSetting.getPropertiesPath();
		if (location != null)
		{
			// Perform actual log4j initialization; else rely on log4j's default
			// initialization.
			try
			{
				// Write log message to server log.
				System.out.println("Initializing log4j from [" + location + "]");
				// Check whether refresh interval was specified.
				String intervalString = REFRESH_INTERVAL_PARAM;
				if (intervalString != null)
				{
					// Initialize with refresh interval, i.e. with log4j's
					// watchdog thread,
					// checking the file in the background.
					try
					{
						long refreshInterval = Long.parseLong(intervalString);
						// PropertyConfigurator.configureAndWatch(location,
						// refreshInterval);
						Log4jConfigurer.initLogging(location, refreshInterval);
					}
					catch (NumberFormatException ex)
					{
						throw new IllegalArgumentException("Invalid 'log4jRefreshInterval' parameter: " + ex.getMessage());
					}
				}
				else
				{
					// Initialize without refresh check, i.e. without log4j's
					// watchdog thread.
					// PropertyConfigurator.configure(location);
					Log4jConfigurer.initLogging(location);
				}
			}
			catch (Exception ex)
			{
				throw new IllegalArgumentException("Invalid 'log4jConfigLocation' parameter: " + ex.getMessage());
			}
		}
	}
}
