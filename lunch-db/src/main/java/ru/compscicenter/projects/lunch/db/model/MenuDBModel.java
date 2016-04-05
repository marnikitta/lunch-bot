package ru.compscicenter.projects.lunch.db.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "menus")
public class MenuDBModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar date;

    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<MenuItemDBModel> items;

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<MenuItemDBModel> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDBModel> items) {
        this.items = items;
    }
}
