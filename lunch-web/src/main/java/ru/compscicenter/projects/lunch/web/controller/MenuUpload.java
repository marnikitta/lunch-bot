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

import javax.annotation.Resource;

@Controller
@RequestMapping("/")
public class MenuUpload {


    @Resource(name = "deciderService")
    DeciderService deciderService;

    @RequestMapping(value = "/", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String helloWorlder() {
        return "Hello worldd!";
    }

    @RequestMapping(method = RequestMethod.GET, value = "upload")
    public String upload(Model model) {
        model.addAttribute("title", "Title");
        return "fileUpload";
    }

    @RequestMapping(method = RequestMethod.POST, value = "upload")
    public String handleFileUpload(@RequestParam("name") String name,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Folder separators not allowed");
            return "redirect:upload";
        }
        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Relative pathnames not allowed");
            return "redirect:upload";
        }

        if (!file.isEmpty()) {
            try {
                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded " + name + "!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("message",
                        "You failed to upload " + name + " => " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "You failed to upload " + name + " because the file was empty");
        }
        return "redirect:upload";
    }
}
