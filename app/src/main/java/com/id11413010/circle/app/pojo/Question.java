/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.pojo;

/**
 * A plain old java object representing a Question in a Poll. Contains getter and setter methods for its various fields.
 */
public class Question {
    /**
     * A String representing a poll option
     */
    private String question;
    /**
     * An integer representing the number of votes on the question
     */
    private int poll;

    private Integer id;
    /**
     * The constructor for the Question class. Creates a question given its various fields.
     * @param question The value to set the question's option to.
     * @param poll The value to set the question's poll to.
     */
    public Question(String question, int poll, Integer id) {
        this.question = question;
        this.poll = poll;
        this.id = id;
    }
    /**
     * Returns the value of the question's option
     * @return A String containing the question's option
     */
    public String getQuestion() {
        return question;
    }
    /**
     * Set the value of the Question's option
     * @param question The value to set the question's option to.
     */
    public void setQuestion(String question) {
        this.question = question;
    }
    /**
     * Returns the value of the question's poll
     * @return An integer containing the question's poll
     */
    public int getPoll() {
        return poll;
    }
    /**
     * Set the value of the Question's poll
     * @param poll The value to set the question's poll to.
     */
    public void setPoll(int poll) {
        this.poll = poll;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
