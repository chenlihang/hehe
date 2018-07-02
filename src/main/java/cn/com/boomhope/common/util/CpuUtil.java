package cn.com.boomhope.common.util;

import org.apache.log4j.Logger;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;

import cn.com.boomhope.common.exception.LogicException;

public class CpuUtil {
	public static int CPU_NUMBER = 1;//cpu核数
	public static Sigar sigar = new Sigar();
	public static Logger log = Logger.getLogger(CpuUtil.class);
	
	static{
		try {
			if(CPU_NUMBER==1){
				CpuInfo infos[] = sigar.getCpuInfoList();
				if(infos == null){
					throw new LogicException("获取cpu核数失败");
				}
				if(infos.length == 0){
					throw new LogicException("获取cpu核数失败");
				}
				CPU_NUMBER =  infos.length;
			}
		} catch (Exception e) {
			log.error("获取cpu核数失败！！！！"+e);
		}
	}
}
