package ru.compscicenter.projects.lunch.web.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import ru.compscicenter.projects.lunch.web.dao.GameDao;
import ru.compscicenter.projects.lunch.web.model.Game;
import ru.compscicenter.projects.lunch.web.model.UserDBModel;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("unchecked")
public class GameDaoImpl implements GameDao {

    private SessionFactory factory;

    public void setFactory(final SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<Game> getUnfinished(final UserDBModel user) {
        Session session = factory.getCurrentSession();
        return session.createCriteria(Game.class).
                add(Restrictions.eq("user", user)).
                add(Restrictions.isNull("winner")).
                list();
    }

    @Override
    public void addOrUpdate(final Game game) {
        Session session = factory.getCurrentSession();
        session.saveOrUpdate(game);
    }

    @Override
    @Nullable
    public Game getById(final long id) {
        Session session = factory.getCurrentSession();
        Object o = session.get(Game.class, id);
        if (o != null) {
            return (Game) o;
        }
        return null;
    }

    public List<Game> getFinished(final UserDBModel user) {
        Session session = factory.getCurrentSession();
        return session.createCriteria(Game.class).
                add(Restrictions.eq("user", user)).
                add(Restrictions.isNotNull("winner")).
                list();
    }
}
