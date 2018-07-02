package cn.com.boomhope.common.orm.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.type.Type;

import cn.com.boomhope.common.web.easyui.view.Page;

/**
 * dao接口封装
 * 
 * @author 郑铭生
 *
 * @param <T>
 * @param <PK>
 */
public interface IBaseDao<T extends Serializable>
{
	String FROM = "from";

	/**
	 * 打开session
	 * 
	 * @return
	 */
	Session openSession();
	
	/**
	 * 关闭链接，不包括session.flush()
	 * @param session
	 */
	public void closeSessionWithoutFlush(Session session);

	/**
	 * 关闭链接，包括session.flush()
	 * 
	 * @param session
	 */
	void closeSession(Session session);
	
	/**
	 * 打印
	 */
	void printRepairLog(String sql, Object[] values, Type[] types);

	/**
	 * <保存实体>
	 * 
	 * @param t
	 *            实体
	 */
	int save(T t);

	/**
	 * <删除实体>
	 * 
	 * @param t
	 *            实体
	 */
	int delete(T t);

	/**
	 * <更新实体>
	 * 
	 * @param t
	 *            实体
	 */
	int update(T t);

	/**
	 * 
	 * @param hql
	 * @param condition
	 * @return
	 */
	<P> P load(String hql, Map<String, Object> condition);

	/**
	 * <获取所有记录,不建议再数据量很大的表中使用>
	 * 
	 * @param hqlString
	 *            HQL语句
	 * @param values
	 *            不定参数的Object数组
	 * @return 查询多个实体的List集合
	 */
	List<T> getList();

	/**
	 * <根据HQL语句，得到对应的list>
	 * 
	 * @param hql
	 *            HQL语句
	 * @param values
	 *            不定参数的Object数组
	 * @return 查询多个实体的List集合
	 */
	List<?> getListByHQL(String hql, Map<String, Object> condition);

	/**
	 * <根据HQL语句,得到记录数>
	 * 
	 * @param hql
	 * @param args
	 * @return
	 */
	Long getCountByHql(String hql, Map<String, Object> condition);

	/**
	 * <执行hql>
	 * 
	 * @param hql
	 * @param args
	 *            不定参数的Object数组
	 * @return 1 成功 0 失败
	 */
	int executeHql(String hql, Map<String, Object> condition);

	/**
	 * 分页查询
	 * @param cache 是否使用缓存
	 * @param listHql
	 * @param cntHql
	 * @param condition
	 * @param page
	 * @param rows
	 * @return
	 */
	Page getPage(boolean cache, String listHql, String cntHql, Map<String, Object> condition, int page, int rows);

	/**
	 * 获取序列
	 * @param seqName
	 * @return
	 */
	Long getSequence(String seqName);
	
	List<Object[]> executeSql(String sql, Map<String, Object> condition);

	List<?> queryByLimit(String hql, int s, int max);

	int batchDelete(String hql, Map<String, Object> condition);
	
	// 直接执行sql, 获取list列表
	List<Object> executeSql(String sql);
	
	public int executeSQL(String sql) ;
	
}
