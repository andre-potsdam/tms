package com.akl.tms.tds.repo.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NodeEntity
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"description","testSuiteRuns","testCases"})
public class TestSuite {

	@GraphId
	@Getter
	private Long id;
	
	@Getter
	@NonNull
	private String name;
	
	@Getter
	@Setter
	private String description;

	@Relationship(type = "has", direction = Relationship.OUTGOING)
	private Set<TestSuiteRun> testSuiteRuns;
	
	@Relationship(type = "contains", direction = Relationship.OUTGOING)
	private Set<TestCase> testCases;

	
	// ----------------------------------------------
	
	public Set<TestSuiteRun> getTestSuiteRuns() {
		return Optional.ofNullable(testSuiteRuns).orElse(Collections.emptySet());
	}
	
	public void addTestSuiteRun(TestSuiteRun testSuiteRun) {
		if (testSuiteRuns==null) {
			testSuiteRuns = new HashSet<>();
		}
		testSuiteRuns.add(testSuiteRun);
	}
	
	
	public Set<TestCase> getTestCases() {
		return Optional.ofNullable(testCases).orElse(Collections.emptySet());
	}
	
	public void addTestCase(TestCase testCase) {
		if (testCases==null) {
			testCases = new HashSet<>();
		}
		testCases.add(testCase);
	}

}
