package com.kriyatec.dynamic.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;


@NoRepositoryBean
public interface BaseRepository<T, ID> extends MongoRepository<T, ID> {
    
}

//@Repository