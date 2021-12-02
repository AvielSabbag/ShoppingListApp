package edu.uga.cs.shoppinglistapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PurchaseItemDialogFragment extends DialogFragment {
    private EditText purchaseText;
    private String userBought;

    public interface PurchaseItemDialogListener {
        void onFinishNewPurchaseDialog(PurchasedItem purchasedItem);
    }
    public static PurchaseItemDialogFragment newInstance(GroceryItem gItem) {
        PurchaseItemDialogFragment newFragment = new PurchaseItemDialogFragment();

        Bundle args = new Bundle();
        args.putString("itemName", gItem.getItemName());
        newFragment.setArguments(args);

        return  newFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create the AlertDialog view
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.purchase_item_dialog,
                (ViewGroup) getActivity().findViewById(R.id.root));

        // get the view objects in the AlertDialog
        purchaseText = layout.findViewById( R.id.purchaseText );

        // create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        // Set its view (inflated above).
        builder.setView(layout);

        // Set the title of the AlertDialog
        String itemName = getArguments().getString("itemName");
        builder.setTitle( "Purchase " + itemName + "?");
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
            Double price = Double.parseDouble(purchaseText.getText().toString());
            String itemName = getArguments().getString("itemName");

            FirebaseUser currentUser  = FirebaseAuth.getInstance().getCurrentUser();
            if(currentUser != null ) {
                userBought = currentUser.getEmail();
            } else {
                userBought = "empty";
            }

            PurchasedItem purchasedItem = new PurchasedItem(itemName, price, userBought);

            // get the Activity's listener to add the new job lead
            PurchaseItemDialogListener listener = (PurchaseItemDialogListener) getActivity().getSupportFragmentManager().findFragmentByTag("f0");
            // add the new job lead
            listener.onFinishNewPurchaseDialog( purchasedItem);
            // close the dialog
            dismiss();
        }
    }
}
