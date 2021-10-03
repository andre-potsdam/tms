package com.akl.tms.tds.repo;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.akl.tms.tds.repo.model.TestSuiteRun;


@Repository
public interface TestSuiteRunRepository extends Neo4jRepository<TestSuiteRun, Long> {
}
