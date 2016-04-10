package ru.compscicenter.projects.lunch.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.compscicenter.projects.lunch.db.service.MenuService;
import ru.compscicenter.projects.lunch.estimator.MenuXmlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("hibernate-context.xml");
        MenuService menuService = (MenuService) context.getBean(MenuService.class);

        final File folder = new File("xml");

        for (final File fileEntry : folder.listFiles()) {
            menuService.saveMenu(MenuXmlParser.parseMenu(new FileInputStream(fileEntry)).get(0));
        }
    }
}
