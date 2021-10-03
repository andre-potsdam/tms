package com.akl.tms.tds.repo.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@NodeEntity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString(exclude = {"description", "testSuite", "executedBy"})
public class TestCase {

	/** Unique ID over all database nodes. */
	@GraphId 
	@Getter
	private Long id;

	/** Unique for all testcases inside one testsuite. */
	@Getter
	@NonNull
	private String name;

	/** Optional. */
	@Getter 
	@Setter
	private String description;

	/** Mandatory related testsuite. */
	@Relationship(type = "contains", direction = Relationship.INCOMING)
	@Getter 
	@Setter
	@NonNull
	private TestSuite testSuite;

	/** Get all relationships to TestSuiteRuns. */
	@Relationship(type = "executes", direction = Relationship.INCOMING)
	private Set<TestSuiteRunExecutesTestCaseRelationship> executedBy;

	
	// ----------------------------------------------
	
	public Set<TestSuiteRunExecutesTestCaseRelationship> getExecutes() {
		return Collections.unmodifiableSet( Optional.ofNullable(executedBy).orElse(Collections.emptySet()) );
	}
	
	public void addExecutes(TestSuiteRunExecutesTestCaseRelationship executes) {
		if (executedBy==null) {
			executedBy = new HashSet<>();
		}
		executedBy.add(executes);
	}

}
