package cn.com.boomhope.common.web.exception;

/**
 * 服务级别的异常
 * @author 郑铭生
 *
 */
public class ServiceException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 异常代码
	 */
	private int exCode;

	/**
	 * 构造系统异常
	 * @param exCode 异常代码
	 * @param message
	 */
	public ServiceException(int exCode, String message)
	{
		super(message);
	}

	public int getExCode()
	{
		return exCode;
	}

	public void setExCode(int exCode)
	{
		this.exCode = exCode;
	}
}
