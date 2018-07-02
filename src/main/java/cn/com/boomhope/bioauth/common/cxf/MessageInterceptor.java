package cn.com.boomhope.bioauth.common.cxf;

//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.StringWriter;
//import java.nio.charset.Charset;
//
//import org.apache.commons.io.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.log4j.Logger;

import cn.com.boomhope.common.util.SoapXmlUtil;


//import cn.com.bmsoft.base.common.util.SoapXmlUtil;
//import cn.com.boomhope.platform.webservice.WebServiceImpl;
@SuppressWarnings("rawtypes")
public class MessageInterceptor extends AbstractPhaseInterceptor
{
	public MessageInterceptor()
	{
		super(Phase.RECEIVE);
	}

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(MessageInterceptor.class);
	public static final String REQUESTSTART = "<request>";
	public static final String REQUESTEND = "</request>";
	private static final String TYPETEXT = "text/xml; charset=UTF-8";

	public void handleMessage(Message message)
	{
		try
		{
			if (message.containsKey(Message.CONTENT_TYPE))
			{
				Object ct = message.get(Message.CONTENT_TYPE);
				if (ct != null)
				{
					String type = ct.toString();
					if (TYPETEXT.equals(type))
					{
						handlerText(message);
					}
				}
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

	private void handlerText(Message message) throws IOException
	{
		StringWriter writer = new StringWriter();
		InputStream is = message.getContent(InputStream.class);
		IOUtils.copy(is, writer, "UTF-8");
		String content = writer.toString();
		// log.debug("[MessageInterceptor]:拦截报文:"+content);
		// 过滤掉非法字符
		String result = SoapXmlUtil.parseMessage(content);
		// log.debug("[MessageInterceptor]:处理报文:"+content);
		// message
		ByteArrayInputStream bais = new ByteArrayInputStream(result.getBytes(Charset.forName("UTF-8")));
		message.setContent(InputStream.class, bais);
	}
}
