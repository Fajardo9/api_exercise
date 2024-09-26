package com.globant.api_tae.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import static com.globant.api_tae.utils.Constants.*;

/**
 * TestRunner class to execute all the test selected by it&acute;s tag.
 *
 * <p> This class runs the test based on the tags provided,ensuring that specific sets of test are executed as needed.
 * The current tags available are:</p>
 * <ul>
 *     <li>{@code @smoke} -  executes all the available test to validate the basic functionalities</li>
 *     <li>{@code @clientChangingNumber} - executes the test related to the client number changes</li>
 *     <li>{@code @clientManagement} -  executes the tests related to the management of clients</li>
 *     <li>{@code @resourceUpdate} -  execute the test related to the resource changes</li>
 *      <li>{@code @resourceManagement} -  executes the tests related to the management of resources</li>
 * </ul>
 */

@RunWith(Cucumber.class)
@CucumberOptions(
        features = FEATURES_PATH,
        glue = "com.globant.api_tae.stepDefinitions",
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        monochrome = true,
        tags = "@smoke",
        plugin = {
                PRETTY_PLUGIN,
                HTML_PLUGIN,
                JSON_PLUGIN
        }
)
public class TestRunner {

}
