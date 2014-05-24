package com.id11413010.circle.app.pojo;

/**
 * TODO
 */
public class Money {
    private Integer id;
    private String circle;
    private int from;
    private int to;
    private double amount;
    private int paid;
    private String description;

    public Money(String circle, int from, int to, double amount, int paid, String description, Integer id) {
        this.circle = circle;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.paid = paid;
        this.description = description;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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
