package cn.com.boomhope.common.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import cn.com.boomhope.common.vo.ResourceInfo;

/**
 * 资源元素接口
 * 
 * @author 郑铭生
 *
 */
public class SecurityInvokeMetadataSource implements FilterInvocationSecurityMetadataSource
{
	private final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private IAuthService authService;

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException
	{
		List<ConfigAttribute> list = new ArrayList<ConfigAttribute>();
		FilterInvocation filterInvocation = (FilterInvocation) object;
		String requestUrl = filterInvocation.getRequestUrl();
		ConfigAttribute attribute = new SecurityConfig(requestUrl);
		list.add(attribute);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes()
	{
		if (logger.isDebugEnabled())
		{
			this.logger.debug(this.getClass().getName() + ":getAllConfigAttributes");
		}
		List<ResourceInfo> resourceList = authService.getResourceInfo();
		if (resourceList != null && resourceList.size() > 0)
		{
			List<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>(resourceList.size());
			for (ResourceInfo resourceInfo : resourceList)
			{
				if(resourceInfo.getResUrl()!=null&&resourceInfo.getResUrl().trim().length()>0)
				{
					ConfigAttribute configAttribute = new SecurityConfig(resourceInfo.getResUrl());
					configAttributes.add(configAttribute);
				}
			}
			return configAttributes;
		}
		return ListUtils.EMPTY_LIST;
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return true;
	}
}
