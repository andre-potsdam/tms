package com.akl.tms.tds.repo.model;

public enum RunState {

	/** Currently running. */
	RUNNING,
	
	/** 
	 * Was running and has finished now. 
	 * This is NOT related to any test results! 
	 */
	FINISHED,
	
	/** 
	 * Was running, but execution failed somehow. 
	 * This is NOT related to any test results! 
	 */
	FAILED
}
