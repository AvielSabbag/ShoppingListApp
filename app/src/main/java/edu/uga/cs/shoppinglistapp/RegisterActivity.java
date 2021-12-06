package edu.uga.cs.shoppinglistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "RegisterActivity";

    private EditText emailEditText;
    private EditText passworEditText;
    private EditText aptNameEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = (EditText) findViewById( R.id.editTextEmail);
        passworEditText = (EditText) findViewById( R.id.editTextPassword);
        aptNameEditText = (EditText) findViewById(R.id.editTextAptName);

        registerButton = (Button) findViewById( R.id.button4 );
        registerButton.setOnClickListener( new RegisterButtonClickListener() );
    }

    private class RegisterButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final String email = emailEditText.getText().toString();
            final String password = passworEditText.getText().toString();
            final String aptName = aptNameEditText.getText().toString();

            final UserBalance userBalance = new UserBalance(email, aptName, 0.00, 0.00);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference purchasedList = database.getReference("userList");

            purchasedList.push().setValue( userBalance )
                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Log.d( "ShoppingListFragment", "Grocery Item Purchased: " + userBalance.toString() );
                            // Show a quick confirmation
                            Toast.makeText(getApplicationContext(), "Grocery Item Purchased by: " + userBalance.getUser(),
                                    Toast.LENGTH_SHORT).show();
                            //possibly not do anything else but maybe notify purchased recycler and add item to list in fragment
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            firebaseAuth.createUserWithEmailAndPassword( email, password )
                                    .addOnCompleteListener( RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText( getApplicationContext(),
                                                        "Registered user: " + email,
                                                        Toast.LENGTH_SHORT ).show();

                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d( DEBUG_TAG, "createUserWithEmail: success" );

                                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                                Intent intent = new Intent( RegisterActivity.this, ListViewerActivity.class );
                                                startActivity( intent );

                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(DEBUG_TAG, "createUserWithEmail: failure", task.getException());
                                                Toast.makeText(RegisterActivity.this, "Registration failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText( getApplicationContext(), "Failed to add grocery Item by: " + userBalance.getUser(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });


            // This is how we can create a new user using an email/password combination.
            // Note that we also add an onComplete listener, which will be invoked once
            // a new user has been created by Firebase.  This is how we will know the
            // new user creation succeeded or failed.
            // If a new user has been created, Firebase already signs in the new user;
            // no separate sign in is needed.

        }
    }
}
