package edu.uga.cs.shoppinglistapp;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment implements AddGroceryItemDialogFragment.AddGroceryItemDialogListener, PurchaseItemDialogFragment.PurchaseItemDialogListener {

    private List<GroceryItem> shoppingList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    public ShoppingListFragment() {
        // Required empty public constructor
    }
    
    // TODO: Rename and change types and number of parameters
    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fullView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        Button newItem = fullView.findViewById(R.id.button5);
        Button purchaseItem = fullView.findViewById(R.id.button6);

        newItem.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddGroceryItemDialogFragment();
                showDialogFragment(newFragment);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppingList");
        recyclerView = (RecyclerView) fullView.findViewById(R.id.recyclerView);

        shoppingList = new ArrayList<GroceryItem>();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager( layoutManager );

        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    GroceryItem groceryItem = postSnapshot.getValue(GroceryItem.class);
                    shoppingList.add(groceryItem);
                }

                // Now, create a JobLeadRecyclerAdapter to populate a ReceyclerView to display the job leads.
                for(int i = 0; i< shoppingList.size(); i++) {
                    Log.d("ShoppingListFragment", "onDataChange: Item #"+ i + ": " + shoppingList.get(i).toString());
                }
                recyclerAdapter = new GroceryRecyclerAdapter( shoppingList , getActivity());
                recyclerView.setAdapter( recyclerAdapter );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ShoppingListFragment", "onCancelled: The read failed: " + databaseError.getMessage());
            }
        });

        return fullView;
    }

    public void onFinishNewGroceryItemDialog(GroceryItem groceryItem) {
        // add the new job lead
        // Add a new element (JobLead) to the list of job leads in Firebase.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppingList");

        // First, a call to push() appends a new node to the existing list (one is created
        // if this is done for the first time).  Then, we set the value in the newly created
        // list node to store the new job lead.
        // This listener will be invoked asynchronously, as no need for an AsyncTask, as in
        // the previous apps to maintain job leads.
        myRef.push().setValue( groceryItem )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // Update the recycler view to include the new job lead
                        shoppingList.add( groceryItem );
                        recyclerAdapter.notifyItemInserted(shoppingList.size() - 1);

                        Log.d( "ShoppingListFragment", "Grocery Item saved: " + groceryItem.toString() );
                        // Show a quick confirmation
                        Toast.makeText(getContext(), "Grocery Item added by: " + groceryItem.getUserSubmitted(),
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText( getContext(), "Failed to add grocery Item by: " + groceryItem.getUserSubmitted(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void showDialogFragment( DialogFragment newFragment ) {
        newFragment.show(getActivity().getSupportFragmentManager(), null);
    }


    @Override
    public void onFinishNewPurchaseDialog(PurchasedItem purchasedItem) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        DatabaseReference purchasedList = database.getReference("purchasedList");

        purchasedList.push().setValue( purchasedItem )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d( "ShoppingListFragment", "Grocery Item Purchased: " + purchasedItem.toString() );
                        // Show a quick confirmation
                        Toast.makeText(getContext(), "Grocery Item Purchased by: " + purchasedItem.getUserBought(),
                                Toast.LENGTH_SHORT).show();
                        //possibly not do anything else but maybe notify purchased recycler and add item to list in fragment
                        RecentlyPurchasedFragment purchasedFragment = (RecentlyPurchasedFragment) getActivity().getSupportFragmentManager().findFragmentByTag("f1");
                        purchasedFragment.addPurchasedItem(purchasedItem);
                        purchasedFragment.notifyRecycler();
                    }
                })
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText( getContext(), "Failed to add grocery Item by: " + purchasedItem.getUserBought(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

        Query purchaseQuery = myRef.child("shoppingList").orderByChild("itemName").equalTo(purchasedItem.getItemPurchased());

        purchaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot purchaseSnapshot: dataSnapshot.getChildren()) {
                    purchaseSnapshot.getRef().removeValue();

                    for(int i = 0; i<shoppingList.size(); i++) {
                        if(shoppingList.get(i).getItemName().equals(purchasedItem.getItemPurchased())) {
                            shoppingList.remove(i);
                            recyclerAdapter.notifyItemRemoved(i);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ShoppingListFragment", "onCancelled", databaseError.toException());
            }
        });



    }
}