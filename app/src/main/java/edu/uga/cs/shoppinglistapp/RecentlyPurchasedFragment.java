package edu.uga.cs.shoppinglistapp;

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

 */
public class RecentlyPurchasedFragment extends Fragment {
    private List<PurchasedItem> purchasedList;
    private Button settleTheScore;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PurchasedRecyclerAdapter recyclerAdapter;

    public RecentlyPurchasedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RecentlyPurchasedFragment newInstance() {
        RecentlyPurchasedFragment fragment = new RecentlyPurchasedFragment();
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
        View fullView = inflater.inflate(R.layout.fragment_recently_purchased, container, false);


//
//        Button removeItem = fullView.findViewById(R.id.remove);
//
//        removeItem.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                DialogFragment newFragment = new PurchaseItemDialogFragment();
//                showDialogFragment(newFragment);
//            }
//        });


        /**newItem.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DialogFragment();
                showDialogFragment(newFragment);
            }
        });*/

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("purchasedList");
        recyclerView = (RecyclerView) fullView.findViewById(R.id.purchaseListRecycler);
        settleTheScore = (Button) fullView.findViewById(R.id.button5);

        purchasedList = new ArrayList<PurchasedItem>();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager( layoutManager );

        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    PurchasedItem purchasedItem = postSnapshot.getValue(PurchasedItem.class);
                    purchasedList.add(purchasedItem);
                }

                // Now, create a JobLeadRecyclerAdapter to populate a ReceyclerView to display the job leads.
                for(int i = 0; i< purchasedList.size(); i++) {
                    Log.d("ShoppingListFragment", "onDataChange: Item #"+ i + ": " + purchasedList.get(i).toString());
                }
                recyclerAdapter = new PurchasedRecyclerAdapter( purchasedList);
                recyclerView.setAdapter( recyclerAdapter );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("PurchasedListFragment", "onCancelled: The read failed: " + databaseError.getMessage());
            }
        });

        return fullView;
    }

    void showDialogFragment( DialogFragment newFragment ) {
        newFragment.show(getActivity().getSupportFragmentManager(), null);
    }

    public void addPurchasedItem(PurchasedItem pItem) {
        purchasedList.add(pItem);
    }
    public void notifyRecycler() {
        recyclerAdapter.notifyItemInserted(purchasedList.size()-1);
    }

//    public void onFinishNewPurchaseDialog(GroceryItem groceryItem) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference();
//        DatabaseReference purchasedList = database.getReference("purchasedList");
//
//        purchasedList.push().setValue( groceryItem )
//                .addOnSuccessListener( new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        Log.d( "ShoppingListFragment", "Grocery Item Purchased: " + groceryItem.toString() );
//                        // Show a quick confirmation
//                        Toast.makeText(getContext(), "Grocery Item Removed by: " + groceryItem.getUserSubmitted(),
//                                Toast.LENGTH_SHORT).show();
//                        //possibly not do anything else but maybe notify purchased recycler and add item to list in fragment
//                        ShoppingListFragment shoppingListFragment = (ShoppingListFragment) getActivity().getSupportFragmentManager().findFragmentByTag("f0");
//                        shoppingListFragment.addGroceryItem(groceryItem);
//                        shoppingListFragment.notifyRecycler();
//                    }
//                })
//                .addOnFailureListener( new OnFailureListener() {
//                    @Override
//                    public void onFailure(Exception e) {
//                        Toast.makeText( getContext(), "Failed to add grocery Item by: " + groceryItem.getUserSubmitted(),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        Query purchaseQuery = myRef.child("purchasedList").orderByChild("itemName").equalTo(groceryItem.getItemName());
//
//        purchaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot purchaseSnapshot: dataSnapshot.getChildren()) {
//                    purchaseSnapshot.getRef().removeValue();
//
//                    for(int i = 0; i<purchasedList.size(); i++) {
//                        if(purchasedList.get(i).getItemName().equals(groceryItem.getItemName())) {
//                            purchasedList.remove(i);
//                            recyclerAdapter.notifyItemRemoved(i);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("ShoppingListFragment", "onCancelled", databaseError.toException());
//            }
//        });



}
