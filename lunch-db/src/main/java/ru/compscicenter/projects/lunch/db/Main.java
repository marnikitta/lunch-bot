package ru.compscicenter.projects.lunch.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.compscicenter.projects.lunch.db.service.MenuService;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        MenuService menuService = context.getBean(MenuService.class);
    }
}
