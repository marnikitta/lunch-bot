package ru.compscicenter.projects.lunch.db.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import ru.compscicenter.projects.lunch.db.dao.MenuDAO;
import ru.compscicenter.projects.lunch.db.model.MenuDBModel;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

public class MenuDAOImpl implements MenuDAO {

    private SessionFactory factory;

    public void setFactory(final SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    @Transactional
    public void saveOrUpdate(final MenuDBModel menu) {
        Session session = factory.getCurrentSession();
        session.saveOrUpdate(menu);
    }

    @Override
    @Transactional
    public MenuDBModel load(long id) {
        Session session = factory.getCurrentSession();
        Object menu = session.load(MenuDBModel.class, id);
        if (menu instanceof MenuDBModel) {
            return (MenuDBModel) menu;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public List<MenuDBModel> getAll() {
        Session session = factory.getCurrentSession();
        List<MenuDBModel> menus = session.createCriteria(MenuDBModel.class).list();
        return menus;
    }

    @Override
    @Transactional
    public List<MenuDBModel> getAllForDates(final Calendar start, final Calendar end) {
        Session session = factory.getCurrentSession();
        Criteria criteria = session.createCriteria(MenuDBModel.class).add(Restrictions.between("date", start, end));
        List<MenuDBModel> menus = criteria.list();
        return menus;
    }
}
