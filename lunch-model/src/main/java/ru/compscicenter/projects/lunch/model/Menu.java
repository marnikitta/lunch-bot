package ru.compscicenter.projects.lunch.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class Menu {
    private final static SimpleDateFormat formatter = new SimpleDateFormat();
    private final GregorianCalendar date;
    private final List<MenuItem> items;

    private Menu(final GregorianCalendar date, final List<MenuItem> items) {
        this.date = date;
        this.items = items;
    }

    public static class Builder {
        private List<MenuItem> items = new ArrayList<>();
        private GregorianCalendar date;

        public void setDate(String date) {
            String[] parts = date.split(Pattern.quote("."));
            this.date = new GregorianCalendar(
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[0]));
            items = new ArrayList<>();
        }

        public void add(MenuItem item) {
            items.add(item);
        }

        public void addAll(Collection<? extends MenuItem> collection) {
            items.addAll(collection);
        }

        public Menu build() {
            return new Menu(date, items);
        }
    }

    public Stream<MenuItem> stream() {
        return items.stream();
    }

    public int size() {
        return items.size();
    }

    public MenuItem getItem(int index) {
        return items.get(index);
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public String getNiceDate() {
        return formatter.format(date.getTime());
    }

}
