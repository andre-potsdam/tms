package com.akl.tms.tds.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akl.tms.tds.repo.model.RunState;
import com.akl.tms.tds.repo.model.TestCase;
import com.akl.tms.tds.repo.model.TestState;
import com.akl.tms.tds.repo.model.TestSuite;
import com.akl.tms.tds.repo.model.TestSuiteRun;
import com.akl.tms.tds.repo.model.TestSuiteRunExecutesTestCaseRelationship;



@Component
public class TdsGraphTestUtil {

	@Autowired
	TestSuiteRepository testSuiteRepository;

	@Autowired
	TestSuiteRunRepository testSuiteRunRepository;


	// -----------------------------------------

	static public void info(String msg) {
		System.out.println("INFO: "+msg);
	}


	// ----------------------------------------------------------------
	
	public void assertTestSuite(String name, String description) {
		TestSuite ts = testSuiteRepository.findByName(name);
		assertNotNull("no TestSuite for name "+name, ts);
		assertEquals(description, ts.getDescription());
	}
	
	public void assertTestSuiteRun(String testSuiteName, Date start, long durationMillis, RunState runState) {
		TestSuite ts = testSuiteRepository.findByName(testSuiteName);
		assertNotNull("no TestSuite for name "+testSuiteName, ts);
		assertNotNull("no runs for: "+ts, ts.getTestSuiteRuns());
		TestSuiteRun run = null;
		for (TestSuiteRun tmpRun : ts.getTestSuiteRuns()) {
			if (tmpRun.getStart().equals(start)) {
				run = tmpRun;
				break;
			}
		}
		assertNotNull("no run found for start "+start+" in "+ts.getTestSuiteRuns(), run);
		assertEquals(durationMillis, run.getDurationMillis());
		assertEquals(runState, run.getState());
	}
	
	public void assertTestCase(String testSuiteName, String testCaseName, String description) {
		TestSuite ts = testSuiteRepository.findByName(testSuiteName);
		assertNotNull("no TestSuite for name "+testSuiteName, ts);
		assertNotNull("no testcases for: "+ts, ts.getTestCases());
		TestCase tc = null;
		for (TestCase tmpTc : ts.getTestCases()) {
			if (tmpTc.getName().equals(testCaseName)) {
				tc = tmpTc;
				break;
			}
		}
		assertNotNull("no testcase found for name "+testCaseName+" in "+ts.getTestCases(), tc);
		assertEquals(description, tc.getDescription());
	}
	
	public void assertExecutes(String testSuiteName, Date start, String testCaseName, TestState testState, String testInfo) {
		TestSuite ts = testSuiteRepository.findByName(testSuiteName);
		assertNotNull("no TestSuite for name "+testSuiteName, ts);
		assertNotNull("no runs for: "+ts, ts.getTestSuiteRuns());
		TestSuiteRun run = null;
		for (TestSuiteRun tmpRun : ts.getTestSuiteRuns()) {
			if (tmpRun.getStart().equals(start)) {
				run = tmpRun;
				break;
			}
		}
		assertNotNull("no run found for start "+start+" in "+ts.getTestSuiteRuns(), run);
		
		run = testSuiteRunRepository.findOne(run.getId());   // load executes relations
		assertNotNull("no run found for id "+run.getId(), run);
		assertNotNull("no Executes relations in "+run, run.getExecutes());
		TestSuiteRunExecutesTestCaseRelationship exec = null;
		for (TestSuiteRunExecutesTestCaseRelationship tmpExec : run.getExecutes()) {
			assertNotNull("no TestCase in: "+tmpExec, tmpExec.getTestCase() );
			if (tmpExec.getTestCase().getName().equals(testCaseName)) {
				exec = tmpExec;
				break;
			}
		}
		assertNotNull("no EXECUTES relation to TestCase '"+testCaseName+"' in: "+run.getExecutes(), exec);
		assertEquals(testState, exec.getState());
		assertEquals(testInfo, exec.getInfo());
	}


	// ----------------------------------------------------------------

	static public void dumpWholeGraph( GraphDatabaseService dbService ) {
		info("---------- dump whole graph ----------");
		if (!dbService.isAvailable(1)) {
			return;
		}
		Result r = dbService.execute("MATCH (n) RETURN n");
		while (r.hasNext()) {
			info("row: "+ r.next().toString() );
		}
		info("--------------------------------------");
	}

	static public void clearWholeGraph( GraphDatabaseService dbService ) {
		info("reset neo4j");
		if (!dbService.isAvailable(1)) {
			return;
		}
		dbService.execute("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r");
	}


	// -----------------------------------------

	static public void assertTestSuite(TestSuite ts, String name, String description, int numTestCases, int numTestSuiteRuns) {
		assertNotNull(ts);
		assertNotNull(ts.getName());
		assertEquals(ts.getName(), name);
		assertEquals(ts.getDescription(), description);
		assertEquals(numTestCases, (ts.getTestCases()==null ? 0 : ts.getTestCases().size()));
		assertEquals(numTestSuiteRuns, (ts.getTestSuiteRuns()==null ? 0 : ts.getTestSuiteRuns().size()));
	}

	static public void assertTestSuiteRun(TestSuiteRun tsr, Date start, long durationMillis, RunState state, int numExecutesRelationships) {
		assertNotNull(tsr);
		assertNotNull(tsr.getStart());
		assertEquals(tsr.getStart(), start);
		assertNotNull(tsr.getState());
		assertEquals(tsr.getState(), state);
		assertEquals(tsr.getDurationMillis(), durationMillis);
		assertEquals(numExecutesRelationships, (tsr.getExecutes()==null ? 0 : tsr.getExecutes().size()));
	}

	static public void assertTestCase(TestCase tc, String name, String description, int numExecutes) {
		assertNotNull(tc);
		assertNotNull(tc.getName());
		assertEquals(tc.getName(), name);
		assertEquals(tc.getDescription(), description);
		assertEquals(numExecutes, (tc.getExecutes()==null ? 0 : tc.getExecutes().size()));
	}

}
