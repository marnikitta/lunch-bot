package ru.compscicenter.projects.lunch.web.service.impl;

import org.springframework.dao.DuplicateKeyException;
import ru.compscicenter.projects.lunch.model.Menu;
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
import java.util.logging.Logger;

public class MenuServiceImpl implements MenuService {

    private static Logger logger = Logger.getLogger(MenuServiceImpl.class.getName());
    private MenuDAO menuDAO;

    public void setMenuDAO(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    @Transactional
    public void saveMenu(Menu menu) {
        MenuDBModel menuDBModel = ModelConverter.menuToDBMenu(menu);
        if (getForDate(menuDBModel.getDate()) != null) {
            logger.severe("Already has entry for date: " + menu.getDate());
            throw new DuplicateKeyException("Already has entry for date: " + menu.getDate());
        }
        menuDAO.saveOrUpdate(menuDBModel);
    }

    @Override
    @Transactional
    public List<Menu> getAll() {
        List<MenuDBModel> list = menuDAO.getAll();
        Set<Menu> result = new LinkedHashSet<>();
        for (MenuDBModel menuDB : list) {
            result.add(ModelConverter.dbMenuToMenu(menuDB));
        }
        return new ArrayList<>(result);
    }

    @Override
    @Transactional
    public void saveAll(Collection<? extends Menu> coll) {
        for (Menu menu : coll) {
            saveMenu(menu);
        }
    }

    @Override
    @Transactional
    public List<Menu> getAllForDates(Calendar start, Calendar end) {
        List<MenuDBModel> list = menuDAO.getAllForDates(start, end);
        Set<Menu> result = new LinkedHashSet<>();
        for (MenuDBModel menuDB : list) {
            result.add(ModelConverter.dbMenuToMenu(menuDB));
        }
        return new ArrayList<>(result);
    }

    @Override
    @Transactional
    public Menu getForDate(Calendar day) {
        MenuDBModel menuDBModel = menuDAO.getForDate(day);
        if (menuDBModel != null) {
            return ModelConverter.dbMenuToMenu(menuDBModel);
        } else {
            logger.info("no menu for date " + day);
            return null;
        }
    }

    @Override
    @Transactional
    public List<MenuItemDBModel> getAllItems() {
        logger.info("getting all items");
        List<MenuItemDBModel> result = menuDAO.getAllItems();
        if (result != null) {
            Set<MenuItemDBModel> menuItemDBModels = new HashSet<>(result);
            return new ArrayList<>(menuItemDBModels);
        }
        return null;
    }

    @Override
    @Transactional
    public Menu upload(InputStream stream) throws MenuUploadingException, MenuDuplicateException {
        try {
            Menu menu = PDFToMenu.parsePDF(stream);
            if (null == getForDate(menu.getDate())) {
                saveMenu(menu);
                return menu;
            } else {
                throw new MenuDuplicateException("Already has menu for date " + menu.getDate());
            }
        } catch (IOException e) {
            logger.info("Uploading menu wrong format");
            throw new MenuUploadingException("Uploading menu wrong format");
        }

    }
}
