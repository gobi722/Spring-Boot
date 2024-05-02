package com.kriyatec.hibernate.repository;
import com.kriyatec.hibernate.entities.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
	public interface ConfigurationRepository extends MongoRepository<configuration, String> {
	
//		public String findByUsernameAndEmail();
	}


