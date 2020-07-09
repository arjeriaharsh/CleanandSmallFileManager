package com.multiscreenlimited.smallfilemanager.Activity;

import android.content.Intent;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.multiscreenlimited.smallfilemanager.R;

public class StartActivity extends AppCompatActivity {

    private String pathForExternalStorage;
    private String pathForInternal;

    public Toolbar toolbar;


    private Button external, data, downloads, images, internal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (!AudienceNetworkAds.isInitialized(this)) {
            MobileAds.initialize(this, "ca-app-pub-3643397521517163~6582066207");
            AudienceNetworkAds.initialize(this);
        }

        pathForExternalStorage = Environment.getExternalStorageDirectory().toString();
        pathForInternal = Environment.getRootDirectory().toString();



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_home_black_24dp);

        external = findViewById(R.id.external);
        data = findViewById(R.id.data);
        images = findViewById(R.id.images);
        downloads = findViewById(R.id.downloads);
        internal = findViewById(R.id.internal);

        external.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("path", pathForExternalStorage);
                startActivity(intent);
            }
        });

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MusicActivity.class);
                startActivity(intent);
            }
        });
        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });
        downloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("path", pathForExternalStorage + "/Downloads/");
                startActivity(intent);
            }
        });

        internal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("path", pathForInternal);
                startActivity(intent);
            }
        });
    }
}
