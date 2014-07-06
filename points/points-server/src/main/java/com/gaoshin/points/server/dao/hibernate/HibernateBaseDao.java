package com.gaoshin.points.server.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.gaoshin.points.server.dao.GenericDao;

public class HibernateBaseDao implements GenericDao {
	protected HibernateTemplate hibernateTemplate;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

    @Override
    public void saveEntity(Object entity) {
        hibernateTemplate.saveOrUpdate(entity);
        hibernateTemplate.flush();
    }

    @Override
    public void insertEntity(Object entity) {
        hibernateTemplate.save(entity);
        hibernateTemplate.flush();
    }

	@Override
	public <T> T getEntity(Class<T> entityClass, Serializable id) {
		return hibernateTemplate.get(entityClass, id);
	}

    @Override
    public <T> List<T> list(final Class<T> entityClass, final String searchPath, final Object value) {
        return (List<T>) this.hibernateTemplate
        .execute(new HibernateCallback() {
            public Object doInHibernate(Session session) {
                String hql = "select e from " + entityClass.getSimpleName() + " e where e." + searchPath + "=:value";
                List list = session.createQuery(hql).setParameter("value", value).list();
                return list;
            }
        });
    }

    @Override
    public <T> List<T> find(final Class<T> entityClass, final String query, final Object value) {
        return hibernateTemplate.find(query, value);
    }

    @Override
    public <T> List<T> find(final Class<T> entityClass, final int offset, final int size, final String hql, final Object... value) {
        return (List<T>) this.hibernateTemplate
        .execute(new HibernateCallback() {
            public Object doInHibernate(Session session) {
                Query query = session.createQuery(hql);
                for(int i=0; i<value.length; i++) {
                	query.setParameter(i, value[i]);
                }
                List list = query.setFirstResult(offset).setMaxResults(size).list();
                return list;
            }
        });
    }
    
    @Override
    public <T> List<T> find(final Class<T> entityClass, final int size, final String hql, final Object... value) {
        return (List<T>) this.hibernateTemplate
        .execute(new HibernateCallback() {
            public Object doInHibernate(Session session) {
                Query query = session.createQuery(hql);
                for(int i=0; i<value.length; i++) {
                	query.setParameter(i, value[i]);
                }
                List list = query.setMaxResults(size).list();
                return list;
            }
        });
    }
    
    @Override
    public List find(final String query, final Object... value) {
        return hibernateTemplate.find(query, value);
    }

    @Override
    public <T> T findUnique(Class<T> entityClass, final String query, final Object... value) {
        List list = hibernateTemplate.find(query, value);
        if(list.isEmpty()) {
        	return null;
        }
        if(list.size() > 1) {
        	throw new RuntimeException("More than one record found ");
        }
        return (T) list.get(0);
    }

