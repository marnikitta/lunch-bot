package ru.compscicenter.projects.lunch.web.model;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "menu_items")
public class MenuItemDBModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "tags")
    private String tags;

    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private double weight;

    @Column(name = "calorie")
    private double calorie;

    @Column(name = "price")
    private double price;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "compositions", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "comp")
    private List<String> composition;


    public MenuItemDBModel(final String type,
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

    public MenuItemDBModel() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setComposition(List<String> composition) {
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
}
