package cn.com.boomhope.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;

public class FormPostUtil
{
	protected static Logger log = Logger.getLogger(FormPostUtil.class);
	private static final String CHARSET = "GBK";

	public static String post(String requestUrl, List<FormItem> formItems) throws IOException
	{
		if (formItems == null || formItems.size() == 0)
		{
			System.out.println("参数列表为空");
			return "";
		}
		PostMethod postMethod = new PostMethod(requestUrl);
		Part[] parts = new Part[formItems.size()];
		for (int i = 0; i < formItems.size(); i++)
		{
			FormItem formItem = formItems.get(i);
			if (formItem.isFile())
			{
				PartSource partSource = new ByteArrayPartSource(formItem.getName(), (byte[]) formItem.getValue());
				FilePart filePart = new FilePart(formItem.getName(), partSource);
				filePart.setContentType(formItem.getContentType());
				parts[i] = filePart;
			}
			else
			{
				StringPart stringPart = new StringPart(formItem.getName(), formItem.getValue().toString(), CHARSET);
				parts[i] = stringPart;
			}
		}
		MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
		postMethod.setRequestEntity(mre);
		HttpClient client = new HttpClient();
		client.getParams().setHttpElementCharset(CHARSET);
		client.getParams().setContentCharset(CHARSET);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.getHttpConnectionManager().getParams().setSoTimeout(50000);
		int status = 0;
		try
		{
			status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK)
			{
				InputStream inputStream = null;
				try
				{
					inputStream = postMethod.getResponseBodyAsStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, CHARSET));
					StringBuffer stringBuffer = new StringBuffer();
					String str = "";
					while ((str = br.readLine()) != null)
					{
						stringBuffer.append(str).append("\r\n");
					}
					return stringBuffer.toString();
				}
				catch (Exception e)
				{
					throw e;
				}
				finally
				{
					if (inputStream != null)
					{
						try
						{
							inputStream.close();
						}
						catch (IOException e)
						{
						}
					}
				}
			}
			else
			{
				log.error(requestUrl + "post返回码" + status);
			}
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			if (client != null)
			{
				client.getHttpConnectionManager().closeIdleConnections(0);
			}
		}
		return null;
	}
}
