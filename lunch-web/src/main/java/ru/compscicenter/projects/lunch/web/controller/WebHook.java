package ru.compscicenter.projects.lunch.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.compscicenter.projects.lunch.web.service.TelegramService;

import javax.annotation.Resource;

@Controller
@RequestMapping("/")
public class WebHook {

    @Resource(name = "telegramService")
    private TelegramService telegramService;

    @RequestMapping(value = "/hook", method = RequestMethod.POST)
    @ResponseBody
    public void hook(@RequestBody String a) {
        telegramService.handleUpdate(a);
    }

    @RequestMapping(value = "/hook", method = RequestMethod.GET)
    @ResponseBody
    public void test(@RequestParam(value = "a") String a) {
        telegramService.handleUpdate(a);
    }
}
