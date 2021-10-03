package com.akl.tms.tds.service.model;

import lombok.Getter;
import lombok.ToString;


@SuppressWarnings("serial")
@ToString
public class ImportException extends Exception {

	@Getter
	private final ImportError importError;
	
	
	public ImportException(ImportError importError, String msg) {
		super(msg);
		this.importError = importError;
	}
	
}
