package com.david.scaffold.util;

import org.apache.log4j.PropertyConfigurator;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class JUnit4ClassRunner extends SpringJUnit4ClassRunner {
	static {
		String rootPath = System.getProperty("user.dir");
		PropertyConfigurator.configure(rootPath.concat("/src/main/resources/file/log4j.properties"));
	}

	public JUnit4ClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}
}
