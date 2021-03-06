package edu.uga.cs.shoppinglistapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroceryRecyclerAdapter extends RecyclerView.Adapter<GroceryRecyclerAdapter.GroceryItemHolder> {
    private List<GroceryItem> shoppingList;
    private List<GroceryItem> purchasingList;
    private FragmentActivity context;

    public GroceryRecyclerAdapter(List<GroceryItem> sList, FragmentActivity context) {
        shoppingList = sList;
        this.context = context;
    }
    public class GroceryItemHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView description;
        TextView userSubmitted;
        Button purchase;
        Button remove;

        public GroceryItemHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.purchasedItem);
            description = (TextView) itemView.findViewById(R.id.cost);
            userSubmitted = (TextView) itemView.findViewById(R.id.userBoughtText);
            purchase = (Button) itemView.findViewById(R.id.remove);
            remove = (Button) itemView.findViewById(R.id.button7);
        }
    }

    @Override
    public GroceryItemHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.grocery_item, parent, false );
        return new GroceryItemHolder( view );
    }

    @Override
    public void onBindViewHolder( GroceryItemHolder holder, int position ) {
        GroceryItem groceryItem = shoppingList.get( position );


        holder.itemName.setText( groceryItem.getItemName());
        holder.description.setText(groceryItem.getDescription());
        holder.userSubmitted.setText("added by: " + groceryItem.getUserSubmitted());

        holder.purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = PurchaseItemDialogFragment.newInstance(groceryItem);
                ((ShoppingListFragment)context.getSupportFragmentManager().findFragmentByTag("f0")).showDialogFragment(newFragment);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShoppingListFragment)context.getSupportFragmentManager().findFragmentByTag("f0")).removeGroceryItem(groceryItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }
}
