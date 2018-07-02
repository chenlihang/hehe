package cn.com.boomhope.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * zookeeper工具
 * @author Administrator
 *
 */
public class ZooKeeperUtil {
	
	private static Logger log = Logger.getLogger(ZooKeeperUtil.class);
	
	public static ZooKeeper connZookeeper(String serverList,int sessionTimeout) throws Exception
	{
		final CountDownLatch countDown = new CountDownLatch(1);
		ZooKeeper zookeeper = new ZooKeeper(serverList, sessionTimeout, new Watcher(){
			@Override
			public void process(WatchedEvent event) {
				countDown.countDown();
			}
		});
		countDown.await();
		return zookeeper;
	}
	
	/**
	 * 
	 * @param zk
	 * @param path
	 * @param data
	 * @return
	 */
	public static void createIfNotPersistentNode(ZooKeeper zk,String path) throws Exception
	{
		Stat stat = zk.exists(path, false);
		if(stat==null)
		{
			zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
	}
	
	/**
	 * 
	 * @param zk
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String createTempSeqNode(ZooKeeper zk,String path, byte[] data) throws Exception
	{
		String zkpath = zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		return zkpath;
	}
	
	/**
	 * 获取指定永久节点下的所有子节点数据
	 * @param zk
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static List<String> getChildData(ZooKeeper zk,String path) throws Exception
	{
		List<String> nodeList = zk.getChildren(path, null);
		if(nodeList==null||nodeList.size()==0)
		{
			return null;
		}
		List<String> dataList = new ArrayList<String>(nodeList.size());
		for(String nodeUrl:nodeList)
		{
			byte[] dataArr = zk.getData(path.concat("/").concat(nodeUrl), false, null);
			dataList.add(new String(dataArr));
		}
		return dataList;
	}
	
	/**
	 * 创建临时节点
	 *  @param zk
	 *  @param path
	 *  @param data
	 *  @return 返回true标示创建成功，返回false表示创建失败
	 * */
	public static boolean createTmpNode2(ZooKeeper zk, String path, byte[] data){
		try {
			int sIdx = 1;
			int eIdx = -1;
			String tmpZkPath = new String(path);
			while ((eIdx = tmpZkPath.indexOf("/", sIdx)) > 0){
				String tmp = tmpZkPath.substring(0, eIdx);
				Stat stat = zk.exists(tmp, false);
				if (stat == null) {
					zk.create(tmp, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
				sIdx = eIdx + 1;
			}
			Stat stat = zk.exists(tmpZkPath, false);
			if (stat == null) {
				String road = zk.create(tmpZkPath, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				log.debug("分布式临时节点："+road);
			}
			return true;
		} catch (Exception e) {
			log.error("添加分布式临时节点异常", e);
		}
		return false;
	}
	
	/**
	 * 创建临时节点
	 *  @param zk
	 *  @param path
	 *  @param data
	 *  @return 返回true标示创建成功，返回false表示创建失败
	 * */
	public static boolean createTmpNode(ZooKeeper zk, String path, byte[] data){
		try {
			int sIdx = 1;
			int eIdx = -1;
			String tmpZkPath = new String(path).concat("/");
			while ((eIdx = tmpZkPath.indexOf("/", sIdx)) > 0){
				String tmp = tmpZkPath.substring(0, eIdx);
				if(eIdx == 0){
					zk.create(tmp, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				}else{
					zk.create(tmp, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				}
				sIdx = eIdx + 1;
			}
			return true;
		} catch (Exception e) {
			log.error(e);
		}
		
		return false;
	}
	
	/**
	 * 检查节点是否存在
	 * @param zk
	 * @param path
	 * @return 返回true表示节点存在，返回false标示节点不存在
	 * */
	public static boolean checkNodeExists(ZooKeeper zk, String path){
		try {
			if(zk.exists(path, null) != null){
				return true;
			}
		} catch (Exception e) {
			log.error("判断节点是否存在异常", e);
		}
		return false;
	}
	
	/**
	 * 获得节点数据
	 * @param zk
	 * @param path
	 * @return 返回节点数据
	 * */
	public static byte[] getNodeData(ZooKeeper zk, String path){
		try {
			return zk.getData(path, false, null);
		} catch (Exception e) {
			log.error("取节点数据异常", e);
		}
		return null;
	}
	
	/**
	 * 关闭zk
	 * @param zk
	 */
	public static void close(ZooKeeper zk){
		if (zk != null) {
			try {
				zk.close();
			} catch (Exception e) {
				log.error("关闭ZK异常", e);
			}
		}
	}
}
