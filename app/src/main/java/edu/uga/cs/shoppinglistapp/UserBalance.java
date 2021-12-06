package edu.uga.cs.shoppinglistapp;

public class UserBalance {
    private String user;
    private String aptName;
    private Double amntSpent;
    private Double amntOwed;

    public UserBalance() {
        user = "";
        aptName = "";
        amntOwed = 0.00;
        amntSpent = 0.00;
    }
    public UserBalance(String user, String aptName, Double amntSpent, Double amntOwed) {
        this.user = user;
        this.aptName = aptName;
        this.amntSpent = amntSpent;
        this.amntOwed = amntOwed;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setAptName(String aptName) {
        this.aptName = aptName;
    }

    public void setAmntSpent(Double amntSpent) {
        this.amntSpent = amntSpent;
    }

    public void setAmntOwed(Double amntOwed) {
        this.amntOwed = amntOwed;
    }

    public String getUser() {
        return user;
    }

    public String getAptName() {
        return aptName;
    }

    public Double getAmntSpent() {
        return amntSpent;
    }

    public Double getAmntOwed() {
        return amntOwed;
    }
}

