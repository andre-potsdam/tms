package com.akl.tms.testsuiterun;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.akl.tms.testcase.TestCase;
import com.akl.tms.testsuite.TestSuite;

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
@ToString(exclude = {"executes"})
public class TestSuiteRun {

	@GraphId 
	@Getter
	private Long id;
	
	@Getter
	@NonNull
	private Date start;
	
	@Getter
	@Setter
	private long durationMillis;
	
	@Getter
	@Setter
	@NonNull
	private RunState state;

	/** Mandatory related testsuite. */
	@Relationship(type = "has", direction = Relationship.INCOMING)
	@Getter 
	@Setter
	private TestSuite testSuite;

	/** Relationships to TestCases. */
	@Relationship(type = "executes", direction = Relationship.OUTGOING)
	private Set<TestSuiteRunExecutesTestCaseRelationship> executes;

	
	// -------------------------------------------------
	
	public Set<TestSuiteRunExecutesTestCaseRelationship> getExecutes() {
		return Optional.ofNullable(executes).orElse(Collections.emptySet());
	}
	
	public void addExecutes(TestSuiteRunExecutesTestCaseRelationship executeRelationship) {
		if (executes==null) {
			executes = new HashSet<>();
		}
		executes.add(executeRelationship);
	}

}
