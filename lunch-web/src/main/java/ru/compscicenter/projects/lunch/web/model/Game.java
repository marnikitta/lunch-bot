package ru.compscicenter.projects.lunch.web.model;


import javax.persistence.*;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private UserDBModel user;

    @JoinColumn(name = "first_id")
    @ManyToOne
    private MenuItemDBModel first;

    @JoinColumn(name = "second_id")
    @ManyToOne
    private MenuItemDBModel second;

    @JoinColumn(name = "winner_id")
    @ManyToOne
    private MenuItemDBModel winner;

    public Game() {
    }

    public Game(MenuItemDBModel first, MenuItemDBModel second, MenuItemDBModel winner) {
        this.first = first;
        this.second = second;
        this.winner = winner;
    }

    public UserDBModel getUser() {
        return user;
    }

    public void setUser(UserDBModel user) {
        this.user = user;
    }

    public MenuItemDBModel getFirst() {
        return first;
    }

    public void setFirst(MenuItemDBModel first) {
        this.first = first;
    }

    public MenuItemDBModel getSecond() {
        return second;
    }

    public void setSecond(MenuItemDBModel second) {
        this.second = second;
    }

    public MenuItemDBModel getWinner() {
        return winner;
    }

    public void setWinner(MenuItemDBModel winner) {
        this.winner = winner;
    }
}
