package suites;

import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SelectClasses;

@Suite
@SelectClasses({
    tests.kafka.KafkaTest.class,
    tests.opensearch.OpenSearchTest.class,
    tests.api.ApiTest.class
})
public class SuiteRegress {

}
