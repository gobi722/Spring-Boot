package com.example.adminservice.ServiceImpl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.adminservice.Service.BaseEntity;
import com.example.adminservice.Service.GenericCrudService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;

@Service
public class GenericEntityServiceImpl implements GenericCrudService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public <T extends BaseEntity> void saveEntity(T entity) {
        entityManager.persist(entity);       
    }
    @Transactional
    public <T extends BaseEntity> T getEntityById(Class<T> clazz, Long id) {
        return entityManager.find(clazz, id);
    }
    @Transactional
    public <T extends BaseEntity> List<T> getAllEntities(Class<T> clazz) {
        String jpql = "SELECT e FROM " + clazz.getSimpleName() + " e";
        return entityManager.createQuery(jpql, clazz).getResultList();
    }
    @Transactional
    public <T extends BaseEntity> void saveOrUpdateEntity(T entity) {
        if (entity.getId() == null || entityManager.find(entity.getClass(), entity.getId()) == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    } 
    @Transactional
    public <T extends BaseEntity> List<T> getEntitiesByCriteria(Class<T> clazz, Map<String, String> criteria) {
        StringBuilder jpql = new StringBuilder("SELECT e FROM " + clazz.getSimpleName() + " e WHERE ");
        for (String key : criteria.keySet()) {
            jpql.append("e.").append(key).append(" = :").append(key).append(" AND ");
        }
        jpql.setLength(jpql.length() - 5); 
System.out.print(jpql.toString());
        TypedQuery<T> query = entityManager.createQuery(jpql.toString(), clazz);
        for (Map.Entry<String, String> entry : criteria.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }
    @Transactional
    public <T extends BaseEntity> void deleteAllEntities(Class<T> clazz) {
        entityManager.createQuery("DELETE FROM " + clazz.getSimpleName()).executeUpdate();
    }
}
