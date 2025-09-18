package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "steps",
        tags = "@login or @profile or @family or @fans or @locks or @analytics or @help or @logout",
        plugin = {
                "pretty",
                "html:target/cucumber-reports",
                "json:target/cucumber.json"
        },
        monochrome = true
)
public class TestRunner {
    // Runs all tagged scenarios
}