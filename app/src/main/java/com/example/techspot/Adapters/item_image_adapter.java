package com.example.techspot.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class item_image_adapter extends RecyclerView.Adapter<item_image_adapter.itemImageViewHolder> {
    private Context context;
    private List<String> imageUrls; // Change the type to String for URLs


    public item_image_adapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public itemImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new itemImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);

        // Load image using Picasso
        Picasso.get().load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class itemImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public itemImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

