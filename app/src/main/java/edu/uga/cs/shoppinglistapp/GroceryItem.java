package edu.uga.cs.shoppinglistapp;

public class GroceryItem {
    private String itemName;
    private String description;
    private String userSubmitted;

    public GroceryItem() {
        this.itemName = null;
        this.description = null;
    }

    public GroceryItem( String itemName, String description , String userSubmit) {
        this.itemName = itemName;
        this.description = description;
        this.userSubmitted = userSubmit;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserSubmitted(){return userSubmitted;}
    public void setUserSubmitted(String user) { userSubmitted = user;}
    public String toString() {
        return itemName + " added by:  " + userSubmitted + "\n" + description;
    }
}