	@Override
	public <T> List<T> nativeQuery(final Class<T> entityClass, final String sql, final Object... values) {
		return (List<T>) this.hibernateTemplate
		.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				SQLQuery query = session.createSQLQuery(sql);
				for(int i=0; i<values.length; i++) {
					query.setParameter(i, values[i]);
				}
				query.addEntity(entityClass);
				List list = query.list();
				return list;
			}
		});
	}

	@Override
	public <T> T getUniqueResult(Class<T> entityClass, String searchPath,
			Object value) {
		List<T> list = list(entityClass, searchPath, value);
		return list.size() == 0 ? null : list.get(0);
	}

	@Override
	public void deleteEntity(Object entity) {
		hibernateTemplate.delete(entity);
        hibernateTemplate.flush();
	}

	@Override
	public void nativeUpdate(final String sql) {
		this.hibernateTemplate
		.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				SQLQuery query = session.createSQLQuery(sql);
				query.executeUpdate();
				return null;
			}
		});
		hibernateTemplate.flush();
	}

	@Override
	public <T> List<T> list(final Class<T> class1, final String path, final Object value,
			final int offset, final int size) {
		return (List<T>) this.hibernateTemplate
		.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				String hql = "select e from " + class1.getSimpleName() + " e where e." + path + "=:value";
				return session.createQuery(hql).setParameter("value", value).setFirstResult(offset).setMaxResults(size).list();
			}
		});
	}

	@Override
	public <T> List<T> list(final Class<T> class1, final int offset, final int size, final String orderby, final boolean asc) {
		return (List<T>) this.hibernateTemplate
		.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				String hql = "select e from " + class1.getSimpleName() + " e order by e."+orderby + (asc ? " asc" : " desc");
				return session.createQuery(hql).setFirstResult(offset).setMaxResults(size).list();
			}
		});
	}

	@Override
	public <T> List<T> list(final Class<T> class1, final String path, final Object value,
			final int offset, final int size, final String orderby, final boolean asc) {
		return (List<T>) this.hibernateTemplate
		.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				String hql = "select e from " + class1.getSimpleName() + " e where e." + path + "=:value order by e." + orderby + (asc ? " asc" : " desc");
				return session.createQuery(hql).setParameter("value", value).setFirstResult(offset).setMaxResults(size).list();
			}
		});
	}
	
	@Override
	public <T> Long count(final String field, final boolean distinct, final String query, final Object... value){
		return (Long) this.hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				StringBuilder hql = new StringBuilder("select count(");
				if(distinct) hql.append("distinct ");
				hql.append(field);
				hql.append(") "+query);
				Query query = session.createQuery(hql.toString());
                for(int i=0; i<value.length; i++) {
                	query.setParameter(i, value[i]);
                }
                List res = query.list();
                Long l = null;
                if(res !=null && !res.isEmpty()){
                	l = (Long)res.get(0);
                }
				return l;
			}
		});
	}
	
	@Override
	public <T> Long max(final String field, final String query, final Object... value){
		if(field != null)
			return (Long) this.hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					String hql = "select max("+field+") "+query;
					Query q = session.createQuery(hql);
	                for(int i=0; i<value.length; i++) {
	                	q.setParameter(i, value[i]);
	                }
	                List res = q.list();
	                Long l = null;
	                if(res !=null && !res.isEmpty()){
	                	l = (Long)res.get(0);
	                }
					return l;
				}
			});
		else
			return null;
	}
	
	@Override
	public <T> Long min(final String field, final String query, final Object... value){
		if(field != null)
			return (Long) this.hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					String hql = "select min("+field+") "+query;
					Query q = session.createQuery(hql);
	                for(int i=0; i<value.length; i++) {
	                	q.setParameter(i, value[i]);
	                }
	                List res = q.list();
	                Long l = null;
	                if(res !=null && !res.isEmpty()){
	                	l = (Long)res.get(0);
	                }
					return l;
				}
			});
		else
			return null;
	}
	
	@Override
	public <T> Long avg(final String field, final String query, final Object... value){
		if(field != null)
			return (Long) this.hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					String hql = "select avg("+field+") "+query;
					Query q = session.createQuery(hql);
	                for(int i=0; i<value.length; i++) {
	                	q.setParameter(i, value[i]);
	                }
	                List res = q.list();
	                Long l = null;
	                if(res !=null && !res.isEmpty()){
	                	l = (Long)res.get(0);
	                }
					return l;
				}
			});
		else
			return null;
	}
	
	@Override
	public <T> Long sum(final String field, final String query, final Object... value){
		if(field != null)
			return (Long) this.hibernateTemplate.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					String hql = "select sum("+field+") "+query;
					Query q = session.createQuery(hql);
	                for(int i=0; i<value.length; i++) {
	                	q.setParameter(i, value[i]);
	                }
	                List res = q.list();
	                Long l = null;
	                if(res !=null && !res.isEmpty()){
	                	l = (Long)res.get(0);
	                }
					return l;
				}
			});
		else
			return null;
	}
}
