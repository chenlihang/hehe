package cn.com.boomhope.common.web.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriUtils;

public class SecurityInterceptor extends AbstractSecurityInterceptor implements Filter
{
	private SecurityMetadataSource securityMetadataSource;
	private AuthenticationSuccessHandler successHandler;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private String loginFormUrl;
	private boolean useForward = false;
	private List<String> ingoredTargetUrls = new ArrayList<String>();

	@Override
	public Class<?> getSecureObjectClass()
	{
		return FilterInvocation.class;
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource()
	{
		return securityMetadataSource;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		FilterInvocation invocation = new FilterInvocation(request, response, chain);
		this.invoke(invocation);
	}

	private void invoke(FilterInvocation filterInvocation) throws IOException, ServletException
	{
		HttpServletRequest request = filterInvocation.getRequest();
		HttpServletResponse response = filterInvocation.getHttpResponse();
		// 获取访问路径
		ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(requestAttributes);
		// 获取用户权限
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
		{
			// 未登录用户-访问登录页面直接执行
			if (request.getRequestURI().equals(request.getContextPath() + this.loginFormUrl))
			{
				filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
				return;
			}
			// 未登录用户-访问其它页面自动跳转到登录页面
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			String jumpUrl = this.getTargetUrl(request);
			if (this.useForward)
			{
				RequestDispatcher dispatcher = request.getRequestDispatcher(jumpUrl);
				dispatcher.forward(request, response);
			}
			else
			{
				this.redirectStrategy.sendRedirect(request, response, jumpUrl);
			}
		}
		else
		{
			String requestUri = request.getRequestURI();
			String contextPath = request.getContextPath();
			// 已登录用户-访问登录页面，直接跳转至首页
			if (requestUri.equals(contextPath + this.loginFormUrl) || requestUri.equals(contextPath) || requestUri.equals(contextPath + "/"))
			{
				this.successHandler.onAuthenticationSuccess(request, response, authentication);
				return;
			}
			// 否则执行验证
			InterceptorStatusToken token = super.beforeInvocation(filterInvocation);
			try
			{
				filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
			}
			finally
			{
				super.afterInvocation(token, null);
			}
		}
	}

	private String getTargetUrl(HttpServletRequest request) throws UnsupportedEncodingException
	{
		if (!this.isIgnored(request.getRequestURI().replaceFirst(request.getContextPath(), "")))
		{
			String url = request.getRequestURL().toString() + (StringUtils.isNotBlank(request.getQueryString()) ? "?" + request.getQueryString() : "");
			return this.loginFormUrl + "?targetUrl=" + UriUtils.encodeHost(url, "UTF-8");
		}
		else
		{
			return this.loginFormUrl;
		}
	}

	private boolean isIgnored(String url)
	{
		for (String ingoredUrl : this.ingoredTargetUrls)
		{
			if (ingoredUrl.equals(url))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy()
	{
	}

	public SecurityMetadataSource getSecurityMetadataSource()
	{
		return securityMetadataSource;
	}

	public void setSecurityMetadataSource(SecurityMetadataSource securityMetadataSource)
	{
		this.securityMetadataSource = securityMetadataSource;
	}

	public AuthenticationSuccessHandler getSuccessHandler()
	{
		return successHandler;
	}

	public void setSuccessHandler(AuthenticationSuccessHandler successHandler)
	{
		this.successHandler = successHandler;
	}

	public String getLoginFormUrl()
	{
		return loginFormUrl;
	}

	public void setLoginFormUrl(String loginFormUrl)
	{
		this.loginFormUrl = loginFormUrl;
	}
}
