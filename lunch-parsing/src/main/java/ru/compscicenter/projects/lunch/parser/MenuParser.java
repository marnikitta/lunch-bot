package ru.compscicenter.projects.lunch.parser;

import ru.compscicenter.projects.lunch.model.Menu;
import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MenuParser {

    private MenuParser() {
    }

    public static Menu parse(final String str) {
        String s = str.replaceAll("\r\n", " ");
        ArrayList<MenuItem> items = getItems(s);

        String date = getDate(s);

        Menu.Builder builder = new Menu.Builder();
        builder.addAll(items);
        builder.setDate(date);
        return builder.build();
    }

    public static String getDate(final String str) {
        Pattern dayPattern = Pattern.compile("^МЕНЮ\\s+на\\s+(?<day>\\d+)\\s+(?<month>января|февраля|марта|апреля|мая|июня|июля|августа|сентября|октября|ноября|декабря)\\s+(?<year>\\d{4})\\s+года", Pattern.CASE_INSENSITIVE);
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
            return (Integer.parseInt(day) < 10 ? "0" + day : day) + "." + (monthInt < 10 ? "0" + monthInt : monthInt) + "." + year;
        }
        return "02.06.1996";
    }

    public static ArrayList<MenuItem> getItems(String s) {
        String[] spl = s.split("САЛАТЫ|СУПЫ|ГОРЯЧЕЕ|ГАРНИР");
        String[] type = new String[]{"other", "salad", "soup", "main course", "garnish"};
        ArrayList<MenuItem> result = new ArrayList<>();
        int j = 4;
        for (int i = spl.length - 1; i >= 0; --i) {
            result.addAll(parseItems(spl[i], type[j--]));
        }
        return result;
    }

    public static ArrayList<MenuItem> parseItems(final String s, final String type) {
        ArrayList<MenuItem> result = new ArrayList<>();

        String namePattern = "(?<name>[\\p{L}\\s\\-,\\(\\)\"]+?)";
        String ingrPattern = "(\\((?<ingr>[\\p{L}0-9\\s,\\.«»\\\\/\\(\\)\\-]+?)\\))??";
        String weightPattern = "(?<w>\\d+)([/\\\\\\d]+)*\\s*(гр|Гр)\\.?";
        String calPattern = "\\((?<cal>\\d+)\\s+(Ккал|ккал)\\.?\\)";
        String pricePattern = "(?<price>\\d+-\\d+)";

        String pat = "\\s*" + namePattern + "\\s*" + ingrPattern + "\\s*" + weightPattern + "\\s*" + calPattern + "\\s*" + pricePattern;

        Pattern saladPattern = Pattern.compile(pat);
        Matcher matcher = saladPattern.matcher(s);
        while (matcher.find()) {
            String name = matcher.group("name");
            double cal = Double.parseDouble(matcher.group("cal"));
            double price = Double.parseDouble(matcher.group("price").replace("-", "."));
            double weight = Double.parseDouble(matcher.group("w"));
            List<String> ingr = new ArrayList<>();
            if (matcher.group("ingr") != null) {
                ingr.addAll(Arrays.asList(splitIngredients(matcher.group("ingr"))));
            }
            MenuItem item = new MenuItem(type, "", name, weight, cal, price, ingr);
            result.add(item);
        }
        return result;
    }

    public static String[] splitIngredients(String s) {
        return s.split(",\\s*");
    }
}
