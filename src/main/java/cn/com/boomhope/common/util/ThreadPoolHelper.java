package cn.com.boomhope.common.util;

//import cn.com.boomhope.bioauth.data.SettingCxtRmi;

public class ThreadPoolHelper {
	public static int corePoolSize;//核心池数量
	public static int maximumPoolSize;//最大池数量
	
	static{
		//核心池（当前使用池）数量为cpu核数+1，如果为cpu型应用
		corePoolSize = 33;//SettingCxtRmi.RMI_SERVER_CPU_NUMBER+1;
		maximumPoolSize = corePoolSize + 10;
	}
	
	public static int getSearchStep(int totalCnt){
		if(totalCnt > 0){
			if(totalCnt%corePoolSize == 0){
				return totalCnt/corePoolSize;
			}else{
				return (int)(totalCnt/corePoolSize) + 1;
			}
		}
		return 0;
	}
	
	public static int getSearchThreadCnt(int totalCnt, int step){
		if(totalCnt > 0){
			if(totalCnt <= step){//总数少于默认搜索步长，只开一个线程
				return 1;
			}else{
				if(totalCnt%step == 0){
					return totalCnt/step;
				}else{
					return (int)(totalCnt/step) + 1;
				}
			}
		}
		return 0;
	}
	
	public static void main(String[] args) {
		System.out.println(getSearchThreadCnt(20000, 3000));
	}
}
