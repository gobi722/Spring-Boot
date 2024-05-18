package com.example.adminservice.ServiceImpl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.adminservice.Service.BaseEntity;
import com.example.adminservice.Service.GenericCrudService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    public <T extends BaseEntity> T getById(Class<T> clazz, Long id) {
        return entityManager.find(clazz, id);
    }

//    public <T extends BaseEntity> List<T> getAll(Class<T> clazz) {
//        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<T> query = builder.createQuery(clazz);
//        query.from(clazz);
//        return entityManager.createQuery(query).getResultList();
//    } 
    
    
}
