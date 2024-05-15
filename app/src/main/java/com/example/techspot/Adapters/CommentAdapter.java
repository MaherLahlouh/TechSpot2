package com.example.techspot.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techspot.Models.CommentModel;
import com.example.techspot.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.commentViewHolder> {
    ArrayList<CommentModel> commentModels;

    public CommentAdapter(ArrayList<CommentModel> commentModels) {
        this.commentModels = commentModels;
    }

    @NonNull
    @Override
    public commentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_view,parent,false);
        return new commentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull commentViewHolder holder, int position) {
        holder.name.setText(commentModels.get(position).getName());
        holder.comment.setText(commentModels.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }

    public class commentViewHolder extends RecyclerView.ViewHolder{
        TextView name,comment;
        public commentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
            comment = itemView.findViewById(R.id.commenttextview);
        }
    }

    }
