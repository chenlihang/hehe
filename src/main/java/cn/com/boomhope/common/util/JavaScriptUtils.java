package cn.com.boomhope.common.util;


/**
 * 过滤特殊字符的工具类，来源于org.springframework.web.util.JavaScriptUtils,
 * 做了一些改变使得unicode不受影响
 * 
 * @author 郑铭生
 * 
 */
public class JavaScriptUtils
{
	public static String javaScriptEscape(String input)
	{
		if (input == null)
		{
			return input;
		}
		StringBuilder filtered = new StringBuilder(input.length());
		char prevChar = '\u0000';
		char c;
		char nextChar;
		for (int i = 0; i < input.length(); i++)
		{
			c = input.charAt(i);
			if (c == '"')
			{
				filtered.append("\\\"");
			}
			else if (c == '\'')
			{
				filtered.append("\\'");
			}
			else if (c == '\\')
			{
				int r = 0;
				if (i + 1 < input.length())
				{
					nextChar = input.charAt(i + 1);
					int pos = i + 2;
					switch (nextChar)
					{
						case 'u':
						{
							if (pos + 4 < input.length())
							{
								for (int j = pos, end = pos + 4; j < end; j++)
								{
									char tc = input.charAt(j);
									if (!((tc >= '0' && tc <= '9')||(tc >= 'a' && tc <= 'f')||(tc >= 'A' && tc <= 'F')))
									{
										r=1;
										break;
									}
								}
							}
							else
							{
								r=1;
							}
							break;
						}
						case '\t':
						case '\n':
						case '\r':
						case '\f':
						{
							r=2;
						}
						default:
						{
							r=0;
							break;
						}
					}
				}
				if(r==0)
				{
					filtered.append("\\");
				}
				else if(r==1)
				{
					filtered.append("\\\\");
				}
			}
			else if (c == '/')
			{
				filtered.append("\\/");
			}
			else if (c == '\t')
			{
				filtered.append("\\t");
			}
			else if (c == '\n')
			{
				if (prevChar != '\r')
				{
					filtered.append("\\n");
				}
			}
			else if (c == '\r')
			{
				filtered.append("\\n");
			}
			else if (c == '\f')
			{
				filtered.append("\\f");
			}
			else
			{
				filtered.append(c);
			}
			prevChar = c;
		}
		return filtered.toString();
	}
}
