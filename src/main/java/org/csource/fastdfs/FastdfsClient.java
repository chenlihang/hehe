package org.csource.fastdfs;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.csource.common.FileUtil;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;

import cn.com.boomhope.common.util.SysEnvUtil;

public class FastdfsClient implements IFastdfsClient{
	private static String config;
	private static Logger log = Logger.getLogger(FastdfsClient.class);
	
	public void init(String ENVKEY, String FOLDERPATH){
		if(config==null){
//			config = new File(this.getClass().getClassLoader().getResource("").getPath())+"/fdfs_client.conf";
//			config = new File(SysEnvUtil.getEnv(SettingCxt.ENVKEY))+File.separator+SettingCxt.FOLDERPATH+"/fdfs_client.conf";
			config = new File(SysEnvUtil.getEnv(ENVKEY))+File.separator+FOLDERPATH+"/fdfs_client.conf";
		}
		try {
			ClientGlobal.init(config);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}
	
	@Override
	public String[] upload(String group, String filename, byte[] content, String suffix, String type) {
		//创建tracker和storage连接
        TrackerClient tracker = null;
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
		try {
	        tracker = new TrackerClient();
	        trackerServer = tracker.getConnection();
	        storageServer = null;
	        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
	        
	        //设置文件属性
	        NameValuePair[] metaList = new NameValuePair[4];
	        metaList[0] = new NameValuePair("fileName", filename);
	        metaList[1] = new NameValuePair("suffix", suffix);
	        metaList[2] = new NameValuePair("type", type);
	        metaList[3] = new NameValuePair("group", group);
	        
	        //进行上传文件
	        String uploadResult[] = client.upload_file(group, content, suffix, metaList);
	        if(log.isDebugEnabled()){
		        if(uploadResult != null){
		  			String file_url = getFileUrl(trackerServer, uploadResult);
		        	log.debug("上传到nginx成功，对应的组是："+uploadResult[0]+"，对应的文件ID是："+uploadResult[1]+"，对应的访问url为："+file_url);
		        }
	        }
	        return uploadResult;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}finally{
			try {
				close(tracker, trackerServer, storageServer);
			} catch (Exception e) {
				log.error(e);
			}
		}
		return null;
	}

	@Override
	public boolean modify(String group, String filename, byte[] content, String suffix, String type) {
		//创建tracker和storage连接
        TrackerClient tracker = null;
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
		try {
	        tracker = new TrackerClient();
	        trackerServer = tracker.getConnection();
	        storageServer = null;
	        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
	        
	        //设置文件属性
	        NameValuePair[] metaList = new NameValuePair[4];
	        metaList[0] = new NameValuePair("fileName", filename);
	        metaList[1] = new NameValuePair("suffix", suffix);
	        metaList[2] = new NameValuePair("type", type);
	        metaList[3] = new NameValuePair("group", group);
	        
	        //进行更新文件
	        int result = client.modify_file(group, filename, 0, content, 0, 1024);
//	        int result = client.modify_file1(filename, 0, content, 0, 1024);
	        return result==0?true:false;
		} catch (Exception e) {
			log.error(e);
			System.out.println(e);
		}finally{
			try {
				close(tracker, trackerServer, storageServer);
			} catch (Exception e) {
				log.error(e);
			}
		}
		return false;
	}

	@Override
	public byte[] download(String group, String filename) {
		//创建tracker和storage连接
        TrackerClient tracker = null;
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
		try {
	        tracker = new TrackerClient();
	        trackerServer = tracker.getConnection();
	        storageServer = null;
	        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
	        
	        //进行下载文件
	        return client.download_file(group, filename);
		} catch (Exception e) {
			log.error(e);
		}finally{
			try {
				close(tracker, trackerServer, storageServer);
			} catch (Exception e) {
				log.error(e);
			}
		}
		return null;
	}
	
	@Override
	public boolean delete(String group, String filename) {
		//创建tracker和storage连接
        TrackerClient tracker = null;
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
		try {
	        tracker = new TrackerClient();
	        trackerServer = tracker.getConnection();
	        storageServer = null;
	        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
	        
	        //进行下载文件
	        int result = client.delete_file(group, filename);
	        return result==0?true:false;
		} catch (Exception e) {
			log.error(e);
		}finally{
			try {
				close(tracker, trackerServer, storageServer);
			} catch (Exception e) {
				log.error(e);
			}
		}
		return false;
	}

	private void close(TrackerClient tracker, TrackerServer trackerServer, StorageServer storageServer) throws Exception{
		if(storageServer!=null){
			storageServer.close();
		}
		if(trackerServer!=null){
			trackerServer.close();
		}
	}
	
	private String getFileUrl(TrackerServer trackerServer, String[] results)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			MyException {
		InetSocketAddress inetSockAddr;
		int ts;
		String token;
		
		String group_name = results[0];
		String remote_filename = results[1];
		String file_id = group_name + StorageClient1.SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR + remote_filename;
		
		inetSockAddr = trackerServer.getInetSocketAddress();
		String file_url = "http://" + inetSockAddr.getAddress().getHostAddress();
		if (ClientGlobal.g_tracker_http_port != 80)
		{
			 file_url += ":" + ClientGlobal.g_tracker_http_port;
		}
		file_url += "/" + file_id;
		if (ClientGlobal.g_anti_steal_token)
		{
			ts = (int)(System.currentTimeMillis() / 1000);
			token = ProtoCommon.getToken(file_id, ts, ClientGlobal.g_secret_key);
			file_url += "?token=" + token + "&ts=" + ts;
		}
		return file_url;
	}
	
	public static void main(String[] args) throws Exception {
		String filePath = "E:\\公司文档\\广建行\\1000_success\\100033_onspotphoto.jpg";
		FastdfsClient client = new FastdfsClient();
		String group = "facelogs";
		String filename = "test";
		String suffix = "jpg";
		String type = "100001";
		client.init(null, null);
		//上传
		byte[] content = FileUtil.getBytes(filePath);
		String[] uploadResult = client.upload(group, filename, content, suffix, type);
		if(uploadResult!=null){
        	System.out.println("上传到nginx成功，对应的组是："+uploadResult[0]+"，对应的文件ID是："+uploadResult[1]);
		}else{
			System.out.println("上传失败");
		}
//		//更新
//		filename = "M00/00/00/rBABoFXxI_iIQUb_ABFC14ZHcO4AAAAAQBryp8AEULv780.jpg";
//		boolean uOk = client.modify(group, filename, content, suffix, type);
//		if(uOk){
//			System.out.println("更新成功");
//		}else{
//			System.out.println("更新失败");
//		}
		//下载
//		byte[] bytes = client.download(group, filename);
//		if(bytes!=null){
//			System.out.println("下载成功");
//		}else{
//			System.out.println("下载失败");
//		}
		//删除
//		boolean dOk = client.delete(group, filename);
//		if(dOk){
//			System.out.println("删除成功");
//		}else{
//			System.out.println("删除失败");
//		}
	}
}
