import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:application-context-test.xml"})

public class MenuDAOTest extends Assert{
    @Test
    public void test() {

    }
}
