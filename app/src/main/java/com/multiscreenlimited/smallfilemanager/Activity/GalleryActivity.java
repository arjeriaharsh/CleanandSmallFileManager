package com.multiscreenlimited.smallfilemanager.Activity;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.multiscreenlimited.smallfilemanager.Adaptor.ImageHolder;
import com.multiscreenlimited.smallfilemanager.Models.ImageModel;
import com.multiscreenlimited.smallfilemanager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryActivity extends AppCompatActivity {
    public Toolbar toolbar;

    private RecyclerView rvImages;
    private RecyclerView.Adapter adapter;
    public static ArrayList<ImageModel> allImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setTitle("Gallery");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rvImages = findViewById(R.id.recycler_view);
        rvImages.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ImageHolder(allImages,
                GalleryActivity.this);
        getAllShownImagesPath(this);

    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return true;
    }

    private void getAllShownImagesPath(Activity activity) {
        allImages.clear();
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            File imageFile = new File(absolutePathOfImage);
            ImageModel imageModel = new ImageModel();
            imageModel.setUrl(absolutePathOfImage);
            imageModel.setImageFile(imageFile);
            imageModel.setDateModified(String.valueOf(imageFile.lastModified()));
            allImages.add(imageModel);
        }

        Collections.sort(allImages, new Comparator<ImageModel>() {
            @Override
            public int compare(ImageModel s1, ImageModel s2) {
                return s2.getDateModified().compareToIgnoreCase(s1.getDateModified());
            }
        });
        rvImages.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllShownImagesPath(this);
    }



}
