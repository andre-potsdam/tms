package com.akl.tms.tds.repo;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.akl.tms.tds.repo.model.TestSuite;


@Repository
public interface TestSuiteRepository extends Neo4jRepository<TestSuite, Long> {

	/** Search existing TestSuite with given name. */
	public TestSuite findByName(String name);
}
