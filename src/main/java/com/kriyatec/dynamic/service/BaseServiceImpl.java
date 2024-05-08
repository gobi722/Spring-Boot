package com.kriyatec.dynamic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kriyatec.dynamic.repository.BaseRepository;

@Service
public class BaseServiceImpl<T> implements GenericService<T> {

//	 @Autowired
    private BaseRepository<T, String> genericRepository;
    public void setGenericRepository(BaseRepository<T, String> genericRepository) {
        this.genericRepository = genericRepository;
    }
    @Override
    public T create(String collectionName, T entity) { 
        return genericRepository.save(entity);
    }

    @Override
    public T update(String collectionName, String id, T entity) {
      
        return genericRepository.save(entity);
    }

    @Override
    public T getById(String collectionName, String id) {
        return genericRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(String collectionName, String id) {
        genericRepository.deleteById(id);
    }
}
