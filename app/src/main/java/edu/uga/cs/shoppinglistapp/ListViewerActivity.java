package edu.uga.cs.shoppinglistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
        TextView userOnline = findViewById(R.id.textView8);
        TextView amountOwed = findViewById(R.id.textView7);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Query purchaseQuery = myRef.child("userList").orderByChild("user").equalTo(user.getEmail());

        purchaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    UserBalance userBalance = userSnapshot.getValue(UserBalance.class);
                    userOnline.setText( "User Online: " + userBalance.getUser() + ";\n Residing in: " + userBalance.getAptName());
                    amountOwed.setText("Amnt Spent: " + userBalance.getAmntSpent() + ";\t Amnt Owed: " + userBalance.getAmntOwed());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ShoppingListFragment", "onCancelled", databaseError.toException());
            }
        });

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