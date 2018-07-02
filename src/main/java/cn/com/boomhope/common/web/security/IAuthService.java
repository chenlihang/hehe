package cn.com.boomhope.common.web.security;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.boomhope.common.vo.ResourceInfo;

/**
 * 用户权限接口
 * @author 郑铭生
 *
 */
public interface IAuthService
{
	/**
	 * 获取受控资源
	 * @return
	 */
	Map<String, Set<String>> getSecurityMap();

	/**
	 * 根据用户名获取用户信息
	 * @param username
	 * @return
	 */
	UserInfo loadUser(String username);

	/**
	 * 获取所有资源详情列表
	 * @return
	 */
	List<ResourceInfo> getResourceInfo();
	
	/**
	 * 用户输入密码错误次数加1
	 * @param name
	 * @return
	 */
	int addWrongNum(String operid);
	
	/**
	 * 用户输入密码错误次数清零
	 * @param name
	 * @return
	 */
	int clearWrongNum(String operid);
	
	/**
	 * 根据用户名获取客户一天的错误失败次数
	 * @param name
	 * @return
	 */
	int getWrongNum(String operid);
}
