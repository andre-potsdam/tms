package com.akl.tms.tds.service;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.ogm.testutil.MultiDriverTestClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.akl.tms.tds.repo.TdsGraphTestUtil;
import com.akl.tms.tds.repo.model.RunState;
import com.akl.tms.tds.repo.model.TestState;
import com.akl.tms.tds.service.model.TestSuiteRunData;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DataImportServiceTest extends MultiDriverTestClass {

	@Autowired
	private DataImportService dataImportService;

	@Autowired
	TdsGraphTestUtil graphTestUtil;
	
	
	@Before
	public void init() {
		TdsGraphTestUtil.clearWholeGraph( getGraphDatabaseService() );
	}

	
	@After
	public void after() {
		TdsGraphTestUtil.dumpWholeGraph( getGraphDatabaseService() );
	}

	
	// ------------------------- tests ------------------------
	
	@Test
	public void singleImport() throws Exception {
		TestSuiteRunData data = new TestSuiteRunData();
		data.testSuiteName = "TestSuiteA";
		data.state = RunState.FINISHED.name();
		data.start = new Date();
		data.durationMillis = 42000;
		
		Object[][] testcases = {
				{ "TestCaseA1", 1, "info for TestCaseA1" },
				{ "TestCaseA2", 2, "info for TestCaseA2" },
				{ "TestCaseA3", 1, null },
		};
		data.testcases = testcases;
				
		dataImportService.importTestSuiteRunData(data);
		
		graphTestUtil.assertTestSuite("TestSuiteA", null);
		graphTestUtil.assertTestSuiteRun("TestSuiteA", data.start, data.durationMillis, RunState.FINISHED);
		graphTestUtil.assertTestCase("TestSuiteA", "TestCaseA1", null);
		graphTestUtil.assertTestCase("TestSuiteA", "TestCaseA2", null);
		graphTestUtil.assertTestCase("TestSuiteA", "TestCaseA3", null);
		graphTestUtil.assertExecutes("TestSuiteA", data.start, "TestCaseA1", TestState.SUCCESSFUL, "info for TestCaseA1");
		graphTestUtil.assertExecutes("TestSuiteA", data.start, "TestCaseA2", TestState.ERROR, "info for TestCaseA2");
		graphTestUtil.assertExecutes("TestSuiteA", data.start, "TestCaseA3", TestState.SUCCESSFUL, null);
	}
}
