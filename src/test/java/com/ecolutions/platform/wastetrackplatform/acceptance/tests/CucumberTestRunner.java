package com.ecolutions.platform.wastetrackplatform.acceptance.tests;

import io.cucumber.junit.platform.engine.Cucumber;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@Suite
@IncludeEngines("cucumber")  // Indica que use el engine de Cucumber
@SelectClasspathResource("features")  // Ruta a tus archivos .feature
public class CucumberTestRunner {
}