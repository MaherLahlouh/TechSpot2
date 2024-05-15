package com.example.techspot.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Models.CartItemsModel;
import com.example.techspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.cartItemViewHolder> {
    ArrayList<CartItemsModel> cartItemsModels;
    DatabaseReference r = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techspot-fc2c4-default-rtdb.firebaseio.com/");
    FirebaseAuth auth =FirebaseAuth.getInstance();

    public CartItemAdapter(ArrayList<CartItemsModel> cartItemsModels) {
        this.cartItemsModels = cartItemsModels;
    }

    @NonNull
    @Override
    public cartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_view
                ,parent,false);
        return new cartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cartItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.quantityTextView.setText(cartItemsModels.get(position).getCartItemQuantity());
        holder.titleTextView.setText(cartItemsModels.get(position).getCartItemTitle());
        holder.priceTextView.setText(cartItemsModels.get(position).getCartItemPrice());
        holder.deleteItemImage.setImageResource(cartItemsModels.get(position).getCartItemDeleteImage());

        CartItemsModel model = cartItemsModels.get(position);

        holder.deleteItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quan = Integer.parseInt(model.getCartItemQuantity().substring(0,model.getCartItemQuantity().indexOf("x")));
                if (quan == 1) {
                    cartItemsModels.remove(position);
                    r.child("Carts").child(auth.getCurrentUser().getUid()).child(model.getKey()).removeValue();
                    notifyItemRemoved(position);
                } else {
                    model.setCartItemQuantity(String.valueOf(quan - 1)+"x");
                    r.child("Carts").child(auth.getCurrentUser().getUid()).child(model.getKey()).setValue(ServerValue.increment(-1));
                    notifyItemChanged(position);
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return cartItemsModels.size();
    }

    public class cartItemViewHolder extends RecyclerView.ViewHolder{
        TextView quantityTextView,titleTextView,priceTextView;
        ImageView deleteItemImage;
        public cartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            quantityTextView=itemView.findViewById(R.id.quantityTextView);
            titleTextView=itemView.findViewById(R.id.titleTextView);
            priceTextView=itemView.findViewById(R.id.priceTextView);
            deleteItemImage=itemView.findViewById(R.id.deleteItemImage);

        }


    }
}
