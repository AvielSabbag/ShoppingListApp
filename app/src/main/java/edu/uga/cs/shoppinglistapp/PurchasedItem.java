package edu.uga.cs.shoppinglistapp;

public class PurchasedItem {
    private GroceryItem itemPurchased;
    private Double price;
    private String userBought;

    public GroceryItem getItemPurchased() {return itemPurchased;}
    public void setItemPurchased(GroceryItem item){itemPurchased = item;}
    public double getPrice() {return price;}
    public void setPrice(Double price){this.price = price;}
    public String getUserBought() {return userBought;}
    public void setUserBought(String user) {userBought = user;}

}
