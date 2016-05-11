package ru.compscicenter.projects.lunch.web.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import ru.compscicenter.projects.lunch.web.dao.MenuDAO;
import ru.compscicenter.projects.lunch.web.model.MenuDBModel;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;

import javax.annotation.Nullable;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("unchecked")
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
    public List<MenuDBModel> getAll() {
        Session session = factory.getCurrentSession();
        return session.createCriteria(MenuDBModel.class).list();
    }

    @Override
    public List<MenuDBModel> getAllForDates(final Calendar start, final Calendar end) {
        Session session = factory.getCurrentSession();
        Criteria criteria = session.createCriteria(MenuDBModel.class).add(Restrictions.between("date", start, end));
        return criteria.list();
    }

    @Override
    @Nullable
    public MenuDBModel getForDate(final Calendar day) {
        Session session = factory.getCurrentSession();
        Criteria criteria = session.createCriteria(MenuDBModel.class).add(Restrictions.eq("date", day));
        List<MenuDBModel> menus = criteria.list();
        if (menus != null && menus.size() >= 1) {
            return menus.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public List<MenuItemDBModel> getAllItems() {
        Session session = factory.getCurrentSession();
        return session.createCriteria(MenuItemDBModel.class).list();
    }

    @Override
    public boolean contains(final Calendar calendar) {
        Session session = factory.getCurrentSession();
        Criteria criteria = session.createCriteria(MenuDBModel.class).add(Restrictions.eq("date", calendar)).setProjection(Projections.rowCount());
        Number count = (Number) criteria.uniqueResult();
        return count.intValue() != 0;
    }

    @Override
    @Nullable
    public MenuItemDBModel getForName(final String name) {
        Session session = factory.getCurrentSession();
        Criteria criteria = session.createCriteria(MenuItemDBModel.class).add(Restrictions.eq("name", name));
        List<MenuItemDBModel> menuItems = criteria.setMaxResults(1).list();
        if (menuItems != null && menuItems.size() >= 1) {
            return menuItems.get(0);
        }
        return null;
    }
}