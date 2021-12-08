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
    private TextView userOnline;
    private static TextView amountOwed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_viewer);
        ViewPager2 viewPager = (ViewPager2) findViewById(R.id.viewPager);
        GroceryPagerAdapter groceryPagerAdapter = new GroceryPagerAdapter(this, this);
        viewPager.setAdapter(groceryPagerAdapter);
        Button logoutButton = findViewById(R.id.logout);
        userOnline = findViewById(R.id.textView8);
        amountOwed = findViewById(R.id.textView7);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setInfoUI(user.getEmail());

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

    public String setInfoUI(String email) {
        final String[] apartmentName = {"empty"};
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Query purchaseQuery = myRef.child("userList").orderByChild("user").equalTo(email);

        purchaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    UserBalance userBalance = userSnapshot.getValue(UserBalance.class);
                    userOnline.setText( "User Online: " + userBalance.getUser() + ";\n Residing in: " + userBalance.getAptName());
                    amountOwed.setText("Amnt Spent: " + userBalance.getAmntSpent() + ";\t Amnt Owed: " + userBalance.getAmntOwed());
                    apartmentName[0] = userBalance.getAptName();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ShoppingListFragment", "onCancelled", databaseError.toException());
            }
        });

        return apartmentName[0];
    }
    public static String findApartmentNameByEmail(String email) {
        final String[] apartmentName = new String[1];
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Query purchaseQuery = myRef.child("userList").orderByChild("user").equalTo(email);

        purchaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    UserBalance userBalance = userSnapshot.getValue(UserBalance.class);
                    Log.d("findApartmentName", "onDataChange: apartmentName: " + userBalance.getAptName());
                    apartmentName[0] = userBalance.getAptName();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ShoppingListFragment", "onCancelled", databaseError.toException());
            }
        });

        return apartmentName[0];
    }

    static void updateAmountSpentTextView(Double amntSpent) {
        amountOwed.setText("Amount Spent: "+ amntSpent.toString());
    }




}