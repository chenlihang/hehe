package cn.com.boomhope.common.util;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

import cn.com.boomhope.common.util.ZooKeeperUtil;

public class RmiCheckStatUtils {
	
	private static Logger log = Logger.getLogger(RmiCheckStatUtils.class);
	
	//定时器执行锁
	public static final String FACE_LOCK_NODE = "/bioauth/lock/face/lock_checkstat";
	
	//是否获得同步锁，获得的话就由本机执行状态更新
	public static boolean obtainLock = false;
	
	/**
	 * 检测本机是否运行检测rmi接口任务定时任务
	 * 
	 * @return 返回true标示需要执行定时任务，返回false标示不执行定时任务
	 * */
	public static boolean checkRunRmiTask(ZooKeeper zk, String configIp){
		//String ipAddress = getIpAddresses();
		String ipAddress = configIp;
		//log.info("获取的IP为："+ipAddress);
		if(ipAddress == null){
			//无法运行定时任务
			log.error("无法获取到本机IP地址，无法执行定时任务");
			return false;
		}
		
		//节点不存在，创建节点，是临时节点
		if(ZooKeeperUtil.checkNodeExists(zk, FACE_LOCK_NODE)){
			String nodeData = new String(ZooKeeperUtil.getNodeData(zk, FACE_LOCK_NODE));
			log.debug("执行计算节点检测的机器IP为： " + nodeData);
			if(ipAddress.equals(nodeData)){
				return true;
			}
		}else{
			log.debug("分布式锁不存在，创建节点");
			//节点不存在，创建节点
			ZooKeeperUtil.createTmpNode2(zk, FACE_LOCK_NODE, ipAddress.getBytes());
		}
		return false;
	}
}
