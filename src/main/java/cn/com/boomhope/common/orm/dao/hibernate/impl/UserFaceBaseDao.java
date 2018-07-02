package cn.com.boomhope.common.orm.dao.hibernate.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.BinaryType;
import org.hibernate.type.DateType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.com.boomhope.common.orm.dao.hibernate.IBaseDao;
import cn.com.boomhope.common.util.BytesUtil;
import cn.com.boomhope.common.util.DateUtil;
import cn.com.boomhope.common.web.easyui.view.Page;

public class UserFaceBaseDao<T extends Serializable> implements IBaseDao<T>
{
	protected static Logger log = Logger.getLogger(UserFaceBaseDao.class);
	protected static Logger logRepair = Logger.getLogger("repair");
	@Autowired
	@Qualifier("sessionFactory1")
	protected SessionFactory sessionFactory1;
	@Autowired
	@Qualifier("dataSource1")
	protected DataSource datasSource1;
	
	protected Class<? extends Serializable> cls;

	@Override
	public Session openSession()
	{
		Session session = sessionFactory1.openSession();
		return session;
	}
	
	@Override
	public void closeSessionWithoutFlush(Session session){
		if (session != null) {
			session.close();
		}
	}

	@Override
	public void closeSession(Session session)
	{
		if (session != null)
		{
			session.flush();
			session.close();
		}
	}

	@Override
	public int save(T t)
	{
		Session session = null;
		try
		{
			session = this.openSession();
			session.save(t);
			return 1;
		}
		catch (Exception e)
		{
			log.error("保存实体失败", e);
		}
		finally
		{
			closeSession(session);
		}
		return 0;
	}

	@Override
	public int delete(T t)
	{
		Session session = null;
		try
		{
			session = this.openSession();
			session.delete(t);
			return 1;
		}
		catch (Exception e)
		{
			log.error("删除实体失败", e);
		}
		finally
		{
			closeSession(session);
		}
		return 0;
	}

	@Override
	public int update(T t)
	{
		Session session = null;
		try
		{
			session = this.openSession();
			session.update(t);
			return 1;
		}
		catch (Exception e)
		{
			log.error("更新实体失败", e);
		}
		finally
		{
			closeSession(session);
		}
		return 0;
	}

