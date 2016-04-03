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

    public List<MenuItemDBModel> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDBModel> items) {
        this.items = items;
    }
}
