package ru.compscicenter.projects.lunch.web.service.impl;

import ru.compscicenter.projects.lunch.web.dao.GameDao;
import ru.compscicenter.projects.lunch.web.dao.MenuDAO;
import ru.compscicenter.projects.lunch.web.dao.UserDAO;
import ru.compscicenter.projects.lunch.web.model.Game;
import ru.compscicenter.projects.lunch.web.model.UserDBModel;
import ru.compscicenter.projects.lunch.web.service.CacheService;
import ru.compscicenter.projects.lunch.web.service.GameService;

import java.util.List;

public class GameServiceImpl implements GameService {

    private MenuDAO menuDAO;
    private UserDAO userDAO;
    private GameDao gameDao;
    private CacheService cacheService;

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setUserDAO(UserDAO userDAO) {

        this.userDAO = userDAO;
    }

    public void setMenuDAO(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    @Override
    public Game getNextGame(long userId) {
        UserDBModel userDBModel = userDAO.getById(userId);
        List<Game> unfinished = gameDao.getUnfinished(userDBModel);

        if (unfinished.size() != 0) {

        }
        return null;
    }

    @Override
    public void setResult(long userId, long gameId, long winnerId) {

    }

    private Game makeGameFromUnfinished(List<Game> unfinished) {
        if (unfinished.size() == 0) {
            throw new IllegalArgumentException("Unfinished size can't be zero");
        }

        for (Game game : unfinished) {

        }
        return null;
    }

}
