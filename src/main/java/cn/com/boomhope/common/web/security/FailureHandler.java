package cn.com.boomhope.common.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class FailureHandler implements AuthenticationFailureHandler
{
	private String defaultFailureUrl;
	private String forceResetPwdUrl;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException
	{
		// 如果密码过期（首次登陆强制改密码）
		if(exception instanceof CredentialsExpiredException)
		{
			redirectStrategy.sendRedirect(request, response, forceResetPwdUrl);
			return;
		}
		String msg = exception.getMessage();
        msg = (msg==null||msg.trim().length()==0)?"登录异常":msg;
        msg = java.net.URLEncoder.encode(msg, "UTF-8");
        String url = defaultFailureUrl+"?error="+msg;
        redirectStrategy.sendRedirect(request, response, url);
	}

	public String getDefaultFailureUrl()
	{
		return defaultFailureUrl;
	}

	public void setDefaultFailureUrl(String defaultFailureUrl)
	{
		this.defaultFailureUrl = defaultFailureUrl;
	}

	public String getForceResetPwdUrl() {
		return forceResetPwdUrl;
	}

	public void setForceResetPwdUrl(String forceResetPwdUrl) {
		this.forceResetPwdUrl = forceResetPwdUrl;
	}
}
