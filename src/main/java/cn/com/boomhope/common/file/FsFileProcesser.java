package cn.com.boomhope.common.file;

import java.util.Map;

import org.csource.fastdfs.FastdfsClient;

import cn.com.boomhope.common.vo.FileVo;

public class FsFileProcesser extends BaseFileProcesser implements FileProcesser
{
	private static FastdfsClient client = null;

	public FsFileProcesser(Map<String, Object> settingCxt)
	{
		super(settingCxt);
		client = new FastdfsClient();
		String envkey = (String) settingCxt.get("envkey");
		String folderPath = (String) settingCxt.get("folderPath");
		client.init(envkey, folderPath);
	}

	@Override
	public String saveFile(String type, String fileName, byte[] content)
	{
		FileVo filePathVo = this.pathMap.get(type);
		if (filePathVo == null)
		{
			return null;
		}
		String retFileName = "";
		try {
	        //进行上传文件
	        String uploadResult[] = client.upload(filePathVo.getGroup(), fileName, content, filePathVo.getSuffix(), filePathVo.getType());
	        if(log.isDebugEnabled()){
		        if(uploadResult != null){
		        	log.debug("上传到nginx成功，对应的组是："+uploadResult[0]+"，对应的文件ID是："+uploadResult[1]);
		        }
	        }
	        retFileName = uploadResult[1];
		} catch (Exception e) {
			log.error(e);
		}
		return retFileName;
	}

	@Override
	public String saveFile(String type, String fileName, String content)
	{
		return saveFile(type, fileName, content.getBytes());
	}

	@Override
	public boolean removeFile(String type, String fileName)
	{
		boolean ok = false;
		FileVo filePathVo = this.pathMap.get(type);
		if (filePathVo == null)
		{
			return ok;
		}
		try
		{
			ok = client.delete(filePathVo.getGroup(), fileName);
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return ok;
	}

	@Override
	public String updateFile(String type, String fileName, byte[] content)
	{
		boolean dok = false;
		FileVo filePathVo = this.pathMap.get(type);
		if (filePathVo == null)
		{
			return null;
		}
		try
		{
			dok = client.delete(filePathVo.getGroup(), fileName);
			if(dok){
				String uploadResult[] = client.upload(filePathVo.getGroup(), fileName, content, filePathVo.getSuffix(), filePathVo.getType());
		        if(log.isDebugEnabled())
		        {
			        if(uploadResult != null)
			        {
			        	log.debug("上传到nginx成功，对应的组是："+uploadResult[0]+"，对应的文件ID是："+uploadResult[1]);
			        }
		        }
		        return uploadResult[1];
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	@Override
	public String updateFile(String type, String fileName, String content)
	{
		return updateFile(type, fileName, content.getBytes());
	}

	@Override
	public byte[] getFile(String type, String fileName)
	{
		FileVo filePathVo = this.pathMap.get(type);
		if (filePathVo == null)
		{
			return null;
		}
		try {
			return client.download(filePathVo.getGroup(), fileName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public String getFileStr(String type, String veinspath)
	{
		return new String(getFile(type, veinspath));
	}

	@Override
	public String saveFile(String type, String distributePath, String fileName,
			byte[] content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveLogFile(String type, String fileName, byte[] content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String copyToLogFile(String type, String fileName, String newType,
			String newFileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveDatePathFile(String fileKey, String type, int count,
			String base64) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveDatePathFile(String fileKey, String type, int count,
			byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}
}
