import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.web.service.UserService;

import javax.annotation.Resource;

@Test
@ContextConfiguration(locations = {"classpath:/application-context-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager")
public class UserServiceTest extends AbstractTestNGSpringContextTests {

    @Resource(name = "userService")
    private UserService userService;

    @Test(groups = "creatingUsers", dependsOnGroups = "fillingDB")
    public void creatingUsers() {
        userService.makeRandomUser(1);
        userService.makeRandomUser(2);
        userService.makeRandomUser(3);
        userService.createUser(4);
        userService.createUser(6);
    }

    @Test(dependsOnGroups = "fillingDB")
    public void resettingUsers() {
        userService.makeRandomUser(101);
        userService.reset(101);
        Assert.assertTrue(userService.exists(101));
        Assert.assertEquals(userService.getUserById(101).getLoveSet().size(), 0);
        Assert.assertEquals(userService.getUserById(101).getHateSet().size(), 0);
    }

    @Test(dependsOnGroups = "creatingUsers", expectedExceptions = org.springframework.dao.DuplicateKeyException.class)
    public void creatingDuplicate() {
        userService.makeRandomUser(1);
    }

    @Test(dependsOnGroups = "creatingUsers")
    public void gettingUsers() {
        Assert.assertNull(userService.getUserById(5));
        Assert.assertNotNull(userService.getUserById(1));
        Assert.assertNotNull(userService.getUserById(2));
        Assert.assertNotNull(userService.getUserById(3));
        Assert.assertNotNull(userService.getUserById(6));
        Assert.assertNotNull(userService.getUserById(4));
    }

    @Test(dependsOnGroups = "creatingUsers")
    public void userExists() {
        Assert.assertTrue(!userService.exists(5));
        Assert.assertTrue(!userService.exists(5));
        Assert.assertTrue(!userService.exists(123));
        Assert.assertTrue(userService.exists(1));
        Assert.assertTrue(userService.exists(2));
        Assert.assertTrue(userService.exists(2));
        Assert.assertTrue(userService.exists(6));
    }
}
