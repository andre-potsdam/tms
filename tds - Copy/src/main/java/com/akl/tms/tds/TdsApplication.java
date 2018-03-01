package com.akl.tms.tds;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;

@SpringBootApplication
@EnableNeo4jRepositories
public class TdsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TdsApplication.class, args); 
	}



//	@Bean
//	public SessionFactory getSessionFactory() {
//		return new SessionFactory(configuration(), "org.neo4j.cineasts.domain");
//	}
//
//	@Bean
//	public Neo4jTransactionManager transactionManager() throws Exception {
//		return new Neo4jTransactionManager(getSessionFactory());
//	}
//
//	@Bean
//	public org.neo4j.ogm.config.Configuration configuration() {
//		return new org.neo4j.ogm.config.Configuration.Builder()
//				.uri("bolt://localhost")
//				.build();
//	}
}
