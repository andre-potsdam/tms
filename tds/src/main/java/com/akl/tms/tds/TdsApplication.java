package com.akl.tms.tds;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;

import com.akl.tms.tds.repo.model.TestCase;

@SpringBootApplication
@EnableNeo4jRepositories
public class TdsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TdsApplication.class, args); 
	}


	// neo4j configuration is done with file ogm.properties
//	@Bean
//	public org.neo4j.ogm.config.Configuration configuration() {
//		return new org.neo4j.ogm.config.Configuration.Builder()
//				.uri("bolt://localhost")
//				.build();
//	}

	
	@Bean
	public SessionFactory getSessionFactory() {
		// Session factory for neo4j.
		// Must be created explicitly here.
		// Parameters are a list of all packages, which contain domain classes
		// to be converted by OGM.
		//
		// Otherwise spring instantiates a default session, which scans all tms classes
		// and causes strange NullPointerException.
		String[] packagesWithDomainClasses = { TestCase.class.getPackage().getName() };
		return new SessionFactory(packagesWithDomainClasses);
	}

	
	@Bean
	public Neo4jTransactionManager transactionManager() throws Exception {
		return new Neo4jTransactionManager(getSessionFactory());
	}
}
