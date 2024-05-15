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
import com.example.techspot.Models.ComputerModel;
import com.example.techspot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComputerAdapter extends RecyclerView.Adapter<ComputerAdapter.myComputerViewHolder> {

    List<ComputerModel> computerModelList;

    private List<ComputerModel> filteredComputer;
    private MainRecyclerViewInterFace mainRecyclerViewInterFace;

    public ComputerAdapter(List<ComputerModel> computerModelList, List<ComputerModel> filteredComputer, MainRecyclerViewInterFace mainRecyclerViewInterFace) {
        this.computerModelList = computerModelList;
        this.filteredComputer = filteredComputer;
        this.mainRecyclerViewInterFace = mainRecyclerViewInterFace;
    }

    @NonNull
    @Override
    public myComputerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_recyclerview_items
                ,parent,false);
        return new myComputerViewHolder(view);
    }

    public void setFilteredComputerModels(List<ComputerModel> filteredComputerModels) {
        this.filteredComputer = filteredComputerModels;
        notifyDataSetChanged();  // Notify the adapter of the new filtered data
    }

    @Override
    public void onBindViewHolder(@NonNull myComputerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.computerTitle.setText(filteredComputer.get(position).getComputerTitle());

        holder.computerPrice.setText(filteredComputer.get(position).getComputerPrice()+"JOD");

        String imageUrl=filteredComputer.get(position).getComputerImage();
        if (imageUrl !=null && !imageUrl.isEmpty()){
            Picasso.get().load(imageUrl).into(holder.computerImage);
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
        return filteredComputer.size();
    }
    public void filterComputerBrand(List<String> computerBrand, boolean isChecked) {
        filteredComputer.clear();
        for (ComputerModel computer : computerModelList) {
            if (!isChecked ||computerBrand.contains(computer.getComputerTitle())) {
                filteredComputer.add(computer);
            }
        }
        notifyDataSetChanged();
    }

    public class myComputerViewHolder extends RecyclerView.ViewHolder{

        TextView computerTitle, computerPrice;
        ImageView computerImage;
        public myComputerViewHolder(@NonNull View itemView) {
            super(itemView);
            computerTitle = itemView.findViewById(R.id.itemTitle);
            computerImage = itemView.findViewById(R.id.itemImage);
            computerPrice = itemView.findViewById(R.id.itemPrice);
        }
    }

    public void filter(String filterItem, boolean isChecked) {
        filteredComputer.clear();
        for (ComputerModel item : computerModelList) {
            if (!isChecked || item.getComputerTitle().equalsIgnoreCase(filterItem)) {
                filteredComputer.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public void filterByComputerName(String laptopName, boolean isChecked) {
        filteredComputer.clear();
        for (ComputerModel computer : computerModelList) {
            if (!isChecked || computer.getComputerTitle().toLowerCase().contains(laptopName.toLowerCase())) {
                filteredComputer.add(computer);
            }
        }
        notifyDataSetChanged();
    }
}

