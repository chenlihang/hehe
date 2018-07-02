package cn.com.boomhope.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.util.StringTokenizer;

import cn.com.boomhope.common.vo.SystemInfoBean;

import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class SystemInfoUtils {

	private static final int CPUTIME = 5000;  
    private static final int PERCENT = 100;  
    
	public static SystemInfoBean getSystemInfo() {
		SystemInfoBean infoBean = new SystemInfoBean();
		// 操作系统
		String osName = System.getProperty("os.name");
		infoBean.setOsName(osName);

		// 内存
		OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		infoBean.setTotalPhysicalMemorySize(osmb.getTotalPhysicalMemorySize());
		infoBean.setFreePhysicalMemorySize(osmb.getFreePhysicalMemorySize());
		// cpu
		if(osName.toLowerCase().startsWith("linux"))
		{
			String linuxVersion = getLinuxVersion();
			double cpuRate = getCpuRateForLinux(linuxVersion);
			infoBean.setCpuRate(cpuRate);
		}
		else if(osName.toLowerCase().startsWith("windows"))
		{
			try {
				System.out.println(String.format("windows下暂时不做监控, 类%s.java", SystemInfoUtils.class));
				Thread.sleep(5 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// @windows下暂时不做监控，增加于20161224
//			double cpuRate = getCpuRatioForWindows();
//			infoBean.setCpuRate(cpuRate);
		}
		return infoBean;
	}
	
	private static String getLinuxVersion()
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringTokenizer tokenStat = null;
		try 
		{
			Process process = Runtime.getRuntime().exec("lsb_release -a");
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			br.readLine();
			br.readLine();
			br.readLine();
			tokenStat = new StringTokenizer(br.readLine());
			tokenStat.nextToken();
			String linuxVersion = tokenStat.nextToken();
			return linuxVersion;
		} catch (Exception e) {
//			e.printStackTrace();
		    return null;
		}
		finally
		{
			freeResource(is, isr, br);
		}
	}

	
	private static double getCpuRateForLinux(String linuxVersion) {

		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader brStat = null;
		StringTokenizer tokenStat = null;
		try {
			//Get usage rate of CUP
			Process process = Runtime.getRuntime().exec("top -bi -n 2 -d 0.02");
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			brStat = new BufferedReader(isr);
			if (linuxVersion != null && linuxVersion.length() > 0 && linuxVersion.equals("2.4")) {
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				tokenStat = new StringTokenizer(brStat.readLine());
				tokenStat.nextToken();
				tokenStat.nextToken();
				String user = tokenStat.nextToken();
				tokenStat.nextToken();
				String system = tokenStat.nextToken();
				tokenStat.nextToken();
				String nice = tokenStat.nextToken();
				System.out.println(user + " , " + system + " , " + nice);
				user = user.substring(0, user.indexOf("%"));
				system = system.substring(0, system.indexOf("%"));
				nice = nice.substring(0, nice.indexOf("%"));
				float userUsage = new Float(user).floatValue();
				float systemUsage = new Float(system).floatValue();
				float niceUsage = new Float(nice).floatValue();
				return (userUsage + systemUsage + niceUsage) / 100;
			} 
			else 
			{
				int hitCnt = 0;
				String line;
				while ( (line = brStat.readLine())!=null)
				{
				    if(line.toLowerCase().startsWith("cpu"))
	                {
	                    hitCnt++;
	                    if(hitCnt==2)
	                    {
	                        tokenStat = new StringTokenizer(line);
	                        tokenStat.nextToken();
	                        String cpuUsage = tokenStat.nextToken();
	                        Float usage = new Float(cpuUsage.substring(0, cpuUsage.indexOf("%")));
	                        return usage;
	                    }
	                }
				}
				return -1;
			}

		} catch (IOException ioe) {

			System.out.println(ioe.getMessage());

			freeResource(is, isr, brStat);
		} finally {

			freeResource(is, isr, brStat);

		}
		return -1;
	}

	private static void freeResource(InputStream is, InputStreamReader isr, BufferedReader br) {

		try {
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
	
	private static double getCpuRatioForWindows() {
        try {
            String procCmd = System.getenv("windir")
                    + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,"
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return Double.valueOf(
                        PERCENT * (busytime) / (busytime + idletime)).doubleValue();
            } else {
                return 0.0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

	private static long[] readCpu(final Process proc) {
		final int FAULTLENGTH = 10;
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();

            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }

            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;

            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }

                String caption = substring(line, capidx, cmdidx - 1) .trim();
                String cmd = substring(line, cmdidx, kmtidx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0) {
                    continue;
                }

                // log.info("line="+line);
                if (caption.equals("System Idle Process") || caption.equals("System")) {
                    idletime += Long.valueOf(
                            substring(line, kmtidx, rocidx - 1).trim()).longValue();
                    idletime += Long.valueOf(
                            substring(line, umtidx, wocidx - 1).trim()).longValue();
                    continue;
                }

                kneltime += Long.valueOf(
                        substring(line, kmtidx, rocidx - 1).trim()).longValue();
                usertime += Long.valueOf(
                        substring(line, umtidx, wocidx - 1).trim()).longValue();
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String substring(String src, int start_idx, int end_idx) {
        byte[] b = src.getBytes();
        String tgt = "";
        for (int i = start_idx; i <= end_idx; i++) {
            tgt += (char) b[i];
        }
        return tgt;
    }
}
