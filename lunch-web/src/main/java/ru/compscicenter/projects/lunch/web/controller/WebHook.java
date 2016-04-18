package ru.compscicenter.projects.lunch.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.compscicenter.projects.lunch.web.service.TelegramService;

import javax.annotation.Resource;
import java.util.logging.Logger;

@Controller
@RequestMapping("/")
public class WebHook {
    Logger logger = Logger.getLogger(WebHook.class.getName());

    @Resource(name = "telegramService")
    private TelegramService telegramService;

    @RequestMapping(value = "/hook", method = RequestMethod.POST)
    @ResponseBody
    public void hook(@RequestBody String a) {
        telegramService.sendMessage(169022871, a);
        telegramService.handleUpdate(a);
    }
}
