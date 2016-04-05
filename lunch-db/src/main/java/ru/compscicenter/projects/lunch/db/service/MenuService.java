package ru.compscicenter.projects.lunch.db.service;

import org.springframework.transaction.annotation.Transactional;
import ru.compscicenter.projects.lunch.db.dao.MenuDAO;
import ru.compscicenter.projects.lunch.db.model.MenuDBModel;
import ru.compscicenter.projects.lunch.db.util.ModelConverter;
import ru.compscicenter.projects.lunch.model.Menu;

public class MenuService {
    private MenuDAO menuDAO;

    public void setMenuDAO(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    @Transactional
    public void saveMenu(Menu menu) {
        MenuDBModel menuDBModel = ModelConverter.menuToDBMenu(menu);
        menuDAO.saveOrUpdate(menuDBModel);
    }
}
