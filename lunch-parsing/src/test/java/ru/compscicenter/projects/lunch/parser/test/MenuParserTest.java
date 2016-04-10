package ru.compscicenter.projects.lunch.parser.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.compscicenter.projects.lunch.parser.MenuParser;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MenuParserTest extends Assert {

    @Test
    public void getDateTest() {
        String[] headers = new String[]{
                "МЕНЮ на  22 января   2016 года",
                "МЕНЮ на  20 февраля   2016 года",
                "МЕНЮ на  20 сентября   2016 года",
                "МЕНЮ на  1 декабря   2014 года"
        };
        Calendar[] results = new GregorianCalendar[4];
        results[0] = new GregorianCalendar(2016, 0, 22);
        results[1] = new GregorianCalendar(2016, 1, 20);
        results[2] = new GregorianCalendar(2016, 8, 20);
        results[3] = new GregorianCalendar(2014, 11, 1);

        for (int i = 0; i < headers.length; ++i) {
            assertEquals(MenuParser.getDate(headers[i]), results[i]);
        }
    }
}
