package ru.compscicenter.projects.lunch.model;

import java.util.List;

public class MenuItem {

    private final String type;
    private final String tags;
    private final String name;
    private final double weight;
    private final double calorie;
    private final double price;
    private final List<String> composition;


    public MenuItem(final String type,
                    final String tags,
                    final String name,
                    final double weight,
                    final double calorie,
                    final double price,
                    final List<String> composition) {
        this.type = type;
        this.tags = tags;
        this.name = name;
        this.weight = weight;
        this.calorie = calorie;
        this.price = price;
        this.composition = composition;
    }

    public String getType() {
        return type;
    }

    public String getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getCalorie() {
        return calorie;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getComposition() {
        return composition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuItem menuItem = (MenuItem) o;

        if (Double.compare(menuItem.weight, weight) != 0) return false;
        if (Double.compare(menuItem.calorie, calorie) != 0) return false;
        if (Double.compare(menuItem.price, price) != 0) return false;
        if (type != null ? !type.equals(menuItem.type) : menuItem.type != null) return false;
        if (tags != null ? !tags.equals(menuItem.tags) : menuItem.tags != null) return false;
        if (name != null ? !name.equals(menuItem.name) : menuItem.name != null) return false;
        return !(composition != null ? !composition.equals(menuItem.composition) : menuItem.composition != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = type != null ? type.hashCode() : 0;
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(calorie);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (composition != null ? composition.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MenuItem{");
        sb.append("type='").append(type).append('\'');
        sb.append(", tags='").append(tags).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", weight=").append(weight);
        sb.append(", calorie=").append(calorie);
        sb.append(", price=").append(price);
        sb.append(", composition=").append(composition);
        sb.append('}');
        return sb.toString();
    }
}
