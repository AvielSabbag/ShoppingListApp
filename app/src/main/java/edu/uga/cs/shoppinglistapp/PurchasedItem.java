package edu.uga.cs.shoppinglistapp;

public class PurchasedItem {
    private String itemPurchased;
    private Double price;
    private String userBought;

    public PurchasedItem(String itemName, Double price, String userBought) {
        itemPurchased = itemName;
        this.price = price;
        this.userBought = userBought;
    }
    public String getItemPurchased() {return itemPurchased;}
    public void setItemPurchased(String item){itemPurchased = item;}
    public double getPrice() {return price;}
    public void setPrice(Double price){this.price = price;}
    public String getUserBought() {return userBought;}
    public void setUserBought(String user) {userBought = user;}

}
