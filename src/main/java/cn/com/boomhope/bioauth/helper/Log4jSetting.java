package cn.com.boomhope.bioauth.helper;

/**
 * log4j设置
 * 
 * @author 郑铭生
 *
 */
public class Log4jSetting
{
	private String propertiesPath;
	private String logPath;
	private String logVariables;

	public String getPropertiesPath()
	{
		return propertiesPath;
	}

	public void setPropertiesPath(String propertiesPath)
	{
		this.propertiesPath = propertiesPath;
	}

	public String getLogPath()
	{
		return logPath;
	}

	public void setLogPath(String logPath)
	{
		this.logPath = logPath;
	}

	public String getLogVariables()
	{
		return logVariables;
	}

	public void setLogVariables(String logVariables)
	{
		this.logVariables = logVariables;
	}
}
