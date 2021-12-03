package edu.uga.cs.shoppinglistapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PurchasedRecyclerAdapter extends RecyclerView.Adapter<PurchasedRecyclerAdapter.PurchasedItemHolder> {
    private List<PurchasedItem> purchasedList;
    //Comment
    public PurchasedRecyclerAdapter(List<PurchasedItem> pList) {
        purchasedList = pList;
    }
    public class PurchasedItemHolder extends RecyclerView.ViewHolder {
        TextView itemPurchased;
        TextView price;
        TextView userBought;

        public PurchasedItemHolder(View itemView) {
            super(itemView);

            itemPurchased = (TextView) itemView.findViewById(R.id.purchasedItem);
            price  = (TextView) itemView.findViewById(R.id.cost);
            userBought = (TextView) itemView.findViewById(R.id.userBoughtText);
        }
    }

    public PurchasedRecyclerAdapter.PurchasedItemHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.purchased_item, parent, false );
        return new PurchasedRecyclerAdapter.PurchasedItemHolder( view );
    }

    public void onBindViewHolder( PurchasedItemHolder holder, int position ) {
        PurchasedItem purchasedItem = purchasedList.get( position );

        holder.itemPurchased.setText((CharSequence) purchasedItem.getItemPurchased());
        holder.price.setText( String.valueOf( purchasedItem.getPrice()));
        holder.userBought.setText(purchasedItem.getUserBought());

    }

    @Override
    public int getItemCount() {
        Log.d("PurchasedRecycler", "getItemCount: " + purchasedList.size());
        return purchasedList.size();
    }
}
