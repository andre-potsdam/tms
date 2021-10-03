package com.akl.tms.tds.repo.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RelationshipEntity(type = "executes")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"testSuiteRun","testCase"})
@ToString(exclude = {"testSuiteRun","testCase"})
public class TestSuiteRunExecutesTestCaseRelationship {

	@GraphId 
	private Long id;
	
	@StartNode
	@Getter
	@Setter
	private TestSuiteRun testSuiteRun;
	
	@EndNode
	@Getter
	@Setter
	private TestCase testCase;

	@Getter
	@NonNull
	private TestState state;
	
	@Getter
	@Setter
	private String info;
	
}
