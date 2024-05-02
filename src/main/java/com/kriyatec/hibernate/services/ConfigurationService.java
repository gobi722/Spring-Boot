
package com.kriyatec.hibernate.services;
import com.kriyatec.hibernate.repository.ConfigurationRepository;
import com.kriyatec.hibernate.entities.*;
import com.kriyatec.hibernate.repository.ConfigurationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationService {

   // private final ConfigurationRepository configurationRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ConfigurationService( ConfigurationRepository configurationRepository, MongoTemplate mongoTemplate) {
       // this.configurationRepository = configurationRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<configuration> findDbbyOrgId(String OrgId) {
        List<configuration> configurations = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("OrgId").is(OrgId))
                ),
                "configuration",
                configuration.class
        ).getMappedResults();

        return configurations;
    }
}