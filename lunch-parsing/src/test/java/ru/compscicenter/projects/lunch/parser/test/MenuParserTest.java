package ru.compscicenter.projects.lunch.parser.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.parser.MenuParser;

public class MenuParserTest extends Assert {

    @Test
    public void getDateTest() {
        String date = "МЕНЮ на 4 марта 2016 года";
        assertEquals(MenuParser.getDate(date), "04.03.2016");
        date = "МЕНЮ на 3 февраля 2016 года\n" +
                "Для составления бизнес-ланча предлагаем следующее меню:\n" +
                "с 12.00 до 16.00";
        assertEquals(MenuParser.getDate(date), "03.02.2016");
    }
}
