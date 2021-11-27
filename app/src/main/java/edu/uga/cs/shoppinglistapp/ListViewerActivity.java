package edu.uga.cs.shoppinglistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

public class ListViewerActivity extends AppCompatActivity {
    private static List<GroceryItem> shoppingList;
    private static List<PurchasedItem> purchasedItemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_viewer);

    }
}