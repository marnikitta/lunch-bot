import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.db.service.UserService;

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
    }

    @Test(dependsOnGroups = "creatingUsers", expectedExceptions = org.springframework.dao.DuplicateKeyException.class)
    public void creatingDuplicate() {
        userService.makeRandomUser(1);
        userService.makeRandomUser(2);
        userService.makeRandomUser(3);
    }

    @Test(dependsOnGroups = "creatingUsers")
    public void gettingUsers() {
        Assert.assertNull(userService.getUserById(5));
        Assert.assertNotNull(userService.getUserById(1));
        Assert.assertNotNull(userService.getUserById(2));
        Assert.assertNotNull(userService.getUserById(3));
    }
}
