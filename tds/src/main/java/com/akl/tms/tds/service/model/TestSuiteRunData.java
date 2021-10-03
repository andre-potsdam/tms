package com.akl.tms.tds.service.model;

import java.util.Date;

import lombok.ToString;

/**
 * Used at the external interface of Test Data Service.
 * Contains all data of a previously executed test suite run.
 * 
 * Testsuite name and start of testsuite run are together a unique key!
 */
@ToString(exclude = {"testcases"})
public class TestSuiteRunData {

	/** Name of corresponding testsuite. */
	public String testSuiteName;
	
	/** Start of this testsuite run. */
	public Date start;
	
	/** Duration (ms) of this testsuite run. */
	public long durationMillis;
	
	/** State of this testsuite run. State name must correspond to enum RunState! */
	public String state;
	
	
	/**
	 * Every row contains data of a single testcase.
	 * Following data are expected:
	 * 	index 0: String:  testcase name, mandatory
	 *  index 1: INTEGER: testcase state, mandatory
	 *  				  Enum according to TestState
	 *  					1: SUCCESSFUL, 
	 *  					2: ERROR
	 *  index 2: String:  testcase info, optional
	 *  			      This may provide further details concerning the testcase execution resp. state.
	 *  				  This is NOT a general testcase description!
	 */
	public Object[][] testcases;
}
