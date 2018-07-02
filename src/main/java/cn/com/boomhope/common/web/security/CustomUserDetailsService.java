package cn.com.boomhope.common.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义用户服务
 * 
 * @author 郑铭生
 *
 */
@Service
@Lazy
public class CustomUserDetailsService implements UserDetailsService
{
	@Autowired
	private IAuthService authService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		UserInfo userDetails = authService.loadUser(username);
		if (userDetails == null)
		{
			throw new UsernameNotFoundException("用户不存在");
		}
		return userDetails;
	}
	
	/**
	 * 用户输入密码错误次数加1
	 * @param name
	 * @return
	 */
	public int addWrongNum(String operid)
	{
		return authService.addWrongNum(operid);
	}
	
	/**
	 * 用户输入密码错误次数清零
	 * @param name
	 * @return
	 */
	public int clearWrongNum(String operid)
	{
		return authService.clearWrongNum(operid);
	}
	
	/**
	 * 根据用户名获取客户一天的错误失败次数
	 * @param name
	 * @return
	 */
	public int getWrongNum(String operid)
	{
		return authService.getWrongNum(operid);
	}
}
