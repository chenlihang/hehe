package cn.com.boomhope.common.util;

public class ModUtil
{
	/**
	 * hash求模
	 * 
	 * @param id
	 * @return
	 */
	public static int getMod(String id, int max)
	{// 只有在hibernate运行模式下才进行
		return Math.abs(id.hashCode() % max);
	}

	public static void main(String[] args)
	{
		int featureCnt = 5;// 每个人最多有模板数
		long feature = 15;// 18k
		long oraceMaxDb = 2 * 1024 * 1024;// 2G
		long totalCnt = 100 * 10000;// 100w
		long totalMenery = totalCnt * feature * featureCnt;// 100w用户所用的内存占用量
		System.out.println("需要占用内存：" + totalMenery / (1024 * 1024) + "G");
		System.out.println(totalCnt / 36);
		if (totalMenery <= oraceMaxDb)
		{
			System.out.println("需要分表：1个");
		}
		else
		{
			System.out.println("需要分表：" + (totalMenery / oraceMaxDb == 0 ? totalMenery / oraceMaxDb : ((totalMenery / oraceMaxDb) + 1)) + "个");
		}
		//System.out.println("保存位置在表" + ModUtil.getMod("2"));
	}
}
