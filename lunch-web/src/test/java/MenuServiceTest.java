import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.estimator.MenuXmlParser;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.web.exception.MenuDuplicateException;
import ru.compscicenter.projects.lunch.web.service.MenuService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Test
@ContextConfiguration(locations = {"classpath:/application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager")
public class MenuServiceTest extends AbstractTestNGSpringContextTests {

    @Resource(name = "menuService")
    private MenuService menuService;

    private List<Menu> list = new ArrayList<>();

    @BeforeGroups(groups = "fillingDB")
    public void loadExamples() throws Exception {
        list.add(MenuXmlParser.parseMenu(MenuServiceTest.class.getResourceAsStream("xml/01022016menu.xml")).get(0));
        list.add(MenuXmlParser.parseMenu(MenuServiceTest.class.getResourceAsStream("xml/02022016menu.xml")).get(0));
        list.add(MenuXmlParser.parseMenu(MenuServiceTest.class.getResourceAsStream("xml/02032016menu.xml")).get(0));
    }

    @Test(groups = "fillingDB")
    public void saveTest() throws MenuDuplicateException {
        menuService.saveAll(list);
    }

    @Test(dependsOnGroups = {"fillingDB"})
    public void getAllTest() {
        List<Menu> menus = menuService.getAll();
        Assert.assertEquals(menus.size(), 3);
    }

    @Test(dependsOnGroups = {"fillingDB"})
    public void getAllForDatesTest() {
        List<Menu> menus = menuService.getAllForDates(new GregorianCalendar(2016, 1, 1), new GregorianCalendar(2016, 1, 2));
        Assert.assertEquals(menus.size(), 2);

        menus = menuService.getAllForDates(new GregorianCalendar(2016, 1, 1), new GregorianCalendar(2016, 2, 2));
        Assert.assertEquals(menus.size(), 3);

        menus = menuService.getAllForDates(new GregorianCalendar(2016, 1, 1), new GregorianCalendar(2016, 0, 2));
        Assert.assertEquals(menus.size(), 0);
    }

    @Test(dependsOnGroups = {"fillingDB"})
    public void checkMenuItems() {
        Menu menu = menuService.getForDate(new GregorianCalendar(2016, 1, 1));
        Assert.assertEquals(menu.size(), 29);
    }

    @Test(dependsOnGroups = {"fillingDB"}, expectedExceptions = MenuDuplicateException.class)
    public void saveCopy() throws MenuDuplicateException {
        menuService.saveAll(list);
    }
}
