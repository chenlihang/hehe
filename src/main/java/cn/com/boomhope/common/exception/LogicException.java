package cn.com.boomhope.common.exception;

public class LogicException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -648368639246440328L;
	private String exCode;

	public LogicException(String exCode, String message)
	{
		super(message);
		this.exCode = exCode;
	}

	public String getExCode()
	{ 
		return exCode;
	}

	public void setExCode(String exCode)
	{
		this.exCode = exCode;
	}
	
	public LogicException(String message)
	{
		super(message);
	}
}
