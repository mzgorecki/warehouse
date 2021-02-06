package com.demo.warehouse.database;

import static com.demo.Assertions.isNotNull;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;

public abstract class AbstractHibernateDAO<E, I extends Serializable> {

	private final Class<E> entityClass;

	@PersistenceContext
	private EntityManager entityManager;

	public AbstractHibernateDAO(Class<E> entityClass) {
		isNotNull(entityClass, "EntityClass cannot be null!");
		this.entityClass = entityClass;
	}

	private Session currentSession() {
		return entityManager.unwrap(Session.class);
	}

	Criteria criteria() {
		return currentSession().createCriteria(this.entityClass);
	}

	public void save(E obj) {
		currentSession().save(obj);
	}
}
