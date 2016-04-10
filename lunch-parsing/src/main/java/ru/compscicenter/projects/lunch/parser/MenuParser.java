package ru.compscicenter.projects.lunch.parser;

import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MenuParser {

    private static Logger logger = Logger.getLogger(MenuParser.class.getName());

    private MenuParser() {
    }

    public static Menu parse(final String str) {
        String s = str.replaceAll("\r\n", " ");
        Menu.Builder builder = new Menu.Builder();

        List<MenuItem> items = getItems(s);
        Calendar date = getDate(s);

        builder.addAll(items);
        builder.setDate(date);

        return builder.build();
    }

    public static Calendar getDate(final String str) {
        Pattern dayPattern = Pattern.compile("МЕНЮ\\s+на\\s+(?<day>\\d+)\\s+(?<month>января|февраля|марта|апреля|мая|июня|июля|августа|сентября|октября|ноября|декабря)\\s+(?<year>\\d{4})\\s+года", Pattern.CASE_INSENSITIVE);
        Matcher matcher = dayPattern.matcher(str);
        if (matcher.find()) {
            String day = matcher.group("day");
            String month = matcher.group("month");
            String year = matcher.group("year");
            month = month.toLowerCase();
            int monthInt;

            switch (month) {
                case "января":
                    monthInt = 1;
                    break;
                case "февраля":
                    monthInt = 2;
                    break;
                case "марта":
                    monthInt = 3;
                    break;
                case "апреля":
                    monthInt = 4;
                    break;
                case "мая":
                    monthInt = 5;
                    break;
                case "июня":
                    monthInt = 6;
                    break;
                case "июля":
                    monthInt = 7;
                    break;
                case "августа":
                    monthInt = 8;
                    break;
                case "сентября":
                    monthInt = 9;
                    break;
                case "октября":
                    monthInt = 10;
                    break;
                case "ноября":
                    monthInt = 11;
                    break;
                case "декабря":
                    monthInt = 12;
                    break;
                default:
                    monthInt = 0;
            }
            monthInt -= 1;
            return new GregorianCalendar(Integer.parseInt(year), monthInt, Integer.parseInt(day));
        } else {
            logger.warning("Failed to parse date: " + str);
            return new GregorianCalendar(1996, 6, 2);
        }
    }

    public static List<MenuItem> getItems(final String s) {
        Pattern splitPattern = Pattern.compile("САЛАТЫ|СУПЫ|ГОРЯЧЕЕ|ГАРНИР");
        String[] spl = splitPattern.split(s);
        String[] type = new String[]{"other", "salad", "soup", "main course", "garnish"};

        List<MenuItem> result = new ArrayList<>();
        int j = 4;
        for (int i = spl.length - 1; i >= 0; --i) {
            result.addAll(parseItems(spl[i], type[j--]));
        }
        return result;
    }

    public static List<MenuItem> parseItems(final String s, final String type) {
        List<MenuItem> result = new ArrayList<>();

        String namePattern = "(?<name>[\\p{L}\\s\\-,\"]+?)";
        String ingrPattern = "(\\((?<ingr>[\\p{L}0-9\\s,\\.«»\\\\/\\-]+?)\\))??";
        String weightPattern = "(?<w>\\d+)([/\\\\\\d]+)*\\s*(Гр|гр)\\.?";
        String calPattern = "\\((?<cal>\\d+)\\s+(Ккал|ккал)\\.?\\)";
        String pricePattern = "(?<price>\\d+-\\d+)";

        String pat = "\\s*" + namePattern + "\\s*" + ingrPattern + "\\s*" + weightPattern + "\\s*" + calPattern + "\\s*" + pricePattern;

        Pattern saladPattern = Pattern.compile(pat);
        Matcher matcher = saladPattern.matcher(s);

        while (matcher.find()) {
            String name = matcher.group("name").toLowerCase().replaceAll("\\s+", " ");

            double cal = Double.parseDouble(matcher.group("cal"));
            double price = Double.parseDouble(matcher.group("price").replace("-", "."));
            double weight = Double.parseDouble(matcher.group("w"));

            List<String> ingr = new ArrayList<>();
            if (matcher.group("ingr") != null) {
                for (String ing : splitIngredients(matcher.group("ingr"))) {
                    ingr.add(ing.toLowerCase().replaceAll("\\s+", " "));
                }
            }

            MenuItem item = new MenuItem(type, "", name, weight, cal, price, ingr);
            result.add(item);
        }
        return result;
    }

    public static String[] splitIngredients(final String s) {
        return s.split(",\\s*");
    }
}
