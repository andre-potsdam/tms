package com.akl.tms.tds.testsuite;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/testsuite")
public class TestSuiteController {

	
	@GetMapping
	public List<TestSuite> getAll() throws Exception {
		return null;
	}
}
