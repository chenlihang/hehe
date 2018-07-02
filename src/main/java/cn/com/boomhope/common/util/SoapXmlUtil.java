package cn.com.boomhope.common.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.springframework.web.util.HtmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SoapXmlUtil
{
	private static Logger log = Logger.getLogger(SoapXmlUtil.class);
	private static final String RS = "<request>";
	private static final String RE = "</request>";
//	private static SoapMessageHandler handler = new SoapMessageHandler();
	
//	private static ThreadLocal<SAXParser> docBuildeIns = new ThreadLocal<SAXParser>() {
//	    protected SAXParser initialValue() {
//	        try {
//	            return SAXParserFactory.newInstance().newSAXParser();
//	        } 
//	        catch (Exception e)
//			{
//				log.error("创建saxparser异常.",e);
//			}
//			return null;
//	    }
//	};

	public static String parseMessage(String message)
	{
		try
		{
			InputStream is = new ByteArrayInputStream(message.getBytes("UTF-8"));
			//docBuildeIns.get().parse(is, handler);
			SoapMessageHandler handler = new SoapMessageHandler();
			SAXParserFactory.newInstance().newSAXParser().parse(is, handler);
			String request = handler.getRequest();
			int s = message.indexOf(RS) + RS.length();
			int e = message.indexOf(RE);
			StringBuilder sb = new StringBuilder();
			sb.append(message.substring(0, s));
			sb.append(request);
			sb.append(message.substring(e));
			return sb.toString();
		}
		catch (Exception e)
		{
			log.error("parseMessage异常.",e);
		}
		return message;
	}
}

class SoapMessageHandler extends DefaultHandler
{
	private StringBuffer buff;
	private boolean found = false;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		if ("request".equals(qName))
		{
			found = true;
			buff = new StringBuffer();
		}
		else
		{
			found = false;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		found = false;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if (found)
		{
			buff.append(new String(ch, start, length));
		}
	}

	public String getRequest()
	{
		String request = buff.toString();
		request = HtmlUtils.htmlEscape(request);
		request = JavaScriptUtils.javaScriptEscape(request);
		return request;
	}
}
