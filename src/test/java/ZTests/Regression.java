package ZTests;
import org.junit.jupiter.api.SelectClasses;
import org.junit.jupiter.api.Suite;

@Suite
@SelectClasses({MyFeatureTest.class, AnotherFeatureTest.class})
public class Regression {
}
