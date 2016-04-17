package ru.compscicenter.projects.lunch.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
@RequestMapping("/")
public class WebHook {
    Logger logger = Logger.getLogger(WebHook.class.getName());

    @RequestMapping("/hook")
    @ResponseBody
    public void hook(HttpServletRequest request) {
    }

    public void hookForHook(HttpServletRequest request) {
        logger.info(request.getQueryString());
    }
}
