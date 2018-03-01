package com.akl.tms.tds.testsuiterun;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.akl.tms.tds.testcase.TestCase;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RelationshipEntity(type = "executes")
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"testSuiteRun","testCase"})
@ToString
public class TestSuiteRunExecutesTestCaseRelationship {

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
	@NonNull
	private String info;
	
}
