package ru.compscicenter.projects.lunch.db;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("hibernate-context.xml");
        SessionFactory factory = context.getBean(SessionFactory.class);
    }
}