	@Override
	public <P> P load(String hql, Map<String, Object> condition)
	{
		Session session = null;
		try
		{
			session = this.openSession();
			Query query = session.createQuery(hql);
			if (condition != null && !condition.isEmpty())
			{
				Iterator<Entry<String, Object>> it = condition.entrySet().iterator();
				while (it.hasNext())
				{
					Entry<String, Object> entry = it.next();
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
			@SuppressWarnings("unchecked")
			List<P> list = query.list();
			if (list != null && list.size() > 0)
			{
				return list.get(0);
			}
		}
		catch (Exception e)
		{
			log.error("读取实体失败", e);
		}
		finally
		{
			closeSession(session);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getList()
	{
		Session session = null;
		try
		{
			session = this.openSession();
			Query query = session.createQuery(String.format("FROM %s", cls.getName()));
			return query.list();
		}
		catch (Exception e)
		{
			log.error("读取实体失败", e);
		}
		finally
		{
			closeSession(session);
		}
		return null;
	}

	@Override
	public List<?> getListByHQL(String hql, Map<String, Object> condition)
	{
		Session session = null;
		try
		{
			session = this.openSession();
			Query query = session.createQuery(hql);
			if (condition != null && !condition.isEmpty())
			{
				Iterator<Entry<String, Object>> it = condition.entrySet().iterator();
				while (it.hasNext())
				{
					Entry<String, Object> entry = it.next();
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
			return query.list();
		}
		catch (Exception e)
		{
			log.error("读取实体失败", e);
		}
		finally
		{
			closeSession(session);
		}
		return null;
	}

	@Override
	public Long getCountByHql(String hql, Map<String, Object> condition)
	{
		Session session = null;
		try
		{
			session = this.openSession();
			Query query = session.createQuery(hql);
			if (condition != null && !condition.isEmpty())
			{
				Iterator<Entry<String, Object>> it = condition.entrySet().iterator();
				while (it.hasNext())
				{
					Entry<String, Object> entry = it.next();
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
			List<?> list = query.list();
			if (list != null && list.size() > 0)
			{
				Object obj = list.get(0);
				if (obj != null)
				{
					return Long.parseLong(obj.toString());
				}
			}
		}
		catch (Exception e)
		{
			log.error("读取实体失败", e);
		}
		finally
		{
			closeSession(session);
		}
		return 0L;
	}

	@Override
	public int executeHql(String hql, Map<String, Object> condition)
	{
		Session session = null;
		try
		{
			session = this.openSession();
			Query query = session.createQuery(hql);
			if (condition != null && !condition.isEmpty())
			{
				Iterator<Entry<String, Object>> it = condition.entrySet().iterator();
				while (it.hasNext())
				{
					Entry<String, Object> entry = it.next();
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
			return query.executeUpdate();
		}
		catch (Exception e)
		{
			log.error("读取实体失败", e);
		}
		finally
		{
			closeSession(session);
		}
		return 0;
	}

	@Override
	public Page getPage(boolean cache, String listHql, String cntHql, Map<String, Object> condition, int page, int rows)
	{
		Session session = null;
		try
		{
			session = this.openSession();
			// 列表查询
			Query listQuery = session.createQuery(listHql);
			listQuery.setFirstResult((page-1) * rows);
			listQuery.setMaxResults(rows);
			// 分页查询
			Query cntQuery = session.createQuery(cntHql);
			// 条件
			if (condition != null && !condition.isEmpty())
			{
				Iterator<Entry<String, Object>> it = condition.entrySet().iterator();
				while (it.hasNext())
				{
					Entry<String, Object> entry = it.next();
					String key = entry.getKey();
					Object value = entry.getValue();
					listQuery.setParameter(key, value);
					cntQuery.setParameter(key, value);
				}
			}
			// 查询
			List<?> rowsList = listQuery.setCacheable(cache).list();
			int total = 0;
			List<?> countList = cntQuery.setCacheable(cache).list();
			if (countList != null && countList.size() > 0)
			{
				Object obj = countList.get(0);
				total = Integer.parseInt(obj.toString());
			}
			Page pageObj = new Page(total, rowsList);
			return pageObj;
		}
		catch (Exception e)
		{
			log.error("读取实体失败", e);
		}
		finally
		{
			closeSession(session);
		}
		return null;
	}
	
	@Override
	public Long getSequence(String seqName) {
		/*try {
			Object dialect =  org.apache.commons.beanutils.PropertyUtils.getProperty(this.sessionFactory1, "dialect");
			if (dialect.toString().equals("org.hibernate.dialect.Oracle10gDialect")) {//oracle方言
				OracleSequenceMaxValueIncrementer incr =  new OracleSequenceMaxValueIncrementer(datasSource1, seqName);
				return incr.nextLongValue();
			} 
//			else if (dialect.toString().equals("org.hibernate.dialect.SQLServerDialect")) {
//
//			} 
			//针对oracle的，其他数据库可用其他的
//			OracleSequenceMaxValueIncrementer incr =  new OracleSequenceMaxValueIncrementer(datasSource, seqName);
//			MySQLMaxValueIncrementer incr = new MySQLMaxValueIncrementer(dataSource, incrementerName, columnName);
//			DB2SequenceMaxValueIncrementer incr = new DB2SequenceMaxValueIncrementer(dataSource, incrementerName);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}*/
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> executeSql(String sql, Map<String, Object> condition){
		Session session = null;
		try
		{
			session = this.openSession();
		    SQLQuery query = session.createSQLQuery(sql);  
			if (condition != null && !condition.isEmpty())
			{
				Iterator<Entry<String, Object>> it = condition.entrySet().iterator();
				while (it.hasNext())
				{
					Entry<String, Object> entry = it.next();
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
			return  query.list();
		}
		catch (Exception e)
		{
			log.error("读取实体失败", e);
		}
		finally
		{
			closeSession(session);
		}
		return null;
	}
	
	@Override
	public List<?> queryByLimit(String hql, int s, int max) {
		Session session = null;
		try
		{
			session = this.openSession();
			Query query = session.createQuery(hql);
			query.setFirstResult(s);
			query.setMaxResults(max);
			
			return query.list();
		}
		catch (Exception e)
		{
			log.error("读取实体失败", e);
		}
		finally
		{
			closeSession(session);
		}
		return null;
	}
	
	@Override
	public int batchDelete(String hql, Map<String, Object> condition) {
		Session session = null;
		try {
			 session = this.openSession();
			Query query = session.createQuery(hql);
			if (condition != null && !condition.isEmpty())
			{
				Iterator<Entry<String, Object>> it = condition.entrySet().iterator();
				while (it.hasNext())
				{
					Entry<String, Object> entry = it.next();
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
			return query.executeUpdate();
		} catch (Exception e) {
			log.error("批量删除实体失败", e);
		}finally {
			closeSession(session);
		}
		return 0;
	}
	
	// 直接执行sql, 获取list列表
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> executeSql(String sql)
	{
		Session session = null;
	    try
	    {
		    session = this.openSession();
	        SQLQuery query = session.createSQLQuery(sql);  
		    return  query.list();
	}
	catch (Exception e)
	{
		log.error("读取实体失败", e);
	}
	finally
	{
		closeSession(session);
	}
	return null;
		
	}
	
	@Override
	public void printRepairLog(String sql, Object[] values, Type[] types) {
		if (values != null) {
			for (int i = 0; i < types.length; i++) {
				String name = types[i].getName();
				if (name.equals(DateType.INSTANCE.getName())) {
					values[i] = "to_date('"+DateUtil.format((Date)values[i], "yyyy-MM-dd hh:mm:ss")+"','yyyy-mm-dd hh24:mi:ss')";
				}
				if(name.equals(BinaryType.INSTANCE.getName())){
					values[i] = "to_blob('"+BytesUtil.bytes2hex((byte[])values[i])+"')";
				}
			}
		}
		logRepair.error(String.format("%s | %s", sql, Arrays.toString(values)));
	}
	
	/**
	 * 获取人脸表数量
	 * @return
	 */
	public int getUserFaceTableCount(){
		Session session = null;
		int count = 0;
		try
		{
			String sql = "select count(*) from sys.user_tables where REGEXP_LIKE(TABLE_NAME,'^USERFACE\\d+$') ";
			
			session = this.openSession();
			SQLQuery query = session.createSQLQuery(sql);
			
			@SuppressWarnings("unchecked")
			List<Object> list = query.list();
			if (list != null && list.size() > 0)
			{
				Object obj = list.get(0);
				if (obj != null)
				{
					count = Integer.valueOf(((BigDecimal) obj).toString());
				}
			}
			return count;
		}
		catch (Exception e)
		{
			log.error(e);
		}
		finally
		{
			this.closeSession(session);
		}
		return count;
	}

	@Override
	public int executeSQL(String sql) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
