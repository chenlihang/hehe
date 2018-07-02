package cn.com.boomhope.common.file;

public interface FileProcesser
{

	/**
	 * 保存文件
	 * @param type
	 * @param distributePath
	 * @param fileName
	 * @param content
	 * @return
	 *
	 * 年/月/日/hash/faceid.jpg<br>
	 * /bioauth/face/2016/09/30/3/2.jpg
	 */
	String saveFile(String type, String distributePath, String fileName, byte[] content);

	/**
	 * 保存文件
	 * @param type
	 * @param fileName
	 * @param content
	 * @return
	 */
	String saveFile(String type, String fileName, byte[] content);

	/**
	 * 保存文件
	 * @param type
	 * @param fileName
	 * @param content
	 * @return
	 */
	String saveFile(String type, String fileName, String content);

	/**
	 *保存日志图片
	 * @param type
	 * @param fileName
	 * @param content
	 * @return
	 */
	String saveLogFile(String type, String fileName, byte[] content);

	/**
	 * 保存文件
	 * @param type
	 * @param fileName
	 * @param content
	 * @return
	 */
	String copyFile(String type, String fileName, String newType, String newFileName);

	/**
	 * copy照片到日志目录
	 * @param type
	 * @param fileName
	 * @param content
	 * @return
	 */
	String copyToLogFile(String type, String fileName, String newType, String newFileName);

	/**
	 * 删除文件
	 * @param fileVo
	 * @return
	 */
	boolean removeFile(String type, String fileName);

	/**
	 * 更新文件
	 * @param fileVo
	 * @param bytes
	 * @return
	 */
	String updateFile(String type, String fileName, byte[] content);

	/**
	 * 原样更新文件
	 * @param fileVo
	 * @param bytes
	 * @return
	 */
	String updateFile(String type, String fileName, String content);

	/**
	 * 获取文件
	 * @param fileVo
	 * @return
	 */
	byte[] getFile(String type, String fileName);

	/**
	 * 获取文件字符
	 * @param fileTypeVeinsBiology
	 * @param veinspath
	 * @return
	 */
	String getFileStr(String fileTypeVeinsBiology, String veinspath);

	/**
	 * 保存按照时间分目录的图片
	 * @param fileKey
	 * @param type
	 * @param count
	 * @param base64
	 * @return
	 */
	public String saveDatePathFile(String fileKey, String type, int count, String base64);

	/**
	 *	保存按照时间分目录的图片
	 * @param fileKey
	 * @param type
	 * @param count
	 * @param bytes
	 * @return
	 */
	public String saveDatePathFile(String fileKey, String type, int count, byte[] bytes);
}
