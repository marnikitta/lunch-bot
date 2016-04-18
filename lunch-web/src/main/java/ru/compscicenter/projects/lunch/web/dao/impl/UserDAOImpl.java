package ru.compscicenter.projects.lunch.web.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import ru.compscicenter.projects.lunch.web.dao.UserDAO;
import ru.compscicenter.projects.lunch.web.model.UserDBModel;

import java.util.List;

public class UserDAOImpl implements UserDAO {

    private SessionFactory factory;

    public void setFactory(final SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public UserDBModel getById(long id) {
        Session session = factory.getCurrentSession();
        UserDBModel userDBModel = (UserDBModel) session.load(UserDBModel.class, id);
        return userDBModel;
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
        List<UserDBModel> list = session.createCriteria(UserDBModel.class).add(Restrictions.eq("id", id)).list();
        if (list == null || list.size() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
