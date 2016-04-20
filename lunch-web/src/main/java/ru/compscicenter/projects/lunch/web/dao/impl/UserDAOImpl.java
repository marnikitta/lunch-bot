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
    public UserDBModel getById(long id) {
        Session session = factory.getCurrentSession();
        Object userDBModel = session.load(UserDBModel.class, id);
        return (UserDBModel) userDBModel;
    }

    @Override
    public void saveOrUpdate(UserDBModel userDBModel) {
        Session session = factory.getCurrentSession();
        session.saveOrUpdate(userDBModel);
    }

    @Override
    public boolean contains(long id) {
        //TODO: optimize
        Session session = factory.getCurrentSession();
        Object object = session.get(UserDBModel.class, id);
        if (object != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void delete(long id) {
        Session session = factory.getCurrentSession();
        Object userDBModel = session.get(UserDBModel.class, id);
        if (userDBModel != null) {
            session.delete((UserDBModel) userDBModel);
        }
    }
}
