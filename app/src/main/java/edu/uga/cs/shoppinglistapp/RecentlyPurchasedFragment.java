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

import com.firebase.ui.auth.data.model.User;
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

import edu.uga.cs.shoppinglistapp.ListViewerActivity;
import edu.uga.cs.shoppinglistapp.PurchasedItem;
import edu.uga.cs.shoppinglistapp.PurchasedRecyclerAdapter;
import edu.uga.cs.shoppinglistapp.UserBalance;

/**

 */
public class RecentlyPurchasedFragment extends Fragment {
    private static List<PurchasedItem> purchasedList;
    private static List<UserBalance> balanceList;
    private Button settleTheScore;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PurchasedRecyclerAdapter recyclerAdapter;
    private Double totalListCost;
    private Double avgSpent;
    private Double amntOwed;
    private static UserBalance newBalance;
    private static DatabaseReference balanceRef;
    private static String apartmentName;

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("purchasedList");
        balanceRef = database.getReference("userList");
        recyclerView = (RecyclerView) fullView.findViewById(R.id.purchaseListRecycler);
        settleTheScore = (Button) fullView.findViewById(R.id.button5);
        balanceList = new ArrayList<UserBalance>();
        populateBalances();
        newBalance = new UserBalance();
        totalListCost = 0.00;

        settleTheScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateBalances();
                for (PurchasedItem pItem: purchasedList) {
                    Log.d("SettleTheScore", "userName: " + pItem.getUserBought());
                    //add item cost to total list cost
                    totalListCost = totalListCost + pItem.getPrice();
                    //remove item from list
                    removePurchasedItem(pItem);
                }
                //calculate avgSpent
                Log.d("RecentlyPurchased", "avgSpent- apartmentName: " + apartmentName);
                List<UserBalance> userBalanceArrayList = balanceList;
                avgSpent = totalListCost/userBalanceArrayList.size();
                Log.d("SettleTheScore", " User 0: " + userBalanceArrayList.get(0).getUser());
                Log.d("SettleTheScore", "User 1: " + userBalanceArrayList.get(1).getUser());
                //for each roomate(amntOwed = amntSpent - avgSpent)
                for (UserBalance u: userBalanceArrayList) {
                    amntOwed = u.getAmntSpent() - avgSpent;
                    UserBalance newUserBalance = new UserBalance(u.getUser(), u.getAptName(), 0.00, (u.getAmntOwed() + amntOwed));

                    Query userQuery = balanceRef.orderByChild("user").equalTo(u.getUser());
                    userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                Log.d("SettleTheScore", "User: "+ newUserBalance.getUser()+
                                        "newAmountOwed: " + newUserBalance.getAmntOwed());
                                userSnapshot.getRef().setValue(newUserBalance);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("ShoppingListFragment", "onCancelled", databaseError.toException());
                        }
                    });
                }

            }

        });

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
    static public int[] balanceExists(String email) {
        int returns[] = new int[2];
        returns = new int[]{0, 0};
        for (int i = 0; i<balanceList.size(); i++) {
            if (email.equals(balanceList.get(i).getUser())) {
                returns = new int[]{1, i};
            }
        }
        return returns;
    }

    public void removePurchasedItem(PurchasedItem pItem) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Query purchaseQuery = myRef.child("purchasedList").orderByChild("itemPurchased").equalTo(pItem.getItemPurchased());

        purchaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot purchaseSnapshot : dataSnapshot.getChildren()) {
                    purchaseSnapshot.getRef().removeValue();

                    for (int i = 0; i < purchasedList.size(); i++) {
                        if (purchasedList.get(i).getItemPurchased().equals(pItem.getItemPurchased())) {
                            purchasedList.remove(i);
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

    static public void populateBalances() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("userList");
        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    UserBalance userBalance = postSnapshot.getValue(UserBalance.class);
                    apartmentName = userBalance.getAptName();
                    balanceList.add(userBalance);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("PurchasedListFragment", "onCancelled: The read failed: " + databaseError.getMessage());
            }
        });
    }

    static void updateAmountSpent(PurchasedItem pItem) {
        for (int i = 0; i<balanceList.size(); i++) {
            Log.d("SettleTheScore", "onClick: balanceList Node " + i + ": " + balanceList.get(i).getAptName());
        }
        populateBalances();
        String userBought = pItem.getUserBought();
        int balanceExistsReturns[] = balanceExists(userBought);
        if (balanceExistsReturns[0] == 0) {
            Log.d("SettleTheScore", "onClick: Apartment Name: " + findApartmentNameByEmail(userBought));
            newBalance = new UserBalance(userBought, findApartmentNameByEmail(userBought), pItem.getPrice(), 0.00);
            balanceList.add(newBalance);
        } else {
            newBalance = new UserBalance(userBought, balanceList.get(balanceExistsReturns[1]).getAptName(),
                    (balanceList.get(balanceExistsReturns[1]).getAmntSpent() + pItem.getPrice()),
                    balanceList.get(balanceExistsReturns[1]).getAmntOwed());
            balanceList.set(balanceExistsReturns[1], newBalance);
        }
        //add item cost to appropriate users amntSpent
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query userQuery = database.getReference("userList").orderByChild("user").equalTo(userBought);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserBalance userBalance = userSnapshot.getValue(UserBalance.class);
                    Log.d("updateAmountSpent", "onDataChange: newAmountSpent: " + newBalance.getAmntSpent());
                    userSnapshot.getRef().setValue(newBalance);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ShoppingListFragment", "onCancelled", databaseError.toException());
            }
        });
    }
    public List<UserBalance> getRoomates(String apartmentName) {
        final List<UserBalance> userBalances = new ArrayList<UserBalance>();
        Query userQuery = balanceRef.orderByChild("aptName").equalTo(apartmentName);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserBalance uBalance = userSnapshot.getValue(UserBalance.class);
                    Log.d("RecentlyPurchased", "getRoomates: user added:" + uBalance.getUser());
                    userBalances.add(uBalance);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ShoppingListFragment", "onCancelled", databaseError.toException());
            }
        });
        Log.d("RecentlyPurchased", "getRoomates: " + userBalances.get(0));
        return userBalances;
    }

    static String findApartmentNameByEmail(String email) {
        apartmentName = " ";
        balanceRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    UserBalance userBalance = postSnapshot.getValue(UserBalance.class);
                    Log.d("findApartmentName", "onDataChange: user" + userBalance.getUser());
                    if (userBalance.getUser().equals(email)) {
                        Log.d("findApartmentName", "onDataChange: apartmentName: " + userBalance.getAptName());
                        apartmentName = userBalance.getAptName();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("PurchasedListFragment", "onCancelled: The read failed: " + databaseError.getMessage());
            }
        });

        return apartmentName;
    }
}
