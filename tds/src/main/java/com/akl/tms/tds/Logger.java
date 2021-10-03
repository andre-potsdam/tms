package com.akl.tms.tds;

import org.springframework.stereotype.Component;

@Component
public class Logger {

	public void error(String msg) {
		System.out.println("ERROR: "+msg);
	}

	public void warning(String msg) {
		System.out.println("WARN: "+msg);
	}

	public void info(String msg) {
		System.out.println("INFO: "+msg);
	}
	
	public void debug(String msg) {
		System.out.println("DEBUG: "+msg);
	}
	
}
