package ru.compscicenter.projects.lunch.web.service;

import ru.compscicenter.projects.lunch.web.exception.GameUpdatingException;
import ru.compscicenter.projects.lunch.web.model.Game;

public interface GameService {
    Game getNextGame(long userId);

    void setResult(long userId, long gameId, long winnerId) throws GameUpdatingException;
}
