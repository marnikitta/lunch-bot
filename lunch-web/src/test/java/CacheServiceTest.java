import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.estimator.DeciderException;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;
import ru.compscicenter.projects.lunch.web.service.CacheService;

import javax.annotation.Resource;
import java.util.Map;

@Test
@ContextConfiguration(locations = {"classpath:/application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager")
public class CacheServiceTest extends AbstractTestNGSpringContextTests {

    @Resource(name = "cacheService")
    private CacheService cacheService;

    @Test(groups = "firstAttempt", timeOut = 500, dependsOnGroups = "fillingDB")
    public void firstAttempt() throws DeciderException {
        Map<MenuItemDBModel, Integer> map = cacheService.getClusters();
        Assert.assertEquals(map.size(), 118);
    }

    @Test(dependsOnGroups = "firstAttempt", timeOut = 50)
    public void secondAttempt() throws DeciderException {
        Map<MenuItemDBModel, Integer> map = cacheService.getClusters();
        Assert.assertEquals(map.size(), 118);
    }
}
