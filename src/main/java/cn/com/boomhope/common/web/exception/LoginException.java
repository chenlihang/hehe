package cn.com.boomhope.common.web.exception;

/**
 * 登陆异常
 * @author 郑铭生
 *
 */
public class LoginException extends ServiceException
{
	private static final int EXCODE = 1;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LoginException(String message)
	{
		super(EXCODE, message);
	}
}
