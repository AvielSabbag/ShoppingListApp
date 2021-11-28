package edu.uga.cs.shoppinglistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import java.util.List;

public class ListViewerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_viewer);
        ViewPager2 viewPager = (ViewPager2) findViewById(R.id.viewPager);
        GroceryPagerAdapter groceryPagerAdapter = new GroceryPagerAdapter(this, this);
        viewPager.setAdapter(groceryPagerAdapter);

    }
}