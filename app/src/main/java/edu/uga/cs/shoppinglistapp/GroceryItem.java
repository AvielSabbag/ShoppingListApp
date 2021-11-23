package edu.uga.cs.shoppinglistapp;

public class GroceryItem {
    private String itemName;
    private String description;

    public GroceryItem() {
        this.itemName = null;
        this.description = null;
    }

    public GroceryItem( String itemName, String description ) {
        this.itemName = itemName;
        this.description = description;
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


    public String toString() {
        return itemName + " " + description;
    }
}
