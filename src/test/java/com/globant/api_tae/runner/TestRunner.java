package com.globant.api_tae.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import java.lang.invoke.ConstantCallSite;

import static com.globant.api_tae.utils.Constants.*;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = FEATURES_PATH,
        glue = "com.globant.api_tae.stepDefinitions",
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        monochrome = true,
        tags = "@xd",
        plugin = {
                PRETTY_PLUGIN,
                HTML_PLUGIN,
                JSON_PLUGIN
        }
)
public class TestRunner {

}
