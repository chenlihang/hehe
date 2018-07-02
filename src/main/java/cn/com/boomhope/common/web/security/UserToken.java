package cn.com.boomhope.common.web.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * 用户令牌，扩展时使用
 * @author 郑铭生
 *
 */
public class UserToken extends UsernamePasswordAuthenticationToken
{
	private static final long serialVersionUID = 1L;

	public UserToken(String username, String password)
	{
		super(username, password);
	}
}
