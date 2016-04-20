import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.estimator.MenuXmlParser;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.web.exception.MenuDuplicateException;
import ru.compscicenter.projects.lunch.web.exception.MenuUploadingException;
import ru.compscicenter.projects.lunch.web.service.MenuService;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.PatternSyntaxException;

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
        Assert.assertEquals(menus.size(), 4);
    }

    @Test(dependsOnGroups = {"fillingDB"})
    public void getAllForDatesTest() {
        List<Menu> menus = menuService.getAllForDates(new GregorianCalendar(2016, 1, 1), new GregorianCalendar(2016, 1, 2));
        Assert.assertEquals(menus.size(), 2);

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

    @Test(groups = {"uploadTest", "fillingDB"})
    public void uploadTest() throws MenuUploadingException, MenuDuplicateException {
        InputStream inputStream = MenuServiceTest.class.getResourceAsStream("pdf/11022016.pdf");
        menuService.upload(inputStream);
        Assert.assertTrue(menuService.contains(new GregorianCalendar(2016, 1, 11)));
    }

    @Test(dependsOnGroups = "uploadTest", expectedExceptions = MenuDuplicateException.class)
    public void failedUpload() throws MenuUploadingException, MenuDuplicateException {
        InputStream inputStream = MenuServiceTest.class.getResourceAsStream("pdf/11022016.pdf");
        menuService.upload(inputStream);
    }

    @Test(dependsOnGroups = "fillingDB")
    public void getForNameExistsTest() {
        MenuItem item = menuService.getForNameAndPrice("картофельное пюре", 55.0);
        Assert.assertNotNull(item);
        Assert.assertEquals(item.getName(), "картофельное пюре");
        Assert.assertEquals(item.getPrice(), 55.0);
    }

    @Test(dependsOnGroups = "fillingDB")
    public void getForNameNonExistsTest() {
        MenuItem item = menuService.getForNameAndPrice("асфальт", 1234);
        Assert.assertNull(item);
    }

    @Test(dependsOnGroups = "fillingDB")
    public void getForRegex() {
        List<MenuItem> items = menuService.getForNameAndPriceRegex(".*", 0, 255);
        Assert.assertTrue(items.size() > 5);

        items = menuService.getForNameAndPriceRegex("асфальт", 0, 255);
        Assert.assertEquals(items.size(), 0);

        items = menuService.getForNameAndPriceRegex(".*суп.*", 0, 255);
        Assert.assertEquals(items.size(), 4);
    }

    @Test(dependsOnGroups = "fillingDB", expectedExceptions = PatternSyntaxException.class)
    public void getForWrongRegex() {
        List<MenuItem> items = menuService.getForNameAndPriceRegex("[*", 0, 255);
    }
}
