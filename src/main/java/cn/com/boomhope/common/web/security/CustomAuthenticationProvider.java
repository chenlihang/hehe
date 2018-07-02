package cn.com.boomhope.common.web.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import cn.com.boomhope.common.util.DateUtil;

/**
 * 仿照spring security包中的DaoAuthenticationProvider类编写了该类，用于做用户名密码校验
 * 
 * @author 郑铭生
 *
 */
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider
{
	@Autowired
	private CustomUserDetailsService userDetailsService;

	/**
	 * 附加的检查
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException
	{
		if (userDetails instanceof UserInfo)
		{
			UserInfo userInfo = (UserInfo) userDetails;
			userInfo.setLoginTime(DateUtil.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
			Object details = authentication.getDetails();
			if (details instanceof WebAuthenticationDetails)
			{
				WebAuthenticationDetails webDetails = (WebAuthenticationDetails) details;
				userInfo.setLoginIp(webDetails.getRemoteAddress());
			}
		}
	}

	// 这里的username对应的是OperInfo的operid
	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException
	{
		UserDetails loadedUser;
		try
		{
			loadedUser = this.getUserDetailsService().loadUserByUsername(username);
		}
		catch (UsernameNotFoundException notFound)
		{
			throw notFound;
		}
		catch (Exception repositoryProblem)
		{
			throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}
		if (loadedUser == null)
		{
			throw new AuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
		}
		
		// 进行判断系统是否登录错误次数超过5次
		Integer wrongNum = this.getUserDetailsService().getWrongNum(username);
		if (wrongNum >= 5)
		{
			logger.error("校验异常：同一天连续登录失败次数超过5次");
			throw new BadCredentialsException("校验异常：同一天连续登录失败次数超过5次");
		}
		
		
		// 此处校验密码
		if (authentication.getCredentials() == null)
		{
			logger.error("校验异常：密码为空");
			// 登录错误次数加1
			this.getUserDetailsService().addWrongNum(username);
			
			throw new BadCredentialsException("校验异常：密码为空");
		}
		String presentedPassword = authentication.getCredentials().toString();
		// 此处做MD5校验待实现
		if (!loadedUser.getPassword().equals(presentedPassword))
		{
			logger.error("校验异常： 密码错误");
			// 登录错误次数加1
			this.getUserDetailsService().addWrongNum(username);
						
			throw new BadCredentialsException("校验异常： 密码错误");
		}
		authentication.setDetails(loadedUser);
		
		// 登录成功，则进行错误次数清零
		this.getUserDetailsService().clearWrongNum(username);
		return loadedUser;
	}

	public CustomUserDetailsService getUserDetailsService()
	{
		return userDetailsService;
	}

	public void setUserDetailsService(CustomUserDetailsService userDetailsService)
	{
		this.userDetailsService = userDetailsService;
	}
}
