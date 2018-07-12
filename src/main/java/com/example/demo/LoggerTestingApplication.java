package com.example.demo;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

import com.example.demo.singleton.LoggerSingleton;

@SpringBootApplication
public class LoggerTestingApplication {

	final static Logger logger = LoggerSingleton.getLoggerOBJ().getLoggerman();
	
	public static void main(String[] args) {
		SpringApplication.run(LoggerTestingApplication.class, args);
		
		
		logger.info("--------------------------------------------------------");
		logger.info("|******************************************************|");
		logger.info("|***xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx***|");
		logger.info("|***xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx***|");
		logger.info("|***xxxxxxxxxxx Application Has Started xxxxxxxxxxxx***|");
		logger.info("|***xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx***|");
		logger.info("|***xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx***|");
		logger.info("|******************************************************|");
		logger.info("--------------------------------------------------------");
		
		
	}

}
