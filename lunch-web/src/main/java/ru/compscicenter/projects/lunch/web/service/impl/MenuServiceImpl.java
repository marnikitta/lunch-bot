package ru.compscicenter.projects.lunch.web.service.impl;

import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.parser.PDFToMenu;
import ru.compscicenter.projects.lunch.web.dao.MenuDao;
import ru.compscicenter.projects.lunch.web.exception.MenuDuplicateException;
import ru.compscicenter.projects.lunch.web.exception.MenuUploadingException;
import ru.compscicenter.projects.lunch.web.model.MenuDBModel;
import ru.compscicenter.projects.lunch.web.model.MenuItemDBModel;
import ru.compscicenter.projects.lunch.web.service.MenuService;
import ru.compscicenter.projects.lunch.web.util.ModelConverter;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MenuServiceImpl implements MenuService {

    private MenuDao menuDao;

    public void setMenuDao(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    @Transactional
    public void saveMenu(final Menu menu) throws MenuDuplicateException {
        if (contains(menu.getDate())) {
            throw new MenuDuplicateException("Already has entry for date: " + menu.getDate());
        }
        MenuDBModel menuDBModel = ModelConverter.menuToDBMenu(menu);
        menuDao.saveOrUpdate(menuDBModel);
    }

    @Override
    @Transactional
    public List<Menu> getAll() {
        List<MenuDBModel> list = menuDao.getAll();
        Set<Menu> result = list.stream().map(ModelConverter::dbMenuToMenu).collect(Collectors.toSet());
        return new ArrayList<>(result);
    }

    @Override
    @Transactional
    public void saveAll(final Collection<? extends Menu> coll) throws MenuDuplicateException {
        for (Menu menu : coll) {
            saveMenu(menu);
        }
    }

    @Override
    @Transactional
    public List<Menu> getAllForDates(final Calendar start, final Calendar end) {
        List<MenuDBModel> list = menuDao.getAllForDates(start, end);
        Set<Menu> result = list.stream().map(ModelConverter::dbMenuToMenu).collect(Collectors.toSet());
        return new ArrayList<>(result);
    }

    @Override
    @Transactional
    public Menu getForDate(final Calendar day) {
        MenuDBModel menuDBModel = menuDao.getForDate(day);
        if (menuDBModel != null) {
            return ModelConverter.dbMenuToMenu(menuDBModel);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public MenuItem getForName(final String name) {
        MenuItemDBModel model = menuDao.getForName(name);
        if (model != null) {
            return ModelConverter.dbMenuItemToMenuItem(model);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public List<MenuItem> getForNameRegex(final String regex) {
        Pattern pattern = Pattern.compile(regex);

        List<MenuItemDBModel> all = menuDao.getAllItems();
        Set<MenuItem> result = new HashSet<>();

        for (MenuItemDBModel model : all) {
            Matcher matcher = pattern.matcher(model.getName());
            if (matcher.matches()) {
                result.add(ModelConverter.dbMenuItemToMenuItem(model));
            }
        }
        return new ArrayList<>(result);
    }

    @Override
    @Transactional
    public boolean contains(final Calendar day) {
        return menuDao.contains(day);
    }

    @Override
    @Transactional
    public List<MenuItem> getAllItems() {
        List<MenuItemDBModel> allItems = menuDao.getAllItems();
        Set<MenuItem> result = allItems.stream().map(ModelConverter::dbMenuItemToMenuItem).collect(Collectors.toSet());

        return new ArrayList<>(result);
    }

    @Override
    public List<MenuItemDBModel> getAllDBItems() {
        return menuDao.getAllItems();
    }

    @Override
    @Transactional
    public Menu upload(final InputStream stream) throws MenuUploadingException, MenuDuplicateException {
        try {
            Menu menu = PDFToMenu.parsePDF(stream);
            saveMenu(menu);
            return menu;
        } catch (IOException e) {
            throw new MenuUploadingException("Uploading menuPattern wrong format", e);
        }
    }
}
