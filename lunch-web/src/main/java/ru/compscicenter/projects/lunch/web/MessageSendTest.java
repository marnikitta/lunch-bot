package ru.compscicenter.projects.lunch.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.compscicenter.projects.lunch.web.service.TelegramService;

public class MessageSendTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        TelegramService telegramService = context.getBean(TelegramService.class);
        telegramService.update();
    }
}
