package com.example.techspot.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Interfaces.MainRecyclerViewInterFace;
import com.example.techspot.Models.LaptopModel;
import com.example.techspot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LaptopAdapters extends RecyclerView.Adapter<LaptopAdapters.LaptopViewHolder> {
    private List<LaptopModel> laptopModels;
    private List<LaptopModel> filteredLaptop;
    private MainRecyclerViewInterFace mainRecyclerViewInterFace;

    public LaptopAdapters(List<LaptopModel> laptopModels, List<LaptopModel> filteredLaptop, MainRecyclerViewInterFace mainRecyclerViewInterFace) {
        this.laptopModels = laptopModels;
        this.filteredLaptop = filteredLaptop;
        this.mainRecyclerViewInterFace = mainRecyclerViewInterFace;
    }

    public void setItemClickListener(MainRecyclerViewInterFace itemClickListener) {
        this.mainRecyclerViewInterFace = itemClickListener;
    }

    @NonNull
    @Override
    public LaptopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_recyclerview_items, parent, false);
        return new LaptopViewHolder(view);
    }

    public void setFilteredLaptopModels(List<LaptopModel> filteredLaptopModels) {
        this.filteredLaptop = filteredLaptopModels;
        notifyDataSetChanged();  // Notify the adapter of the new filtered data
    }

    @Override
    public void onBindViewHolder(@NonNull LaptopViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.laptopTitle.setText(filteredLaptop.get(position).getLaptopTitle());
        holder.laptopPrice.setText(filteredLaptop.get(position).getLaptopPrice()+"JOD");
        String imageUrl=filteredLaptop.get(position).getLaptopImage();
        if (imageUrl !=null && !imageUrl.isEmpty()){
            Picasso.get().load(imageUrl).into(holder.laptopImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mainRecyclerViewInterFace!=null){
                    mainRecyclerViewInterFace.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredLaptop.size();
    }

    public void filterLaptopBrand(List<String> laptopBrand, boolean isChecked) {
        filteredLaptop.clear();
        for (LaptopModel laptop : laptopModels) {
            for (String brand : laptopBrand) {
                if (!isChecked || laptop.getLaptopTitle().toLowerCase().contains(brand.toLowerCase())) {
                    filteredLaptop.add(laptop);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }


    public void filter(String filterItem, boolean isChecked) {
        filteredLaptop.clear();
        for (LaptopModel item : laptopModels) {
            if (!isChecked || item.getLaptopTitle().equalsIgnoreCase(filterItem)) {
                filteredLaptop.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public void filterByLaptopName(String laptopName, boolean isChecked) {
        filteredLaptop.clear();
        for (LaptopModel laptop : laptopModels) {
            if (!isChecked || laptop.getLaptopTitle().toLowerCase().contains(laptopName.toLowerCase())) {
                filteredLaptop.add(laptop);
            }
        }
        notifyDataSetChanged();
    }

    public class LaptopViewHolder extends RecyclerView.ViewHolder {
        TextView laptopTitle, laptopPrice;
        ImageView laptopImage;

        public LaptopViewHolder(@NonNull View itemView) {
            super(itemView);
            laptopTitle = itemView.findViewById(R.id.itemTitle);
            laptopImage = itemView.findViewById(R.id.itemImage);
            laptopPrice = itemView.findViewById(R.id.itemPrice);
        }
    }
}
