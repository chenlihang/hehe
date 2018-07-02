package cn.com.boomhope.common.ehcache;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class EhcacheInteceptor {
	private Logger log = Logger.getLogger(EhcacheInteceptor.class);
	
	private Cache cache;
	
	private List<String> setPatterns;
	
	private List<String> updatePatterns;

	/**
	 * 对于要增强的方法，执行环绕处理检查心跳是否正常
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	Object heartbeat(ProceedingJoinPoint joinPoint) throws Throwable{
		Object[] args = joinPoint.getArgs();
		String method = joinPoint.getSignature().getName();
		String className = joinPoint.getThis().getClass().getName();
		
		String cacheKey = getCacheKey(className, method, args);
		if(isContains(setPatterns, method)){//当前方法为settup ehcache方法
			return setupEhcache(joinPoint, args, cacheKey);
		}
		else{//更新ehcache
//			return updateEhcache(joinPoint, cacheKey);
			return updateEhcache(joinPoint);
		}
	}

	private Object setupEhcache(ProceedingJoinPoint joinPoint, Object[] args, String cacheKey) throws Throwable {
		Element element = cache.get(cacheKey);

		if (element == null) {
			log.debug("-----非缓存中查找，查找后放入缓存, key:"+cacheKey+"------");
			Object result = joinPoint.proceed(args); 
			element = new Element(cacheKey, (Serializable) result);
			cache.put(element);
		}else{
			log.debug("----缓存中查找----");
		}
		return element.getValue();
	}
	
	private Object updateEhcache(ProceedingJoinPoint joinPoint) throws Throwable {
		String className = joinPoint.getThis().getClass().getName();
		@SuppressWarnings("rawtypes")
		List list = cache.getKeys();
		for (int i = 0; i < list.size(); i++) {
			String cacheKey = String.valueOf(list.get(i));
			if (cacheKey.startsWith(className)) {
				cache.remove(cacheKey);
				if(log.isDebugEnabled()){
					log.debug("------清除缓存key:"+cacheKey+"----");
				}
			}
		}
		return joinPoint.proceed(joinPoint.getArgs());
	}
	
	@SuppressWarnings("unused")
	private Object updateEhcache(ProceedingJoinPoint joinPoint, String cacheKey) throws Throwable {
		if(cache.get(cacheKey)!=null){
			cache.remove(cacheKey);
			if(log.isDebugEnabled()){
				log.debug("------清除缓存key:"+cacheKey+"----");
			}
		}
		return joinPoint.proceed(joinPoint.getArgs());
	}
	
	/**
	 * 获得cache key 的方法，cache key 是Cache 中一个Element 的唯一标识 55 * cache key
	 * 包括包名+类名+方法名，如 com.co.cache.service.UserServiceImpl.getAllUser 56
	 */
	private String getCacheKey(String targetName, String methodName,
			Object[] arguments) {
		StringBuffer sb = new StringBuffer();
		sb.append(targetName).append(".").append(methodName);
		if ((arguments != null) && (arguments.length != 0)) {
			for (int i = 0; i < arguments.length; i++) {
				sb.append(".").append(arguments[i]);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 对于要增强的方法，在方法之前执行
	 * @param joinPoint 连接点信息
	 */
	void before(JoinPoint joinPoint){
	};
	
	/**
	 * 对于要增强的方法，在方法之后执行
	 * @param joinPoint  连接点信息
	 */
	void after(JoinPoint joinPoint){
	};
	/**
	 * 对于要增加的方法，方法返回结果后，对结果进行记录或者分析
	 * 方法
	 * @param obj target执行后返回的结果
	 */
	void afterReturning(JoinPoint joinPoint,Object obj){
	};
	
	/**
	 * 对于要增强的方法，方法抛出异常后，对异常的增强处理，比如记录异常，或者根据异常分析数据什么的
	 * @param e target执行后抛出的异常
	 */
	void handlerException(Throwable e){
		if(log.isDebugEnabled()){
			log.debug(e);
		}
	}
	
	private boolean isContains(List<String> patterns, String method){
		for(String regex : patterns){
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(method);
			if(m.find()){
				return true;
			}
		}
		return false;
	}
	
	boolean checkHeartbeat(){
		return true;
	}
	
	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public List<String> getSetPatterns() {
		return setPatterns;
	}

	public void setSetPatterns(List<String> setPatterns) {
		this.setPatterns = setPatterns;
	}

	public List<String> getUpdatePatterns() {
		return updatePatterns;
	}

	public void setUpdatePatterns(List<String> updatePatterns) {
		this.updatePatterns = updatePatterns;
	}
}
