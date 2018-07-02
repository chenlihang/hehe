package cn.com.boomhope.common.vo;

/**
 * 系统信息
 * @author Administrator
 *
 */
public class SystemInfoBean {
	
	/** 操作系统 */
	private String osName;
	
	/** 总物理内存 */
	private long totalPhysicalMemorySize;
	
	/** 可用物理内存 */
	private long freePhysicalMemorySize;
	
	private double cpuRate;
	
	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public long getTotalPhysicalMemorySize() {
		return totalPhysicalMemorySize;
	}

	public void setTotalPhysicalMemorySize(long totalPhysicalMemorySize) {
		this.totalPhysicalMemorySize = totalPhysicalMemorySize;
	}

	public long getFreePhysicalMemorySize() {
		return freePhysicalMemorySize;
	}

	public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
		this.freePhysicalMemorySize = freePhysicalMemorySize;
	}

	public double getCpuRate() {
		return cpuRate;
	}

	public void setCpuRate(double cpuRate) {
		this.cpuRate = cpuRate;
	}
}
