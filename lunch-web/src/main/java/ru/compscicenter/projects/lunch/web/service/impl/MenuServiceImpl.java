package ru.compscicenter.projects.lunch.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.parser.PDFToMenu;
import ru.compscicenter.projects.lunch.web.dao.MenuDAO;
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

    private static Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    private MenuDAO menuDAO;

    public void setMenuDAO(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    @Override
    @Transactional
    public void saveMenu(final Menu menu) throws MenuDuplicateException {
        MenuDBModel menuDBModel = ModelConverter.menuToDBMenu(menu);
        if (contains(menuDBModel.getDate())) {
            logger.debug("Already has entry for date: " + menu.getDate());
            throw new MenuDuplicateException("Already has entry for date: " + menu.getDate());
        }
        menuDAO.saveOrUpdate(menuDBModel);
    }

    @Override
    @Transactional
    public List<Menu> getAll() {
        List<MenuDBModel> list = menuDAO.getAll();
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
        List<MenuDBModel> list = menuDAO.getAllForDates(start, end);
        Set<Menu> result = list.stream().map(ModelConverter::dbMenuToMenu).collect(Collectors.toCollection(LinkedHashSet::new));
        return new ArrayList<>(result);
    }

    @Override
    @Transactional
    public Menu getForDate(final Calendar day) {
        MenuDBModel menuDBModel = menuDAO.getForDate(day);
        if (menuDBModel != null) {
            return ModelConverter.dbMenuToMenu(menuDBModel);
        } else {
            logger.debug("No menuPattern for date " + day);
            return null;
        }
    }

    @Override
    @Transactional
    public MenuItem getForName(String name) {
        MenuItemDBModel model = menuDAO.getForName(name);
        if (model != null) {
            return ModelConverter.dbMenuItemToMenuItem(model);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public List<MenuItem> getForNameRegex(String regex) {
        Pattern pattern = Pattern.compile(regex);

        List<MenuItemDBModel> all = menuDAO.getAllItems();
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
    public boolean contains(Calendar day) {
        return menuDAO.contains(day);
    }

    @Override
    @Transactional
    public List<MenuItem> getAllItems() {
        List<MenuItemDBModel> allItems = menuDAO.getAllItems();
        Set<MenuItem> result = allItems.stream().map(ModelConverter::dbMenuItemToMenuItem).collect(Collectors.toSet());

        return new ArrayList<>(result);
    }

    @Override
    public List<MenuItemDBModel> getAllDBItems() {
        return menuDAO.getAllItems();
    }

    @Override
    @Transactional
    public Menu upload(final InputStream stream) throws MenuUploadingException, MenuDuplicateException {
        try {
            Menu menu = PDFToMenu.parsePDF(stream);
            logger.info(menu.getItemsCopy().get(0).getName());
            System.out.println(menu.getItemsCopy().get(0).getName());
            if (null == getForDate(menu.getDate())) {
                saveMenu(menu);
                return menu;
            } else {
                throw new MenuDuplicateException("Already has menuPattern for date " + menu.getDate());
            }
        } catch (IOException e) {
            throw new MenuUploadingException("Uploading menuPattern wrong format", e);
        }
    }
}
