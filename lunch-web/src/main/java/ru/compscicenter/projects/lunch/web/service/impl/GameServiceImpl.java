package ru.compscicenter.projects.lunch.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.compscicenter.projects.lunch.estimator.DeciderException;
import ru.compscicenter.projects.lunch.web.dao.GameDao;
import ru.compscicenter.projects.lunch.web.dao.UserDAO;
import ru.compscicenter.projects.lunch.web.exception.GameUpdatingException;
import ru.compscicenter.projects.lunch.web.model.Game;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;
import ru.compscicenter.projects.lunch.web.model.UserDBModel;
import ru.compscicenter.projects.lunch.web.service.CacheService;
import ru.compscicenter.projects.lunch.web.service.GameService;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.IntStream;

public class GameServiceImpl implements GameService {

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

    @Override
    @Transactional
    public Game getNextGame(final long userId) {
        if (!userDAO.contains(userId)) {
            throw new IllegalStateException("DB should contain this id");
        }

        UserDBModel user = userDAO.getById(userId);
        List<Game> unfinished = gameDao.getUnfinished(user);

        if (unfinished.size() != 0) {
            return makeGameFromUnfinished(user);
        } else {
            constructGames(user);
            return makeGameFromUnfinished(user);
        }
    }

    @Override
    @Transactional
    public void setResult(final long userId, final long gameId, final long winnerId) throws GameUpdatingException {
        Game game = gameDao.getById(gameId);

        if (game == null) {
            throw new GameUpdatingException("Trying to update notExisting game (gameId=" + gameId + ")");
        }
        if (game.getUser().getId() != userId) {
            throw new GameUpdatingException("Trying to update not mine game (gameId=" + gameId + "; userId=" + userId + ")");
        }
        if (game.getWinner() != null) {
            throw new GameUpdatingException("Trying to update already finished game (gameId=" + gameId + ")");
        }
        if (game.getFirst().getId() != winnerId && game.getSecond().getId() != winnerId) {
            throw new GameUpdatingException("Trying to set wrong game (not first, not second)");
        }
        if (game.getFirst().getId() == winnerId) {
            game.setWinner(game.getFirst());
        }
        if (game.getSecond().getId() == winnerId) {
            game.setWinner(game.getSecond());
        }
        gameDao.addOrUpdate(game);


        try {
            updateLoveList(game.getUser());
        } catch (DeciderException e) {
            throw new GameUpdatingException(e);
        }
    }

    private Game makeGameFromUnfinished(final UserDBModel user) {
        List<Game> unfinished = gameDao.getUnfinished(user);

        if (unfinished.size() == 0) {
            throw new IllegalArgumentException("Unfinished size can't be zero");
        }
        Collections.shuffle(unfinished);
        Game newGame = refreshGame(unfinished.get(0));
        gameDao.addOrUpdate(newGame);

        return newGame;
    }

    private Game refreshGame(final Game game) {
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
            logger.error("Cached map should contain item {} in {}", game.getFirst(), myMap);
        }

        if (myMap.containsKey(game.getSecond())) {
            MenuItemDBModel item = game.getSecond();
            int clust = myMap.get(item);
            List<MenuItemDBModel> items = buckets.get(clust);
            Collections.shuffle(items);
            game.setSecond(items.get(0));
        } else {
            logger.error("Cached map should contain item {} in {}", game.getSecond(), myMap);
        }

        return game;
    }

    private void updateLoveList(final UserDBModel user) {
        List<MenuItemDBModel> loveList = user.getLoveList();
        loveList.clear();

        List<Game> games = gameDao.getFinished(user);
        Map<MenuItemDBModel, Integer> clusters = cacheService.getClusters();

        Map<Integer, List<MenuItemDBModel>> reversedClusters = reverseMap(clusters);
        int maxCl = reversedClusters.size();

        TreeMap<Integer, Integer> sortedClusts = new TreeMap<>();
        IntStream.range(0, maxCl).forEach(i -> sortedClusts.put(i, 0));
        games.stream().
                map(Game::getWinner).
                map(clusters::get).
                forEach(cl -> sortedClusts.merge(cl, 1, (oldV, newV) -> oldV + newV));

        NavigableSet<Integer> navigableSet = sortedClusts.descendingKeySet();

        for (Integer cl1 : navigableSet) {
            Collections.shuffle(reversedClusters.get(cl1));
            loveList.add(reversedClusters.get(cl1).get(0));
        }

        userDAO.saveOrUpdate(user);
    }


    private void constructGames(final UserDBModel userDBModel) {
        List<Game> games = gameDao.getFinished(userDBModel);
        Map<MenuItemDBModel, Integer> clusters = cacheService.getClusters();

        Map<Integer, List<MenuItemDBModel>> reversedClusters = reverseMap(clusters);
        int maxCl = reversedClusters.size();

        TreeMap<Integer, Integer> sortedClusts = new TreeMap<>();
        IntStream.range(0, maxCl).forEach(i -> sortedClusts.put(i, 0));
        games.stream().
                map(Game::getFirst).
                map(clusters::get).
                forEach(cl -> sortedClusts.merge(cl, 1, (oldV, newV) -> oldV + newV));
        games.stream().
                map(Game::getSecond).
                map(clusters::get).
                forEach(cl -> sortedClusts.merge(cl, 1, (oldV, newV) -> oldV + newV));

        NavigableSet<Integer> navigableSet = sortedClusts.navigableKeySet();

        Iterator<Integer> it = navigableSet.iterator();

        if (navigableSet.size() % 2 != 0) it.next();

        while (it.hasNext()) {
            int cl1 = it.next();
            int cl2 = it.next();

            Collections.shuffle(reversedClusters.get(cl1));
            Collections.shuffle(reversedClusters.get(cl2));

            MenuItemDBModel game1 = reversedClusters.get(cl1).get(0);
            MenuItemDBModel game2 = reversedClusters.get(cl2).get(0);
            Game g = new Game(userDBModel, game1, game2, null);
            gameDao.addOrUpdate(g);
        }
    }

    private static <T> Map<Integer, List<T>> reverseMap(final Map<T, Integer> map) {
        Map<Integer, List<T>> reversed = new HashMap<>();
        map.entrySet().stream().forEach(tIntegerEntry -> {
            List<T> newList = new ArrayList<>();
            newList.add(tIntegerEntry.getKey());
            reversed.merge(tIntegerEntry.getValue(), newList, (oldV, newV) -> {
                oldV.addAll(newV);
                return oldV;
            });
        });
        return reversed;
    }
}
