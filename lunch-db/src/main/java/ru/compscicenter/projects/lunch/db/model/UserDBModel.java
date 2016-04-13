package ru.compscicenter.projects.lunch.db.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserDBModel {
    @Id
    @Column(name = "id")
    private long id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "userlove_menu_item", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "menu_item_id"))
    private Set<MenuItemDBModel> loveSet;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "userhate_menu_item", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "menu_item_id"))
    private Set<MenuItemDBModel> hateSet;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<MenuItemDBModel> getLoveSet() {
        return loveSet;
    }

    public void setLoveSet(Set<MenuItemDBModel> loveSet) {
        this.loveSet = loveSet;
    }

    public Set<MenuItemDBModel> getHateSet() {
        return hateSet;
    }

    public void setHateSet(Set<MenuItemDBModel> hateSet) {
        this.hateSet = hateSet;
    }
}
