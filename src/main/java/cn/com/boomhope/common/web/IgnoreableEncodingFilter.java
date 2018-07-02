package cn.com.boomhope.common.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 
 * @author 郑铭生
 *
 */
public class IgnoreableEncodingFilter extends CharacterEncodingFilter
{
	private String ignoreURL;
	private List<String> ignoreList;
	private final Object ignoreMonitor = new Object();
	private String matchStrategyName;
	private MatchStrategy matchStrategy;

	@Override
	protected void initFilterBean() throws ServletException
	{
		initIgnoreList();
		initMatchStrategy();
	}

	/**
	 * 初始化忽略列表
	 */
	private void initIgnoreList()
	{
		synchronized (this.ignoreMonitor)
		{
			if (this.ignoreURL != null && this.ignoreURL.trim().length() > 0)
			{
				String[] urls = StringUtils.tokenizeToStringArray(this.ignoreURL, ",");
				if (urls != null && urls.length > 0)
				{
					ignoreList = new ArrayList<String>();
					for (String url : urls)
					{
						ignoreList.add(url);
					}
				}
			}
		}
	}

	/**
	 * 初始化忽略模式
	 */
	private void initMatchStrategy()
	{
		if (matchStrategyName == null || matchStrategyName.trim().length() == 0)
		{
			// 默认用全路径匹配
			matchStrategy = new EqualMatchStrategy();
		}
		else
		{
			if ("equal".equals(matchStrategyName))
			{
				matchStrategy = new EqualMatchStrategy();
			}
			else if ("pattern".equals(matchStrategyName))
			{
				matchStrategy = new PatternMatchStrategy();
			}
			else
			{
				matchStrategy = new EqualMatchStrategy();
			}
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
	{
		if (matchStrategy == null)
		{
			return false;
		}
		return matchStrategy.match(request);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
	{
		super.doFilterInternal(request, response, filterChain);
	}

	public String getIgnoreURL()
	{
		return ignoreURL;
	}

	public void setIgnoreURL(String ignoreURL)
	{
		this.ignoreURL = ignoreURL;
	}

	public String getMatchStrategyName()
	{
		return matchStrategyName;
	}

	public void setMatchStrategyName(String matchStrategyName)
	{
		this.matchStrategyName = matchStrategyName;
	}

	/**
	 * url匹配策略
	 * 
	 * @author 郑铭生
	 *
	 */
	abstract class MatchStrategy
	{
		public boolean match(HttpServletRequest request)
		{
			String requestUrl = getRequestPath(request);
			if (ignoreList != null && ignoreList.size() > 0)
			{
				for (String url : ignoreList)
				{
					if (doMatch(requestUrl, url))
					{
						return true;
					}
				}
			}
			return false;
		}

		/**
		 * 是否匹配
		 * 
		 * @param request
		 * @return
		 */
		public abstract boolean doMatch(String requestUrl, String url);

		protected String getRequestPath(HttpServletRequest request)
		{
			String context = request.getContextPath();
			String requestPath = request.getRequestURI();
			String result = requestPath.substring(context.length());
			return result;
		}
	}

	/**
	 * 路径全匹配
	 * 
	 * @author 郑铭生
	 *
	 */
	class EqualMatchStrategy extends MatchStrategy
	{
		@Override
		public boolean doMatch(String requestUrl, String url)
		{
			return requestUrl.equals(url);
		}
	}

	/**
	 * 正则表达式匹配
	 * 
	 * @author 郑铭生
	 *
	 */
	class PatternMatchStrategy extends MatchStrategy
	{
		@Override
		public boolean doMatch(String requestUrl, String pattern)
		{
			return requestUrl.matches(pattern);
		}
	}
}
