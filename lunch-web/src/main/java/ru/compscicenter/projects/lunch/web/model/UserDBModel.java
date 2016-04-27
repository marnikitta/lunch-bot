package ru.compscicenter.projects.lunch.web.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class UserDBModel {
    @Id
    @Column(name = "id")
    private long id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "userlove_menu_item", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "menu_item_id"))
    private List<MenuItemDBModel> loveList;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "userhate_menu_item", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "menu_item_id"))
    private List<MenuItemDBModel> hateList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<MenuItemDBModel> getLoveList() {
        return loveList;
    }

    public void setLoveList(List<MenuItemDBModel> loveList) {
        this.loveList = loveList;
    }

    public List<MenuItemDBModel> getHateList() {
        return hateList;
    }

    public void setHateList(List<MenuItemDBModel> hateList) {
        this.hateList = hateList;
    }
}
