import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.estimator.DeciderException;
import ru.compscicenter.projects.lunch.web.exception.GameUpdatingException;
import ru.compscicenter.projects.lunch.web.model.Game;
import ru.compscicenter.projects.lunch.web.service.GameService;
import ru.compscicenter.projects.lunch.web.service.UserService;

import javax.annotation.Resource;

@Test
@ContextConfiguration(locations = {"classpath:/application-context-test.xml"})
public class GameTest extends AbstractTestNGSpringContextTests {

    @Resource(name = "gameService")
    private GameService gameService;

    @Resource(name = "userService")
    private UserService userService;

    @Test(dependsOnGroups = {"fillingDB", "creatingUsers"})
    public void emulatingGames() throws DeciderException {
        int userId = 4;
        for (int i = 0; i < 100; ++i) {
            Game game = gameService.getNextGame(4);
            gameService.setResult(userId, game.getId(), game.getFirst().getId());
        }
        for (int i = 0; i < 100; ++i) {
            Game game = gameService.getNextGame(4);
            gameService.setResult(userId, game.getId(), game.getSecond().getId());
        }
    }

    @Test(dependsOnGroups = {"fillingDB", "creatingUsers"}, expectedExceptions = GameUpdatingException.class)
    public void updateWrongUser() throws DeciderException {
        int userId = 4;
        Game game = gameService.getNextGame(userId);
        gameService.setResult(userId + 1, game.getId(), game.getFirst().getId());
    }

    @Test(dependsOnGroups = {"fillingDB", "creatingUsers"}, expectedExceptions = GameUpdatingException.class)
    public void updateNonExistingGame() throws DeciderException {
        int userId = 4;
        Game game = gameService.getNextGame(userId);
        gameService.setResult(userId, game.getId() + 10001, game.getFirst().getId());
    }

    @Test(dependsOnGroups = {"fillingDB", "creatingUsers"}, expectedExceptions = GameUpdatingException.class)
    public void updateFinishedGame() throws DeciderException {
        int userId = 4;
        Game game = gameService.getNextGame(userId);
        gameService.setResult(userId, game.getId(), game.getFirst().getId());
        gameService.setResult(userId, game.getId(), game.getFirst().getId());
    }
}


