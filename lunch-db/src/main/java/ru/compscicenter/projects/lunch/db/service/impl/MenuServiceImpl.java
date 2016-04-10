package ru.compscicenter.projects.lunch.db.service.impl;

import org.springframework.transaction.annotation.Transactional;
import ru.compscicenter.projects.lunch.db.dao.MenuDAO;
import ru.compscicenter.projects.lunch.db.model.MenuDBModel;
import ru.compscicenter.projects.lunch.db.service.MenuService;
import ru.compscicenter.projects.lunch.db.util.ModelConverter;
import ru.compscicenter.projects.lunch.model.Menu;

public class MenuServiceImpl implements MenuService {
    private MenuDAO menuDAO;

    public void setMenuDAO(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    public void saveMenu(Menu menu) {
        MenuDBModel menuDBModel = ModelConverter.menuToDBMenu(menu);
        menuDAO.saveOrUpdate(menuDBModel);
    }
}
