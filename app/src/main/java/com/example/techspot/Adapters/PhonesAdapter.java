package com.example.techspot.Adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Interfaces.MainRecyclerViewInterFace;
import com.example.techspot.Models.PhonesModel;
import com.example.techspot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhonesAdapter extends RecyclerView.Adapter<PhonesAdapter.phonesViewHolder> {

    private List<PhonesModel> phonesModels;
    private List<PhonesModel> phonesFiltered;
    private MainRecyclerViewInterFace mainRecyclerViewInterFace;

    public PhonesAdapter(List<PhonesModel> phonesModels, List<PhonesModel> phonesFiltered, MainRecyclerViewInterFace mainRecyclerViewInterFace) {
        this.phonesModels = phonesModels;
        this.phonesFiltered = phonesFiltered;
        this.mainRecyclerViewInterFace = mainRecyclerViewInterFace;
    }

    public void setItemClickListener(MainRecyclerViewInterFace itemClickListener) {
        this.mainRecyclerViewInterFace = itemClickListener;
    }

    @NonNull
    @Override
    public phonesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_recyclerview_items, parent, false);
        return new phonesViewHolder(view);
    }
    public void setFilteredPhonesModels(List<PhonesModel> filteredPhonesModels) {
        this.phonesFiltered = filteredPhonesModels;
        notifyDataSetChanged();  // Notify the adapter of the new filtered data
    }

    @Override
    public void onBindViewHolder(@NonNull phonesViewHolder holder, int position) {
        holder.phoneTitle.setText(phonesFiltered.get(position).getPhoneTitle());
        holder.phonePrice.setText(phonesFiltered.get(position).getPhonePrice()+" JOD");
        String imageUrl=phonesFiltered.get(position).getPhoneImage();
        if (imageUrl !=null && !imageUrl.isEmpty()){
            Picasso.get().load(imageUrl).into(holder.phoneImage);
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
        return phonesFiltered.size();
    }

    public void filterPhoneBrand(List<String> phoneBrand, boolean isChecked) {
        phonesFiltered.clear();
        for (PhonesModel phone : phonesModels) {
            if (!isChecked || phoneBrand.contains(phone.getPhoneTitle())) {
                phonesFiltered.add(phone);
            }
        }
        notifyDataSetChanged();
    }



    public void filterLaptopBrand(List<String> laptopBrand, boolean isChecked) {
        phonesFiltered.clear();
        for (PhonesModel phone :phonesModels) {
            for (String brand : laptopBrand) {
                if (!isChecked || phone.getPhoneTitle().toLowerCase().contains(brand.toLowerCase())) {
                    phonesFiltered.add(phone);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public class phonesViewHolder extends RecyclerView.ViewHolder{
        TextView phoneTitle, phonePrice;
        ImageView phoneImage;

        public phonesViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneTitle = itemView.findViewById(R.id.itemTitle);
            phoneImage = itemView.findViewById(R.id.itemImage);
            phonePrice = itemView.findViewById(R.id.itemPrice);

        }
    }
}
