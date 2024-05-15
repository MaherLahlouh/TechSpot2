package com.example.techspot.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Interfaces.MainRecyclerViewInterFace;
import com.example.techspot.Models.AccessoriesModel;
import com.example.techspot.Models.PhonesModel;
import com.example.techspot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AccessoriesAdapter extends RecyclerView.Adapter<AccessoriesAdapter.AccessoriesViewHolder> {

    private List<AccessoriesModel> accessoriesModelList;
    private List<AccessoriesModel> filteredAccessories;
    private MainRecyclerViewInterFace mainRecyclerViewInterFace;

    public AccessoriesAdapter(List<AccessoriesModel> accessoriesModelList, List<AccessoriesModel> filteredAccessories, MainRecyclerViewInterFace mainRecyclerViewInterFace) {
        this.accessoriesModelList = accessoriesModelList;
        this.filteredAccessories = filteredAccessories;
        this.mainRecyclerViewInterFace = mainRecyclerViewInterFace;
    }

    public void setItemClickListener(MainRecyclerViewInterFace itemClickListener) {
        this.mainRecyclerViewInterFace = itemClickListener;
    }

    @NonNull
    @Override
    public AccessoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_recyclerview_items, parent, false);
        return new AccessoriesViewHolder(view);
    }

    public void setFilteredAccessoriesModels(List<AccessoriesModel> filteredAccessoriesModels) {
        this.filteredAccessories = filteredAccessoriesModels;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull AccessoriesViewHolder holder, int position) {
        holder.accessoriesTitle.setText(filteredAccessories.get(position).getAccessoriesTitle());
        holder.accessoriesPrice.setText(filteredAccessories.get(position).getAccessoriesPrice() + " JOD");
        String imageUrl = filteredAccessories.get(position).getAccessoriesImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.accessoriesImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainRecyclerViewInterFace != null) {
                    mainRecyclerViewInterFace.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredAccessories.size();
    }

    public void filterAccessoriesBrand(List<String> accessoriesBrand, boolean isChecked) {
        filteredAccessories.clear();
        for (AccessoriesModel accessories : accessoriesModelList) {
            if (!isChecked || accessoriesBrand.contains(accessories.getAccessoriesTitle())) {
                filteredAccessories.add(accessories);
            }
        }
        notifyDataSetChanged();
    }


    public void filter(String filterItem, boolean isChecked) {
        filteredAccessories.clear();
        for (AccessoriesModel item : accessoriesModelList) {
            if (!isChecked || item.getAccessoriesTitle().equalsIgnoreCase(filterItem)) {
                filteredAccessories.add(item);
            }
        }
        notifyDataSetChanged();
    }


    public class AccessoriesViewHolder extends RecyclerView.ViewHolder {

        private TextView accessoriesTitle, accessoriesPrice;
        private ImageView accessoriesImage;

        public AccessoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            accessoriesTitle = itemView.findViewById(R.id.itemTitle);
            accessoriesImage = itemView.findViewById(R.id.itemImage);
            accessoriesPrice = itemView.findViewById(R.id.itemPrice);
        }
    }
}
