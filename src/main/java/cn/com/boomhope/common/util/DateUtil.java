package cn.com.boomhope.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class DateUtil
{
	protected static Logger log = Logger.getLogger(DateUtil.class);
	
	public static int getDaysBetween(Date date, Date date2)
	{
		long l1 = date.getTime();
		long l2 = date2.getTime();
		long dl = Math.abs(l2 - l1);
		return (int) (dl / TimeUnit.DAYS.toMillis(1)) + 1;
	}
	
	/**
	 * 字符串转换为日期
	 * @param date
	 * @param formatter 如"yyyy-MM-dd hh:mm:ss"
	 * @return
	 */
	public static Date parse(String date, String formatter) throws Exception
	{
		SimpleDateFormat format = new SimpleDateFormat(formatter);
		return format.parse(date);
	}
	
	/**
	 * 格式化日期
	 * @param date
	 * @param formatter 如"yyyy-MM-dd hh:mm:ss"
	 * @return
	 */
	public static String format(Date date, String formatter)
	{
		SimpleDateFormat format = new SimpleDateFormat(formatter);
		return format.format(date);
	}
	
	 /**
     * 获得指定日期的前一天
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay, String format) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(specifiedDay);
        } catch (ParseException e) {
        	log.error(e);
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat(format).format(c
                .getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后一天
     * 
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay, String format) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(specifiedDay);
        } catch (ParseException e) {
        	log.error(e);
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat(format)
                .format(c.getTime());
        return dayAfter;
    }
    
    /**
     *  获取两个日期之间所间隔的天数
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long getdaysOfTwoDate(Date beginDate, Date endDate)
    {
    	Calendar beginCalendar = Calendar.getInstance();  
        Calendar endCalendar = Calendar.getInstance();  
        beginCalendar.setTime(beginDate);  
        endCalendar.setTime(endDate);
        
        long dayBetween = getTime(beginDate, endDate)/(24*60*60*1000);
        
        // 将结果返回
        return dayBetween;
    }
    
    // 获取日期之间的间隔数
	private static long getTime(Date beginDate, Date endDate){  
        return endDate.getTime() - beginDate.getTime();  
    } 
	
	// 获取一个日期的前2年日期
	public static Date getTwoYearBeforeDate(Date date,Integer year)
	{
		 Calendar calendar = Calendar.getInstance();  
		 calendar.setTime(date);  
		 calendar.add(Calendar.YEAR, -year);
	     return calendar.getTime();
	}
	
	// 比较两个日期, 得到相差值
	public static long getTimsBetweenTwoDays(Date date1, Date date2)
	{
		long time_date1 = date1.getTime();
		long time_date2 = date2.getTime();
		return time_date2 - time_date1;
	}
	
	/**
	 * 比较两个日期是否是同一天
	 * @param date
	 * @param formatter 如"yyyy-MM-dd hh:mm:ss"
	 * @return
	 */
	public static boolean checkDateEqual(Date date1, Date date2, String formatter)
	{
		SimpleDateFormat format = new SimpleDateFormat(formatter);
		String date1Str =  format.format(date1);
		String date2Str =  format.format(date2);
		if (date1Str.equals(date2Str))
		{
			return true;
		}
		return false;
	}
	
	
	public static Date addDate(Date date,int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + days);
        return c.getTime();
    }
}


