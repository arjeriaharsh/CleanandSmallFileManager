package com.multiscreenlimited.smallfilemanager.Adaptor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.multiscreenlimited.smallfilemanager.Activity.GalleryActivity;
import com.multiscreenlimited.smallfilemanager.Models.ImageModel;
import com.multiscreenlimited.smallfilemanager.Activity.PhotoViewerGalleryActivity;
import com.multiscreenlimited.smallfilemanager.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageHolder extends  RecyclerView.Adapter<ImageHolder.ViewHolderImage>{

    private ArrayList<ImageModel> imageList;
    private GalleryActivity mContext;

    public ImageHolder(ArrayList<ImageModel> imageList, GalleryActivity mContext){
        this.imageList = imageList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolderImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_layout, parent, false);
        return new ImageHolder.ViewHolderImage(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImage holder, int position) {
        final ImageModel imageModel = imageList.get(position);

//        Picasso.get().load(imageModel.getImageFile()).resize(128,128).memoryPolicy(MemoryPolicy.NO_STORE).into(holder.main);
        Glide.with(mContext).load(imageModel.getImageFile()).skipMemoryCache(true).into(holder.main);

        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openPhoto = new Intent(mContext, PhotoViewerGalleryActivity.class);
                openPhoto.putExtra("photoid", imageModel.getUrl());
                mContext.startActivity(openPhoto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public class ViewHolderImage extends RecyclerView.ViewHolder {
        private ImageView main;

        public ViewHolderImage(View view) {
            super(view);
            main = view.findViewById(R.id.main);
        }
    }

}
