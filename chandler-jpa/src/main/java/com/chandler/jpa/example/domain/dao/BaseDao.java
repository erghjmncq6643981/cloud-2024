package com.chandler.jpa.example.domain.dao;

import com.chandler.jpa.example.domain.dataobject.BaseEntity;
import com.google.common.collect.Lists;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;

@Repository
public class BaseDao implements IBaseDao {

	@PersistenceContext
	public EntityManager em;

	@Override
	public void persist(BaseEntity entity) {
		em.persist(entity);
	}

	/**
	 * saveOrUpdate
	 *
	 * @param entity 实体
	 */
	@Override
	public void merge(BaseEntity entity) {
		em.merge(entity);
	}

	@Override
	public void remove(BaseEntity entity) {
		em.remove(entity);
	}

	@Override
	public <T extends BaseEntity> T findById(Class<T> entityClass,
			long primaryKey) {
		try {
			return em.find(entityClass, primaryKey);
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public <T extends BaseEntity> T find(Class<T> entityClass, String field,
										 Object value) {
		try {
			String jpql = String.format("SELECT a FROM %s a WHERE %s = '%s'",
					entityClass.getSimpleName(), field, value);
			return em.createQuery(jpql, entityClass).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public <T extends BaseEntity> List<T> finds(Class<T> entityClass,
												String field, Object value) {
		String jpql = String.format("SELECT a FROM %s a WHERE %s = '%s'",
				entityClass.getSimpleName(), field, value);
		return em.createQuery(jpql, entityClass).getResultList();
	}

	@Override
	public <T extends BaseEntity> List<T> findIn(Class<T> entityClass, String field, List<Object> values) {
		StringBuilder whereIn = new StringBuilder();
		for (int i = 0; i < values.size(); i ++) {
			Object value = values.get(i);
			whereIn.append("'");
			whereIn.append(value);
			whereIn.append("'");
			
			if (i + 1 < values.size()) {
				whereIn.append(",");
			}
		}
		String jpql = String.format("SELECT a FROM %s a WHERE %s in (%s)", 
				entityClass.getSimpleName(), field, whereIn.toString());
		return em.createQuery(jpql, entityClass).getResultList();
	}

	@Override
	public <T extends BaseEntity> List<T> findLike(Class<T> entityClass,
												   String field, Object value) {
		String jpql = String.format("SELECT a FROM %s a WHERE %s LIKE '%s'",
				entityClass.getSimpleName(), field, "%" + value + "%");
		TypedQuery<T> query = em.createQuery(jpql, entityClass);
		query.setParameter("value", "%" + value + "%");
		return query.getResultList();
	}

	@Override
	public <T extends BaseEntity> T findById(Class<T> entityClass,
			String primaryKey) {
		try {
			return em.find(entityClass, primaryKey);
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public <T extends BaseEntity> List<T> findByIds(Class<T> entityClass,
			List primaryKeys) {
		List<T> list = Lists.newArrayList();
		for (Object pk : primaryKeys) {
			T entity = null;
			if (pk instanceof Long) {
				entity = findById(entityClass, (Long) pk);
			} else if (pk instanceof String) {
				entity = findById(entityClass, (String) pk);
			}
			if (entity != null) {
				list.add(entity);
			}
		}
		return list;
	}

	@Override
	public <T extends BaseEntity> List<T> findAll(Class<T> clazz) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		cq.from(clazz);
		TypedQuery<T> tq = em.createQuery(cq);
		return tq.getResultList();
	}

	@Override
	public <T extends BaseEntity> T getById(Class<T> entityClass,
			long primaryKey) {
		return em.find(entityClass, primaryKey);
	}

	@Override
	public <T extends BaseEntity> T getById(Class<T> entityClass,
			String primaryKey) {
		return em.find(entityClass, primaryKey);
	}

	@Override
	public void executeUpdate(String sql) {
		em.createNativeQuery(sql).executeUpdate();
	}

	@Override
	public void detach(BaseEntity entity) {
		em.detach(entity);
	}
	public void flush() {
		em.flush();
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em
				.getEntityManagerFactory();
		return new JdbcTemplate(info.getDataSource());
	}

}
