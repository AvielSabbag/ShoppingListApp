package edu.uga.cs.shoppinglistapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class GroceryPagerAdapter extends FragmentStateAdapter {

    private static List<GroceryItem> shoppingList;
    private static List<PurchasedItem> purchasedItemList;

    public GroceryPagerAdapter(Fragment fm, List<GroceryItem> groceryList, List<PurchasedItem> purchasedList) {
        super(fm);
        this.shoppingList = groceryList;
        purchasedItemList = purchasedList;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return new ShoppingListFragment(shoppingList);
        } else {
            return new RecentlyPurchasedFragment(purchasedItemList);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
