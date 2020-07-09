package com.multiscreenlimited.smallfilemanager.Activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.multiscreenlimited.smallfilemanager.BuildConfig;
import com.multiscreenlimited.smallfilemanager.R;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewerGalleryActivity extends AppCompatActivity {

    private String photoID;
    private ImageView mainImage;

    private Toolbar toolbar;
    private File photoFile;
    private ImageButton shareButtonImage;
    private ImageButton deleteButtonImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer_gallery);

        photoID = getPhotoId();

        toolbar = findViewById(R.id.toolbar);
        mainImage = findViewById(R.id.mainImage);
        shareButtonImage = findViewById(R.id.shareButtonImage);
        deleteButtonImage = findViewById(R.id.deleteButtonImage);

        setSupportActionBar(toolbar);

        if (!photoID.isEmpty()){
            photoFile = new File(photoID);
            Glide.with(this).load(photoFile).into(mainImage);
            setTitle(photoFile.getName());
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        PhotoViewAttacher photoAttacher;
        photoAttacher= new PhotoViewAttacher(mainImage);
        photoAttacher.update();

        shareButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(photoFile);
            }
        });
        deleteButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogOK("Are you sure?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteFileFromMediaStore(getContentResolver(), photoFile);
                                    Toast.makeText(PhotoViewerGalleryActivity.this, "File deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                Toast.makeText(PhotoViewerGalleryActivity.this, "Not deleted", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
        });

    }
    public String getPhotoId() {
        String path = "";
        path = Environment.getExternalStorageDirectory().toString();
        if (getIntent().hasExtra("photoid")) {
            path = getIntent().getStringExtra("photoid");
        }
        return path;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void SwapVisibility(View view){
        if (view.getVisibility() == View.VISIBLE){
            fadeOutAndHideImage(view);
        }else{
            fadeInAndShowImage(view);
        }
    }

    private void fadeOutAndHideImage(final View img)
    {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(200);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                img.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        img.startAnimation(fadeOut);
    }
    private void fadeInAndShowImage(final View img)
    {
        Animation fadeOut = new AlphaAnimation(0, 1);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(200);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                img.setVisibility(View.VISIBLE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        img.startAnimation(fadeOut);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    void shareImage(File filePath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID + ".provider",filePath));
        startActivity(Intent.createChooser(intent,"Share with..."));
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    public static void deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri,
                MediaStore.Files.FileColumns.DATA + "=?", new String[] {canonicalPath});
        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
            }
        }
    }
}
