package com.akl.tms.testsuite;

import static com.akl.tms.TmsGraphTestUtil.assertTestCase;
import static com.akl.tms.TmsGraphTestUtil.assertTestSuite;
import static com.akl.tms.TmsGraphTestUtil.assertTestSuiteRun;
import static com.akl.tms.TmsGraphTestUtil.info;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.ogm.testutil.MultiDriverTestClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.akl.tms.TmsGraphTestUtil;
import com.akl.tms.testcase.TestCase;
import com.akl.tms.testsuiterun.RunState;
import com.akl.tms.testsuiterun.TestSuiteRun;




@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSuiteRepositoryTest extends MultiDriverTestClass {

	@Autowired
	private TestSuiteRepository testSuiteRepository;

	
	@Before
	public void init() {
		TmsGraphTestUtil.clearWholeGraph( getGraphDatabaseService() );
	}
	
	
	@Test
	public void createTestSuite() {
		// new with name
		{
			TestSuite ts = new TestSuite("TestSuiteA");
			TestSuite tsa = testSuiteRepository.save(ts);
			info("newly created: "+tsa);
			assertTestSuite(tsa, "TestSuiteA", null, 0, 0);
		}
		// new with name, description
		{
			TestSuite ts = new TestSuite("TestSuiteB");
			ts.setDescription("DescriptionB");
			TestSuite tsb = testSuiteRepository.save(ts);
			info("newly created: "+tsb);
			assertTestSuite(tsb, "TestSuiteB", "DescriptionB", 0, 0);
		}
		// new with name, 1x TestSuiteRun
		{
			long start = System.currentTimeMillis();
			TestSuiteRun tsr = new TestSuiteRun(new Date(start), RunState.FINISHED);
			tsr.setDurationMillis(42);
			TestSuite ts = new TestSuite("TestSuiteC");
			ts.addTestSuiteRun(tsr);
			TestSuite tsc = testSuiteRepository.save(ts);
			info("newly created: "+tsc);
			info("newly created: "+tsr);
			assertTestSuite(tsc, "TestSuiteC", null, 0, 1);
			assertTestSuiteRun(tsr, new Date(start), 42, RunState.FINISHED, 0);
		}
		// new with name, 1x TestCase
		{
			TestSuite ts = new TestSuite("TestSuiteD");
			TestCase tc = new TestCase("TestCaseD1", ts);
			ts.addTestCase(tc);
			TestSuite tsd = testSuiteRepository.save(ts);
			info("newly created: "+tsd);
			info("newly created: "+tc);
			assertTestSuite(tsd, "TestSuiteD", null, 1, 0);
			assertTestCase(tc, "TestCaseD1", null, 0);
		}
		
		TmsGraphTestUtil.dumpWholeGraph( getGraphDatabaseService() );
	}
	
	
}
