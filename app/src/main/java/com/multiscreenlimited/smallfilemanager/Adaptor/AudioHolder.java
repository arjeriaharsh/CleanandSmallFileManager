package com.multiscreenlimited.smallfilemanager.Adaptor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.multiscreenlimited.smallfilemanager.Activity.GalleryActivity;
import com.multiscreenlimited.smallfilemanager.Activity.MusicActivity;
import com.multiscreenlimited.smallfilemanager.Activity.PhotoViewerGalleryActivity;
import com.multiscreenlimited.smallfilemanager.Models.AudioModel;
import com.multiscreenlimited.smallfilemanager.Models.ImageModel;
import com.multiscreenlimited.smallfilemanager.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AudioHolder extends  RecyclerView.Adapter<AudioHolder.ViewHolderImage>{

    private ArrayList<AudioModel> imageList;
    private MusicActivity mContext;

    public AudioHolder(ArrayList<AudioModel> imageList, MusicActivity mContext){
        this.imageList = imageList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolderImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_items, parent, false);
        return new AudioHolder.ViewHolderImage(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImage holder, int position) {
        final AudioModel imageModel = imageList.get(position);

        holder.main.setText(imageModel.getTitle());

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public class ViewHolderImage extends RecyclerView.ViewHolder {
        private TextView main;

        public ViewHolderImage(View view) {
            super(view);
            main = view.findViewById(R.id.textName);
        }
    }

}
