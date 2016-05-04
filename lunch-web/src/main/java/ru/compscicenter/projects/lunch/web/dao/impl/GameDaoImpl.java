package ru.compscicenter.projects.lunch.web.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import ru.compscicenter.projects.lunch.web.dao.GameDao;
import ru.compscicenter.projects.lunch.web.model.Game;
import ru.compscicenter.projects.lunch.web.model.UserDBModel;

import java.util.List;

public class GameDaoImpl implements GameDao {

    private SessionFactory factory;

    public void setFactory(final SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<Game> loadAll(UserDBModel user) {
        Session session = factory.getCurrentSession();
        List<Game> games = session.createCriteria(Game.class).add(Restrictions.eq("user", user)).list();
        return games;
    }

    @Override
    public List<Game> getUnfinished(UserDBModel user) {
        Session session = factory.getCurrentSession();
        List<Game> games = session.createCriteria(Game.class).add(Restrictions.eq("user", user)).add(Restrictions.isNull("winner")).list();
        return games;
    }

    @Override
    public void addGame(Game game) {
        Session session = factory.getCurrentSession();
        session.saveOrUpdate(game);
    }
}
