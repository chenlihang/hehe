/**
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
**/

package org.csource.fastdfs.test;

import org.csource.common.FileUtil;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/**
* client test
* @author Happy Fish / YuQing
* @version Version 1.18
*/
public class Test
{
	private Test()
	{
	}
	
	/**
	* entry point
	* @param args comand arguments
	*     <ul><li>args[0]: config filename</li></ul>
	*     <ul><li>args[1]: local filename to upload</li></ul>
	*/
  public static void main(String args[])
  {
//  	if (args.length < 2)
//  	{
//  		System.out.println("Error: Must have 2 parameters, one is config filename, "
//  		                 + "the other is the local filename to upload");
//  		return;
//  	}
  	
  	System.out.println("java.version=" + System.getProperty("java.version"));
  	  
//  	String conf_filename = args[0];
//  	String local_filename = args[1];
  	String conf_filename = "E:\\work_document\\dev_workspace\\bioauth\\test-fastdfs\\src\\main\\resources\\fdfs_client.conf";
  	String local_filename = "E:\\work_document\\dev_workspace\\bioauth\\test-fastdfs\\src\\main\\resources\\tcsy_online_19.jpg";
  	try
  	{
  		ClientGlobal.init(conf_filename);
  		System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
  		System.out.println("charset=" + ClientGlobal.g_charset);
  		//创建tracker和storage连接
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
        //设置文件属性
        NameValuePair[] metaList = new NameValuePair[3];
        metaList[0] = new NameValuePair("fileName", local_filename);
        metaList[1] = new NameValuePair("suffix", "jpg");
        metaList[2] = new NameValuePair("type", "100001");
        metaList[3] = new NameValuePair("group", "face_bioauth");
        //进行上传文件
//        String fileId = client.upload_file1(local_filename, null, metaList);
        String uploadResult[] = client.upload_file(metaList[3].toString(), FileUtil.getBytes(local_filename), metaList[1].toString(), metaList);
        if(uploadResult == null){
        	System.out.println("upload fail!!");
        	return ;
        }
        System.out.println("upload success.  group is: " + uploadResult[0]+"，file id is  " + uploadResult[1]);

        int i = 0;
        while (i++ < 10) {
            byte[] result = client.download_file1(uploadResult[1]);
            System.out.println(i + ", download result is: " + result.length);
        }
        
  		trackerServer.close();
  	}
  	catch(Exception ex)
  	{
  		ex.printStackTrace();
  	}
  }
}
