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
    public void makeMenuForDay() throws NoMenuForDateException, NoSuchUserException {
        final long id = 1;
        final Calendar calendar = new GregorianCalendar(2016, 2, 2);

        List<MenuItem> items = deciderService.getForDate(id, calendar);
        Assert.assertNotNull(items);
        Assert.assertTrue(items.size() > 0);
    }

    @Test(dependsOnGroups = "creatingUsers")
    public void sumForPeriod() throws NoSuchUserException {
        final long id = 1;
        final Calendar end = new GregorianCalendar(2016, 2, 2);
        final Calendar start = new GregorianCalendar(1996, 2, 2);

        double sum = deciderService.sumForPeriod(id, start, end);
        System.out.println(sum);
        Assert.assertTrue(sum > 0);

        sum = deciderService.sumForPeriod(id, start, start);
        Assert.assertTrue(sum == 0);
    }

    @Test(dependsOnGroups = "creatingUsers", expectedExceptions = NoMenuForDateException.class)
    public void getForWrongDate() throws NoMenuForDateException, NoSuchUserException {
        final long id = 1;
        final Calendar calendar = new GregorianCalendar(2016, 2, 5);
        deciderService.getForDate(id, calendar);
    }

    @Test(dependsOnGroups = "creatingUsers", expectedExceptions = NoSuchUserException.class)
    public void getForWrongUser() throws NoMenuForDateException, NoSuchUserException {
        final long id = 404;
        final Calendar calendar = new GregorianCalendar(2016, 2, 2);
        deciderService.getForDate(id, calendar);
    }
}
