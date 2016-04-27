package ru.compscicenter.projects.lunch.web;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageSendTest {
    public static void main(String[] args) {
        String text = "/list -r hello";
        String list = "\\/list(\\s+(?<regex>-r))?\\s+(?<name>.*?)?\\s*";
        Pattern pattern = Pattern.compile(list);
        Matcher matcher = pattern.matcher(text);
        System.out.println(matcher.matches());
        System.out.println(Charset.defaultCharset());
    }
}
