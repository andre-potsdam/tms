package com.akl.tms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.ogm.testutil.GraphTestUtils;

import com.akl.tms.tds.repo.model.RunState;
import com.akl.tms.tds.repo.model.TestCase;
import com.akl.tms.tds.repo.model.TestSuite;
import com.akl.tms.tds.repo.model.TestSuiteRun;

public class TdsGraphTestUtil {

	
	
	// -----------------------------------------
	
	static public void info(String msg) {
		System.out.println("INFO: "+msg);
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

	
	// ----------------------------------------------------------------
	
	static public void dumpWholeGraph( GraphDatabaseService dbService ) {
		info("---------- dump whole graph ----------");
//		for (Node n : GraphTestUtils.allNodes(dbService)) {
//			info(n.toString());
//		}
		info("--------------------------------------");
	}

	static public void clearWholeGraph( GraphDatabaseService dbService ) {
		info("---------- clear whole graph ----------");
		if (!dbService.isAvailable(1)) {
			return;
		}
		dbService.execute("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r");
	}

}
