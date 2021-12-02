package edu.uga.cs.shoppinglistapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroceryRecyclerAdapter extends RecyclerView.Adapter<GroceryRecyclerAdapter.GroceryItemHolder> {
    private List<GroceryItem> shoppingList;
    private List<GroceryItem> purchasingList;

    public GroceryRecyclerAdapter(List<GroceryItem> sList) {
        shoppingList = sList;
    }
    public class GroceryItemHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView description;
        TextView userSubmitted;
        Button purchase;

        public GroceryItemHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.purchasedItem);
            description = (TextView) itemView.findViewById(R.id.cost);
            userSubmitted = (TextView) itemView.findViewById(R.id.userBoughtText);
            purchase = (Button) itemView.findViewById(R.id.remove);
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

            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }
}
