package com.id11413010.circle.app.pojo;

/**
 * Created by Calvin on 25/05/2014.
 */
public class Ranking {
    private Integer id;
    private Integer leaderboard;
    private Integer user;
    private Integer position;

    public Ranking(Integer id, Integer leaderboard, Integer user, Integer position) {
        this.id = id;
        this.leaderboard = leaderboard;
        this.user = user;
        this.position = position;
    }

    public Integer getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Integer leaderboard) {
        this.leaderboard = leaderboard;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}

