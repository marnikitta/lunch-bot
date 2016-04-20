package ru.compscicenter.projects.lunch.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageSendTest {
    public static void main(String[] args) {
        String text = "/list -r";
        String list = "\\/list(\\s+(?<regex>-r))?(\\s+-n\\s+(?<name>\\\".*\\\"))?(\\s+-p\\s+(?<price>[\\d]+))?\\s*";
        Pattern pattern = Pattern.compile(list);
        Matcher matcher = pattern.matcher(text);
        System.out.println(matcher.matches());
    }
}
