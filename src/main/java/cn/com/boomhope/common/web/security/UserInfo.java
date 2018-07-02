package cn.com.boomhope.common.web.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserInfo extends User
{
	private static final long serialVersionUID = 1L;
	private String name;
	private String orgCode;
	private String orgName;
	private String loginTime;
	private String loginIp;

	public UserInfo(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities)
	{
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public UserInfo(String username, String password, Collection<? extends GrantedAuthority> authorities)
	{
		super(username, password, authorities);
	}

	public UserInfo(UserInfo userInfo, Collection<? extends GrantedAuthority> authorities)
	{
		super(userInfo.getUsername(), userInfo.getPassword(), userInfo.isEnabled(), userInfo.isAccountNonExpired(), userInfo.isCredentialsNonExpired(), userInfo.isAccountNonLocked(), authorities);
		setName(userInfo.getName());
		setOrgCode(userInfo.getOrgCode());
		setOrgName(userInfo.getOrgName());
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getOrgCode()
	{
		return orgCode;
	}

	public void setOrgCode(String orgCode)
	{
		this.orgCode = orgCode;
	}

	public String getOrgName()
	{
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

	public String getLoginTime()
	{
		return loginTime;
	}

	public void setLoginTime(String loginTime)
	{
		this.loginTime = loginTime;
	}

	public String getLoginIp()
	{
		return loginIp;
	}

	public void setLoginIp(String loginIp)
	{
		this.loginIp = loginIp;
	}
}
