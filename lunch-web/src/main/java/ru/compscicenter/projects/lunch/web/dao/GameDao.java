package ru.compscicenter.projects.lunch.web.dao;

import ru.compscicenter.projects.lunch.web.model.Game;
import ru.compscicenter.projects.lunch.web.model.UserDBModel;

import javax.annotation.Nullable;
import java.util.List;

public interface GameDao {
    List<Game> getFinished(UserDBModel userDBModel);

    List<Game> getUnfinished(UserDBModel user);

    void addOrUpdate(Game game);

    @Nullable
    Game getById(long id);
}
