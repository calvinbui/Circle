package com.id11413010.circle.app.pojo;

/**
 * TODO
 */
public class Money {
    private int id;
    private int circle;
    private int from;
    private int to;
    private int amount;
    private int paid;
    private String description;

    public Money(int circle, int from, int to, int amount, int paid, String description) {
        this.circle = circle;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.paid = paid;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCircle() {
        return circle;
    }

    public void setCircle(int circle) {
        this.circle = circle;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
