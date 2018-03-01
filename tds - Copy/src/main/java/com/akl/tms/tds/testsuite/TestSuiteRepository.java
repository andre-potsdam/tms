package com.akl.tms.tds.testsuite;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TestSuiteRepository extends Neo4jRepository<TestSuite, Long> {

}
