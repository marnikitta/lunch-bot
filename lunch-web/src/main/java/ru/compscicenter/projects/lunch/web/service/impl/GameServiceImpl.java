package ru.compscicenter.projects.lunch.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.compscicenter.projects.lunch.estimator.DeciderException;
import ru.compscicenter.projects.lunch.web.dao.GameDao;
import ru.compscicenter.projects.lunch.web.dao.MenuDAO;
import ru.compscicenter.projects.lunch.web.dao.UserDAO;
import ru.compscicenter.projects.lunch.web.model.Game;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;
import ru.compscicenter.projects.lunch.web.model.UserDBModel;
import ru.compscicenter.projects.lunch.web.service.CacheService;
import ru.compscicenter.projects.lunch.web.service.GameService;

import java.util.*;

public class GameServiceImpl implements GameService {

    private MenuDAO menuDAO;
    private UserDAO userDAO;
    private GameDao gameDao;
    private CacheService cacheService;
    private final static Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

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
    public Game getNextGame(long userId) throws DeciderException {
        UserDBModel user = userDAO.getById(userId);
        List<Game> unfinished = gameDao.getUnfinished(user);

        if (unfinished.size() != 0) {
            return makeGameFromUnfinished(unfinished);
        } else {
        }

        return null;
    }

    @Override
    public void setResult(long userId, long gameId, long winnerId) {

    }

    private Game makeGameFromUnfinished(List<Game> unfinished) throws DeciderException {
        if (unfinished.size() == 0) {
            throw new IllegalArgumentException("Unfinished size can't be zero");
        }
        Collections.shuffle(unfinished);
        Game newGame = refreshGame(unfinished.get(0));
        gameDao.addGame(newGame);

        return newGame;
    }

    private Game refreshGame(Game game) throws DeciderException {
        if (game.getWinner() != null) {
            throw new IllegalStateException("Modifying finished game");
        }

        Map<MenuItemDBModel, Integer> myMap = cacheService.getClusters();
        Map<Integer, List<MenuItemDBModel>> buckets = reverseMap(myMap);

        if (myMap.containsKey(game.getFirst())) {
            MenuItemDBModel item = game.getFirst();
            int clust = myMap.get(item);
            List<MenuItemDBModel> items = buckets.get(clust);
            Collections.shuffle(items);
            game.setFirst(items.get(0));
        } else {
            logger.error("Cached map should contain item");
        }

        if (myMap.containsKey(game.getSecond())) {
            MenuItemDBModel item = game.getSecond();
            int clust = myMap.get(item);
            List<MenuItemDBModel> items = buckets.get(clust);
            Collections.shuffle(items);
            game.setSecond(items.get(0));
        } else {
            logger.error("Cached map should contain item");
        }

        return game;
    }

    private static <T> Map<Integer, List<T>> reverseMap(Map<T, Integer> map) {
        Map<Integer, List<T>> reversed = new HashMap<>();
        map.entrySet().stream().forEach(tIntegerEntry -> {
            List<T> newList = new ArrayList<T>();
            newList.add(tIntegerEntry.getKey());
            reversed.merge(tIntegerEntry.getValue(), newList, (oldV, newV) -> {
                oldV.addAll(newV);
                return oldV;
            });
        });
        return reversed;
    }
}
