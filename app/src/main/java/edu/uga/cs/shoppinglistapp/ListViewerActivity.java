package edu.uga.cs.shoppinglistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class ListViewerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_viewer);
        ViewPager2 viewPager = (ViewPager2) findViewById(R.id.viewPager);
        GroceryPagerAdapter groceryPagerAdapter = new GroceryPagerAdapter(this, this);
        viewPager.setAdapter(groceryPagerAdapter);
        Button logoutButton = findViewById(R.id.logout);

        logoutButton.setOnClickListener(new LogoutButtonClickListener());
    }

    //Logout button listener comment
    private class LogoutButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick( View view ){
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            view.getContext().startActivity(intent);
        }
    }




}