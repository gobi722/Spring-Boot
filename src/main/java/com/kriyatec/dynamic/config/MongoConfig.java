package com.kriyatec.dynamic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoTypeMapper;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

@Configuration
public class MongoConfig {

    @Bean
    MappingMongoConverter mappingMongoConverter(org.springframework.data.mongodb.MongoDatabaseFactory factory, MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> converter) {
        MappingMongoConverter mappingConverter = new MappingMongoConverter(factory, converter);
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null)); // Disable _class field
        return mappingConverter;
    }
}

