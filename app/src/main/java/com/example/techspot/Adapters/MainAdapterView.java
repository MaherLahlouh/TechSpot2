package com.example.techspot.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Interfaces.MainRecyclerViewInterFace;
import com.example.techspot.R;

import java.util.List;

public class MainAdapterView extends RecyclerView.Adapter<MainAdapterView.MyViewHolder> {

    List<String> myTitlesMain;
    List<Integer>myImagesMain;
    MainRecyclerViewInterFace rvClick;

    public MainAdapterView(List<String> myTitlesMain, List<Integer> myImagesMain, MainRecyclerViewInterFace rvClick) {
        this.myTitlesMain = myTitlesMain;
        this.myImagesMain = myImagesMain;
        this.rvClick = rvClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_recyclerview
                ,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myTitle.setText(myTitlesMain.get(position));
        holder.myImage.setImageResource(myImagesMain.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvClick.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myImagesMain.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView myImage;
        TextView myTitle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myImage=itemView.findViewById(R.id.itemImageView);
            myTitle=itemView.findViewById(R.id.itemTextView);
        }
    }
}
