package cn.com.boomhope.common.web.security;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;

/**
 * 访问决策管理
 * 
 * @author 郑铭生
 *
 */
public class AccessDecisionManager implements org.springframework.security.access.AccessDecisionManager
{
	private final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private IAuthService authService;

	/**
	 * 核心方法，判断用户是否有对某个资源的访问权限
	 */
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) throws AccessDeniedException, InsufficientAuthenticationException
	{
		FilterInvocation invocation = (FilterInvocation) object;
		String url = invocation.getRequestUrl();
		if (authentication == null)
		{
			throw new AccessDeniedException("未登陆用户");
		}
		if (attributes == null || attributes.size() == 0)
		{
			return;
		}
		UserInfo userInfo = null;
		try
		{
			userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getDetails();
			Map<String, Set<String>> securityMap = authService.getSecurityMap();
			if (securityMap == null || securityMap.isEmpty())
			{
				return;
			}
			Set<String> resSet = securityMap.get(url);
			if (resSet == null)
			{
				return;
			}
			Collection<GrantedAuthority> grantedAuthorities = userInfo.getAuthorities();
			Iterator<GrantedAuthority> it = grantedAuthorities.iterator();
			while (it.hasNext())
			{
				GrantedAuthority authority = it.next();
				String auth = authority.getAuthority();
				// 权限代码
				if (resSet.contains(auth))
				{
					return;
				}
			}
		}
		catch (Exception e)
		{
			throw new AccessDeniedException("读取登陆用户信息异常");
		}
		logger.error(String.format("用户:%s,试图访问url:%s", userInfo.getUsername(), url));
		throw new AccessDeniedException("访问受限");
	}

	@Override
	public boolean supports(ConfigAttribute attribute)
	{
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return true;
	}
}
