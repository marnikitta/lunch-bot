import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.db.service.MenuService;
import ru.compscicenter.projects.lunch.estimator.MenuXmlParser;
import ru.compscicenter.projects.lunch.model.Menu;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Test
@ContextConfiguration(locations = {"classpath:/application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager")
public class MenuDAOTest extends AbstractTestNGSpringContextTests {

    @Resource(name = "menuService")
    private MenuService menuService;

    private List<Menu> list = new ArrayList<>();

    @BeforeSuite
    public void loadExamples() throws Exception {
        list.add(MenuXmlParser.parseMenu(MenuDAOTest.class.getResourceAsStream("xml/01022016menu.xml")).get(0));
        list.add(MenuXmlParser.parseMenu(MenuDAOTest.class.getResourceAsStream("xml/02022016menu.xml")).get(0));
        list.add(MenuXmlParser.parseMenu(MenuDAOTest.class.getResourceAsStream("xml/02032016menu.xml")).get(0));
    }

    @Test
    public void saveTest() {
        menuService.saveAll(list);
    }

    @Test(dependsOnMethods = {"saveTest"})
    public void getAllTest() {
        List<Menu> menus = menuService.getAll();
        Assert.assertEquals(menus.size(), 3);
    }

    @Test(dependsOnMethods = {"saveTest"})
    public void getAllForDatesTest() {
        List<Menu> menus = menuService.getAllForDates(new GregorianCalendar(2016, 1, 1), new GregorianCalendar(2016, 1, 2));
        Assert.assertEquals(menus.size(), 2);

        menus = menuService.getAllForDates(new GregorianCalendar(2016, 1, 1), new GregorianCalendar(2016, 2, 2));
        Assert.assertEquals(menus.size(), 3);
    }

    @Test(dependsOnMethods = {"saveTest"})
    public void checkMenuItems() {
        Menu menu = menuService.getForDate(new GregorianCalendar(2016, 1, 1));
        Assert.assertEquals(menu.size(), 29);
    }

    @Test(dependsOnMethods = {"saveTest"}, expectedExceptions = {org.springframework.dao.DuplicateKeyException.class})
    public void saveCopy() {
        menuService.saveAll(list);
    }
}
