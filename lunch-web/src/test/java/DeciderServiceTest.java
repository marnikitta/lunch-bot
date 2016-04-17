import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.web.exception.NoMenuForDateException;
import ru.compscicenter.projects.lunch.web.exception.NoSuchUserException;
import ru.compscicenter.projects.lunch.web.service.DeciderService;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Test
@ContextConfiguration(locations = {"classpath:/application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager")
public class DeciderServiceTest extends AbstractTestNGSpringContextTests {

    @Resource(name = "deciderService")
    private DeciderService deciderService;

    @Test(dependsOnGroups = "creatingUsers")
    public void makeMenuForDay() {
        final long id = 1;
        final Calendar calendar = new GregorianCalendar(2016, 2, 2);

        List<MenuItem> items = deciderService.getForDate(id, calendar);
        Assert.assertNotNull(items);
        Assert.assertEquals(items.size(), 5);
    }

    @Test(dependsOnGroups = "creatingUsers", expectedExceptions = NoMenuForDateException.class)
    public void getForWrongDate() {
        final long id = 1;
        final Calendar calendar = new GregorianCalendar(2016, 2, 5);
        List<MenuItem> items = deciderService.getForDate(id, calendar);
    }

    @Test(dependsOnGroups = "creatingUsers", expectedExceptions = NoSuchUserException.class)
    public void getForWrongUser() {
        final long id = 4;
        final Calendar calendar = new GregorianCalendar(2016, 2, 5);
        List<MenuItem> items = deciderService.getForDate(id, calendar);
    }
}
