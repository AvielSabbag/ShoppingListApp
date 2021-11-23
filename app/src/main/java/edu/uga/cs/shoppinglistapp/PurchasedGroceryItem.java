package edu.uga.cs.shoppinglistapp;

public class PurchasedGroceryItem {
    private String itemName;
    private String description;
    private String price;
    //private Boolean purchased;

    public PurchasedGroceryItem() {
        this.itemName = null;
        this.description = null;
        this.price = null;
    }

    public PurchasedGroceryItem( String itemName, String description, String price ) {
        this.itemName = itemName;
        this.description = description;
        this.price = price;
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


    public String getPrice() {
        return description;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String toString() {
        return itemName + " " + description + " " + price;
    }
}
