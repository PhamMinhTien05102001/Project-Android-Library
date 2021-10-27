package com.project.library.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.library.R;
import com.project.library.model.Photo;

import java.util.List;

public class PhotoViewPagerAdapter extends RecyclerView.Adapter<PhotoViewPagerAdapter.PhotoViewHolder> {
    private List<Photo> listPhoto;

    public PhotoViewPagerAdapter(List<Photo> listPhoto) {
        this.listPhoto = listPhoto;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.silde_photo, parent, false);

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = listPhoto.get(position);
        holder.imageView_silde.setImageResource(photo.getResourceId());
    }

    @Override
    public int getItemCount() {
        if(listPhoto == null){
            return 0;
        }
        return listPhoto.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView_silde;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView_silde = itemView.findViewById(R.id.img_photo);
        }
    }
}
