package com.example.demo.service;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class LogParserServiceTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Autowired
	private LogParserServies parser;
	
	@Test(expected = FileNotFoundException.class) 
	public void openLogTest() {
		String fakeFileName = "PriceOfButter";
		thrown.expect(FileNotFoundException.class);
		parser.openLog(fakeFileName);
	}

}
