package org.csource.fastdfs;

public interface IFastdfsClient {
	/**
	 * 上传文件
	 * @param group
	 * @param filename
	 * @param content
	 * @param suffix
	 * @param type
	 * @return
	 */
	public String[] upload(String group, String filename, byte[] content, String suffix, String type);
	
	/**
	 * 更新文件
	 * @param group
	 * @param filename
	 * @param content
	 * @param suffix
	 * @param type
	 * @return
	 */
	public boolean modify(String group, String filename, byte[] content, String suffix, String type);
	
	/**
	 * 下载文件
	 * @param group
	 * @param filename
	 * @param content
	 * @param suffix
	 * @param type
	 * @return
	 */
	public byte[] download(String group, String filename);
	
	/**
	 * 删除文件
	 * @param group
	 * @param filename
	 * @return
	 */
	public boolean delete(String group, String filename);
}
