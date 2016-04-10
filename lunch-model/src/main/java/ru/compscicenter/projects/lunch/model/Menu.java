package ru.compscicenter.projects.lunch.model;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class Menu {

    private final Calendar date;
    private final List<MenuItem> items;

    private Menu(final Calendar date, final List<MenuItem> items) {
        this.date = date;
        this.items = items;
    }

    public static class Builder {
        private List<MenuItem> items = new ArrayList<>();
        private Calendar date;

        public void setDate(String date) {
            String[] parts = date.split(Pattern.quote("."));
            this.date = new GregorianCalendar(
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[1]) - 1,
                    Integer.parseInt(parts[0]));
            items = new ArrayList<>();
        }

        public void setDate(Calendar date) {
            this.date = date;
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

    public List<MenuItem> getItemsCopy() {
        return new ArrayList<>(items);
    }

    public MenuItem getItem(int index) {
        return items.get(index);
    }

    public Calendar getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menu menu = (Menu) o;

        if (date != null ? !date.equals(menu.date) : menu.date != null) return false;
        return !(items != null ? !items.equals(menu.items) : menu.items != null);

    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }
}
