package com.example.demo.factory;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.LogEntry;
import com.example.demo.model.LogEntryError;
import com.example.demo.model.LogEntryFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntryFactoryTests {

	@Autowired
	private LogEntryFactory factory;
	
	@Test
	public void getLogEntryTest_ERROR_creation() {
		String [] error = {"2018-07-09 10:06:20,504","error","","","",""};
		assertThat(factory.getLogEntry(error , 0 ), instanceOf(LogEntry.class));
		assertThat(factory.getLogEntry(error , 0 ), instanceOf(LogEntryError.class));
	}
	
	@Test
	public void getLogEntryTest_INFO_creation() {
		String [] info = {"2018-07-09 10:06:20,504","info","","","",""};
		assertThat(factory.getLogEntry(info , 0 ), instanceOf(LogEntry.class));
		assertThat(factory.getLogEntry(info , 0 ), not(instanceOf(LogEntryError.class)));
	}
	
	@Test
	public void getLogEntryTest_WARN_creation() {
		String [] warn = {"2018-07-09 10:06:20,504","WARN","","","",""};
		assertThat(factory.getLogEntry(warn , 0 ), instanceOf(LogEntry.class));
		assertThat(factory.getLogEntry(warn , 0 ), not(instanceOf(LogEntryError.class)));
	}

	@Test
	public void getLogEntryTest_NONE_creation() {
		String [] none = {"2018-07-09 10:06:20,504","none","","","",""};
		assertThat(factory.getLogEntry(none , 0 ), nullValue());
	}


}
