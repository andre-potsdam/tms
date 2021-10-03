package com.akl.tms.tds.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akl.tms.tds.Logger;
import com.akl.tms.tds.repo.TestSuiteRepository;
import com.akl.tms.tds.repo.TestSuiteRunRepository;
import com.akl.tms.tds.repo.model.RunState;
import com.akl.tms.tds.repo.model.TestCase;
import com.akl.tms.tds.repo.model.TestState;
import com.akl.tms.tds.repo.model.TestSuite;
import com.akl.tms.tds.repo.model.TestSuiteRun;
import com.akl.tms.tds.repo.model.TestSuiteRunExecutesTestCaseRelationship;
import com.akl.tms.tds.service.model.ImportError;
import com.akl.tms.tds.service.model.ImportException;
import com.akl.tms.tds.service.model.TestSuiteRunData;

@Service
public class DataImportService {

	@Autowired
	Logger logger;
	
	@Autowired
	private TestSuiteRepository testSuiteRepository;

	@Autowired
	private TestSuiteRunRepository testSuiteRunRepository;

	
	/**
	 * Import all data of a previously executed test suite run.
	 * Data are provided in a compact format by external services.
	 * @throws Exception
	 */
	public void importTestSuiteRunData(TestSuiteRunData data) throws ImportException {
		logger.info("start importing data: "+data);
		
		// check mandatory data
		if (data.testSuiteName==null || data.testSuiteName.trim().length()==0) {
			throw new ImportException(ImportError.DATA_ERROR, "import attribute 'testSuiteName' is missing");
		}
		if (data.start==null) {
			throw new ImportException(ImportError.DATA_ERROR, "import attribute 'start' is missing");
		}
		if (data.state==null) {
			throw new ImportException(ImportError.DATA_ERROR, "");
		}
		RunState state = null;
		try {
			state = RunState.valueOf(data.state);
		}
		catch(Throwable t) {
			throw new ImportException(ImportError.DATA_ERROR, 
					"import attribute 'state' has value '"+data.state+"', but must be one of "+Arrays.toString(RunState.values()));
		}
		if (data.testcases==null || data.testcases.length==0) {
			logger.warning("import data have no testcases: "+data);
		}
		

		// ensure TestSuite (depth 1)
		TestSuite testSuite = testSuiteRepository.findByName( data.testSuiteName );
		if (testSuite==null) {
			// --> new TestSuite
			testSuite = new TestSuite( data.testSuiteName );
			logger.info("newly created: "+testSuite);
		}
		else {
			logger.debug("testsuite already existing: "+testSuite);
		}
		
		
		// ensure TestSuiteRun (depth 1)
		TestSuiteRun run = null;
		for (TestSuiteRun tmpRun : testSuite.getTestSuiteRuns()) {
			if (tmpRun.getStart().equals( data.start )) {
				run = testSuiteRunRepository.findOne( tmpRun.getId() );
				break;
			}
		}
		if (run==null) {
			// --> new TestSuiteRun
			run = new TestSuiteRun(data.start, state);
			logger.info("newly created: "+run);
		}
		else {
			// --> overwrite existing TestSuiteRun
			logger.warning("duplicate import detected, previous data will be overwritten: "+run);
		}
		run.setState(state);
		run.setDurationMillis(data.durationMillis);
		run.clearExecutes();
		
		// ensure TestSuite--HAS-->TestSuiteRun
		testSuite.addTestSuiteRun(run);
		run.setTestSuite(testSuite);
		
		
		// ensure TestCase
		// ensure TestSuite--CONTAINS-->TestCase
		Map<String,TestCase> testCaseMap = new HashMap<>();
		if (data.testcases!=null && data.testcases.length>0) {
			for (TestCase testCase : testSuite.getTestCases()) {
				testCaseMap.put( testCase.getName(), testCase);
			}
			for (Object[] testCaseData : data.testcases) {
				String testCaseName = getTestCaseName(testCaseData);
				TestCase testCase = testCaseMap.get(testCaseName);
				if (testCase==null) {
					// --> create new TestCase
					testCase = new TestCase(testCaseName, testSuite);
					testCaseMap.put( testCase.getName(), testCase);
					testSuite.addTestCase(testCase);
					logger.info("newly created: "+testCase);
				}
			}
		}

		
		// ensure TestSuiteRun--EXECUTES-->TestCase
		if (data.testcases!=null && data.testcases.length>0) {
			for (Object[] testCaseData : data.testcases) {
				String testCaseName = getTestCaseName(testCaseData);
				TestCase testCase = testCaseMap.get(testCaseName);
				TestSuiteRunExecutesTestCaseRelationship executes = new TestSuiteRunExecutesTestCaseRelationship(
						getTestCaseState(testCaseData) );
				executes.setInfo( getTestCaseInfo(testCaseData) );
				executes.setTestSuiteRun(run);
				executes.setTestCase(testCase);
				run.addExecutes(executes);
				testCase.addExecutes(executes);
			}
		}

		
		// save whole graph in one transaction
		testSuiteRepository.save(testSuite);
		
		// ensure consistency ???
		
		logger.info("data imported successfully: "+data);
	}
	
	
	private String getTestCaseName(Object[] testCaseData) throws ImportException {
		if (testCaseData==null || testCaseData.length < 1) {
			throw new ImportException(ImportError.DATA_ERROR, "empty testcase data");
		}
		if (testCaseData[0]==null) {
			throw new ImportException(ImportError.DATA_ERROR, "testcase attribute 'name' is empty");
		}
		if (!(testCaseData[0] instanceof String)) {
			throw new ImportException(ImportError.DATA_ERROR, "testcase attribute 'name' has unexpected type: "+testCaseData[0].getClass().getName());
		}
		String testCaseName = ((String) testCaseData[0]).trim();
		if (testCaseName.length()==0) {
			throw new ImportException(ImportError.DATA_ERROR, "testcase attribute 'name' has length 0");
		}
		return testCaseName;
	}
	
	
	private TestState getTestCaseState(Object[] testCaseData) throws ImportException {
		if (testCaseData==null || testCaseData.length < 2) {
			throw new ImportException(ImportError.DATA_ERROR, "testcase attribute 'state' is missing");
		}
		if (testCaseData[1]==null) {
			throw new ImportException(ImportError.DATA_ERROR, "testcase attribute 'state' is empty");
		}
		if (!(testCaseData[1] instanceof Integer)) {
			throw new ImportException(ImportError.DATA_ERROR, "testcase attribute 'state' has unexpected type (must be Integer)): "+testCaseData[1].getClass().getName());
		}
		int stateNumber = (Integer) testCaseData[1];
		switch (stateNumber) {
		case 1: return TestState.SUCCESSFUL;
		case 2: return TestState.ERROR;
		default: throw new ImportException(ImportError.DATA_ERROR, "testcase attribute 'state' has unexpected value (must be 1 or 2): "+stateNumber);
		}
	}
	
	
	private String getTestCaseInfo(Object[] testCaseData) throws ImportException {
		if (testCaseData==null || testCaseData.length < 3) {
			throw new ImportException(ImportError.DATA_ERROR, "empty testcase data");
		}
		if (testCaseData[2]==null) {
			return null;
		}
		if (!(testCaseData[2] instanceof String)) {
			throw new ImportException(ImportError.DATA_ERROR, "testcase attribute 'info' has unexpected type (must be String): "+testCaseData[2].getClass().getName());
		}
		String testCaseInfo = ((String) testCaseData[2]).trim();
		if (testCaseInfo.length()==0) {
			return null;
		}
		return testCaseInfo;
	}

}
