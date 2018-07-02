package cn.com.boomhope.common.web.exception;

/**
 * 异常基本模型
 * 
 * @author 郑铭生
 *
 */
public class ExceptionModel
{
	private int exCode;
	private String exMsg;

	public ExceptionModel(ServiceException serviceException)
	{
		this.exCode = serviceException.getExCode();
		this.exMsg = serviceException.getMessage();
	}

	public ExceptionModel(int exCode, String exMsg)
	{
		this.exCode = exCode;
		this.exMsg = exMsg;
	}

	public int getExCode()
	{
		return exCode;
	}

	public void setExCode(int exCode)
	{
		this.exCode = exCode;
	}

	public String getExMsg()
	{
		return exMsg;
	}

	public void setExMsg(String exMsg)
	{
		this.exMsg = exMsg;
	}
}
