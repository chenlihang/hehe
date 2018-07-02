package cn.com.boomhope.common.web.security;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

public class CustomAuthenticationManager extends ProviderManager
{

	public CustomAuthenticationManager(List<AuthenticationProvider> providers)
	{
		super(providers);
	}
}
