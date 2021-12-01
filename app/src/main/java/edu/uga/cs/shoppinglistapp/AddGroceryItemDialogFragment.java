package edu.uga.cs.shoppinglistapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddGroceryItemDialogFragment extends DialogFragment {
    private EditText itemName;
    private EditText itemDescription;
    private String userSubmitted;

    public interface AddGroceryItemDialogListener {
        void onFinishNewGroceryItemDialog(GroceryItem groceryItem);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create the AlertDialog view
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_grocery_item_dialog,
                (ViewGroup) getActivity().findViewById(R.id.root));

        // get the view objects in the AlertDialog
        itemName = layout.findViewById( R.id.editText1 );
        itemDescription = layout.findViewById( R.id.editText4 );

        // create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        // Set its view (inflated above).
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle( "New Grocery Item" );
        // Provide the negative button listener
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });
        // Provide the positive button listener
        builder.setPositiveButton( android.R.string.ok, new ButtonClickListener() );

        // Create the AlertDialog and show it
        return builder.create();
    }

    private class ButtonClickListener implements DialogInterface.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String itemNameString = itemName.getText().toString();
            String itemDescriptionString = itemDescription.getText().toString();
            FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if( currentUser != null ) {
                        // User is signed in
                        userSubmitted = currentUser.getEmail();
                    } else {
                        // User is signed out
                        userSubmitted = "empty";
                    }
                }
            });

            GroceryItem groceryItem = new GroceryItem(itemDescriptionString, itemNameString);

            // get the Activity's listener to add the new job lead
            AddGroceryItemDialogListener listener = (AddGroceryItemDialogListener) getActivity().getSupportFragmentManager().findFragmentByTag("f0");
            // add the new job lead
            listener.onFinishNewGroceryItemDialog( groceryItem);
            // close the dialog
            dismiss();
        }
    }
}
