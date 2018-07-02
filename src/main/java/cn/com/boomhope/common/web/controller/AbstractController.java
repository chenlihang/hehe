package cn.com.boomhope.common.web.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import cn.com.boomhope.common.exception.LogicException;
import cn.com.boomhope.common.web.exception.ExceptionModel;
import cn.com.boomhope.common.web.exception.LoginException;
import cn.com.boomhope.common.web.security.UserInfo;

public abstract class AbstractController
{
	protected static Logger log = Logger.getLogger(AbstractController.class);

	public ExceptionModel toExceptionModel(Exception e)
	{
		if (e instanceof LogicException || e instanceof LoginException)
		{
			return new ExceptionModel(-1 , e.getMessage());
		}
		else
		{
			return new ExceptionModel(-1, "系统异常，请联系管理员");
		}
	}

	public UserInfo getUserInfo()
	{
		return (UserInfo) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	public void removeCondition(Map<String, Object> condition)
	{
		if (condition != null && !condition.isEmpty())
		{
			condition.remove("total");
			condition.remove("sort");
			condition.remove("order");
			condition.remove("page");
			condition.remove("rows");
		}
	}
}
