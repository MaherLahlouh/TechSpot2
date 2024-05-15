package com.example.techspot.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.techspot.Models.DetailsModel;
import com.example.techspot.R;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {
    private List<DetailsModel> detailsModels;


    public DetailsAdapter(List<DetailsModel> detailsModels) {
        this.detailsModels = detailsModels;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_details, parent, false);
        return new DetailsViewHolder(view);
    }

    // ...

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        DetailsModel detailsModel = detailsModels.get(position);

        // Set data to views
        holder.osVersion.setText(detailsModel.getOsName());
        holder.cpuDescription.setText(detailsModel.getCpuDescription());
        holder.ramSize.setText(detailsModel.getRamSize());
        holder.backCameraInfo.setText(detailsModel.getBackCameraInfo());
        holder.frontCameraInfo.setText(detailsModel.getFrontCameraInfo());
        holder.displayInfo.setText(detailsModel.getDisplayInfo());
        holder.batteryInfo.setText(detailsModel.getBatteryInfo());
        holder.memorySize.setText(detailsModel.getMemorySize());
        holder.gpuInfo.setText(detailsModel.getGpuInfo());
        holder.price.setText(detailsModel.getPrice());

        // Set up item_image_adapter for ViewPager2
        item_image_adapter imageAdapter = new item_image_adapter(holder.itemView.getContext(), detailsModel.getImages());
        holder.viewPager2.setAdapter(imageAdapter);
    }




    @Override
    public int getItemCount() {
        return detailsModels.size();

    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder {
        TextView osVersion, cpuDescription, ramSize, backCameraInfo, frontCameraInfo, displayInfo,
                batteryInfo, memorySize, gpuInfo, price;
        ViewPager2 viewPager2;

        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            osVersion = itemView.findViewById(R.id.osText);
            cpuDescription = itemView.findViewById(R.id.cpuText);
//            cpuDescription = itemView.findViewById(R.id.networkText);
            ramSize = itemView.findViewById(R.id.ramText);
            backCameraInfo = itemView.findViewById(R.id.backCameraText);
            frontCameraInfo = itemView.findViewById(R.id.frontCameraText);
            displayInfo = itemView.findViewById(R.id.displayText);
            batteryInfo = itemView.findViewById(R.id.batteryText);
            memorySize = itemView.findViewById(R.id.memoryText);
            gpuInfo = itemView.findViewById(R.id.gpuText);
            price = itemView.findViewById(R.id.priceText);
            viewPager2=itemView.findViewById(R.id.viewPager2);
        }
    }
}
