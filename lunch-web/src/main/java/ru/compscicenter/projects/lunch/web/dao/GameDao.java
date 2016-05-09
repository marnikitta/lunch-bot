package ru.compscicenter.projects.lunch.web.dao;

import ru.compscicenter.projects.lunch.web.model.Game;
import ru.compscicenter.projects.lunch.web.model.UserDBModel;

import java.util.List;

public interface GameDao {
    public List<Game> loadAll(UserDBModel user);

    public List<Game> getFinnished(UserDBModel userDBModel);

    public List<Game> getUnfinished(UserDBModel user);

    public void addGame(Game game);

    public Game getById(long id);
}
