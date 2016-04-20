package ru.compscicenter.projects.lunch.web.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import ru.compscicenter.projects.lunch.web.dao.MenuDAO;
import ru.compscicenter.projects.lunch.web.model.MenuDBModel;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;

import java.util.Calendar;
import java.util.List;

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
        List<MenuDBModel> menus = session.createCriteria(MenuDBModel.class).list();
        return menus;
    }

    @Override
    public List<MenuDBModel> getAllForDates(final Calendar start, final Calendar end) {
        Session session = factory.getCurrentSession();
        Criteria criteria = session.createCriteria(MenuDBModel.class).add(Restrictions.between("date", start, end));
        List<MenuDBModel> menus = criteria.list();
        return menus;
    }

    @Override
    public MenuDBModel getForDate(Calendar day) {
        Session session = factory.getCurrentSession();
        Criteria criteria = session.createCriteria(MenuDBModel.class).add(Restrictions.eq("date", day));
        List<MenuDBModel> menus = criteria.list();
        if (menus != null && menus.size() >= 1) {
            return menus.get(0);
        }
        return null;
    }

    @Override
    public List<MenuItemDBModel> getAllItems() {
        Session session = factory.getCurrentSession();
        List<MenuItemDBModel> result = session.createCriteria(MenuItemDBModel.class).list();
        if (result != null) {
            return result;
        } else {
            return null;
        }
    }

    @Override
    public boolean contains(Calendar calendar) {
        //TODO: optimize
        Session session = factory.getCurrentSession();
        Criteria criteria = session.createCriteria(MenuDBModel.class).add(Restrictions.eq("date", calendar));
        List<MenuDBModel> menus = criteria.list();
        if (menus != null && menus.size() >= 1) {
            return true;
        }
        return false;
    }

    @Override
    public MenuItemDBModel getForNameAndPrice(String name, double price) {
        Session session = factory.getCurrentSession();
        Criteria criteria = session.createCriteria(MenuItemDBModel.class).add(Restrictions.eq("name", name)).add(Restrictions.eq("price", price));
        List<MenuItemDBModel> menuItems = criteria.setMaxResults(1).list();
        if (menuItems != null && menuItems.size() >= 1) {
            return menuItems.get(0);
        }
        return null;
    }
}
