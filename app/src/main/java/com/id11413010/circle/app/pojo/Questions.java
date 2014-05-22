package com.id11413010.circle.app.pojo;

/**
 * Created by CalvinLaptop on 21/05/2014.
 */
public class Questions {
    private String question;
    private int votes;
    private int ballot;

    public Questions(String question, int votes, int ballot) {
        this.question = question;
        this.votes = votes;
        this.ballot =  ballot;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getVotes() {
        return votes;
    }

    public int getBallot() {
        return ballot;
    }

    public void setBallot(int ballot) {
        this.ballot = ballot;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
