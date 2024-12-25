package com.chandler.jpa.example.domain.dao;

import java.util.List;

import com.chandler.jpa.example.domain.dataobject.BaseEntity;
import org.springframework.jdbc.core.JdbcTemplate;


public interface IBaseDao {
	void persist(BaseEntity entity);

	void merge(BaseEntity entity);

	void remove(BaseEntity entity);

	<T extends BaseEntity> T findById(Class<T> entityClass, long primaryKey);

	<T extends BaseEntity> T findById(Class<T> entityClass,
			String primaryKey);
	<T extends BaseEntity> T find(Class<T> entityClass, String field,
			Object value);

    <T extends BaseEntity> List<T> finds(Class<T> entityClass, String field,
			Object value);

    <T extends BaseEntity> List<T> findLike(Class<T> entityClass,
			String field, Object value);

    <T extends BaseEntity> List<T> findByIds(Class<T> entityClass,
			List primaryKeys);

	<T extends BaseEntity> List<T> findAll(Class<T> clazz);

	<T extends BaseEntity> T getById(Class<T> entityClass, long primaryKey);

	<T extends BaseEntity> T getById(Class<T> entityClass,
			String primaryKey);

	void executeUpdate(String sql);

	void detach(BaseEntity entity);

	JdbcTemplate getJdbcTemplate();

	<T extends BaseEntity> List<T> findIn(Class<T> entityClass, String field, List<Object> values);
}
