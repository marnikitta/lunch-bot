package ru.compscicenter.projects.lunch.web.service;

import ru.compscicenter.projects.lunch.estimator.DeciderException;
import ru.compscicenter.projects.lunch.web.model.Game;

public interface GameService {
    public Game getNextGame(long userId) throws DeciderException;

    public void setResult(long userId, long gameId, long winnerId);
}
