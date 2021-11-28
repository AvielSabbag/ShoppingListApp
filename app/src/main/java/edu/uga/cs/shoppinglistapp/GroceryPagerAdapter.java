package edu.uga.cs.shoppinglistapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class GroceryPagerAdapter extends FragmentStateAdapter {

    private static List<GroceryItem> shoppingList;
    private static List<PurchasedItem> purchasedItemList;
    private static Context context;

    public GroceryPagerAdapter(FragmentActivity fm, Context con) {
        super(fm);
        context = con;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return new ShoppingListFragment();
        } else {
            return new RecentlyPurchasedFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
