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
    private int votes;
    /**
     * An integer representing what poll it concides with
     */
    private int ballot;

    /**
     * The constructor for the Question class. Creates a question given its various fields.
     * @param question The value to set the question's option to.
     * @param votes The value to set the question's vote to.
     * @param ballot The value to set the question's ballot to.
     */
    public Question(String question, int votes, int ballot) {
        this.question = question;
        this.votes = votes;
        this.ballot =  ballot;
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
     * Returns the value of the question's votes
     * @return A integer containing the question's votes
     */
    public int getVotes() {
        return votes;
    }
    /**
     * Returns the value of the question's poll
     * @return An integer containing the question's poll
     */
    public int getBallot() {
        return ballot;
    }
    /**
     * Set the value of the Question's ballot
     * @param ballot The value to set the question's ballot to.
     */
    public void setBallot(int ballot) {
        this.ballot = ballot;
    }
    /**
     * Set the value of the Question's votes
     * @param votes The value to set the question's vote to.
     */
    public void setVotes(int votes) {
        this.votes = votes;
    }
}
