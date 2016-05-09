package ru.compscicenter.projects.lunch.web.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.compscicenter.projects.lunch.web.dao.UserDAO;
import ru.compscicenter.projects.lunch.web.model.UserDBModel;

public class UserDAOImpl implements UserDAO {

    private SessionFactory factory;

    public void setFactory(final SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public UserDBModel getById(final long id) {
        Session session = factory.getCurrentSession();
        Object userDBModel = session.load(UserDBModel.class, id);
        return (UserDBModel) userDBModel;
    }

    @Override
    public void saveOrUpdate(final UserDBModel userDBModel) {
        Session session = factory.getCurrentSession();
        session.saveOrUpdate(userDBModel);
    }

    @Override
    public boolean contains(final long id) {
        Session session = factory.getCurrentSession();
        Object object = session.get(UserDBModel.class, id);
        return object != null;
    }

    @Override
    public void delete(final long id) {
        Session session = factory.getCurrentSession();
        Object userDBModel = session.get(UserDBModel.class, id);
        if (userDBModel != null) {
            session.delete(userDBModel);
        }
    }
}
