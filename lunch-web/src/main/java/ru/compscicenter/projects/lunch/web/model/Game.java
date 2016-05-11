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

    public Game(final UserDBModel user,
                final MenuItemDBModel first,
                final MenuItemDBModel second,
                final MenuItemDBModel winner) {
        this.user = user;
        this.first = first;
        this.second = second;
        this.winner = winner;
    }

    public long getId() {
        return id;
    }

    public UserDBModel getUser() {
        return user;
    }

    public MenuItemDBModel getFirst() {
        return first;
    }

    public void setFirst(final MenuItemDBModel first) {
        this.first = first;
    }

    public MenuItemDBModel getSecond() {
        return second;
    }

    public void setSecond(final MenuItemDBModel second) {
        this.second = second;
    }

    public MenuItemDBModel getWinner() {
        return winner;
    }

    public void setWinner(final MenuItemDBModel winner) {
        this.winner = winner;
    }
}
