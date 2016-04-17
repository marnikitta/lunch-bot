package ru.compscicenter.projects.lunch.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.compscicenter.projects.lunch.web.service.DeciderService;
import ru.compscicenter.projects.lunch.web.service.MenuService;

import javax.annotation.Resource;

@Controller
@RequestMapping("/")
public class MenuUpload {


    @Resource(name = "deciderService")
    DeciderService deciderService;

    @Resource(name = "menuService")
    MenuService menuService;

    @RequestMapping(value = "/", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String helloWorlder() {
        return "Hello world!";
    }

    @RequestMapping(method = RequestMethod.GET, value = "upload")
    public String upload(Model model) {
        model.addAttribute("title", "Title");
        return "fileUpload";
    }

    @RequestMapping(method = RequestMethod.POST, value = "upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (!file.isEmpty()) {
            try {
                menuService.upload(file.getInputStream());
                return "OK!";
            } catch (Exception e) {
                return ("You failed to upload " + e.getMessage());
            }
        } else {
            return "File is empty!";
        }
    }
}
