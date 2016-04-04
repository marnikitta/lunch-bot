package ru.compscicenter.projects.lunch.db.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.compscicenter.projects.lunch.db.model.MenuDBModel;

public class MenuDAOImpl implements MenuDAO {

    private SessionFactory factory;

    public void setFactory(final SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void saveOrUpdate(final MenuDBModel menu) {
        Session session = factory.getCurrentSession();
        session.saveOrUpdate(menu);
    }

    @Override
    public MenuDBModel load(long id) {
        Session session = factory.getCurrentSession();
        Object menu = session.load(MenuDBModel.class, id);
        if (menu instanceof MenuDBModel) {
            return (MenuDBModel) menu;
        } else {
            return null;
        }
    }
}
