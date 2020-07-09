package com.multiscreenlimited.smallfilemanager.Activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.multiscreenlimited.smallfilemanager.Adaptor.FilesAdaptor;
import com.multiscreenlimited.smallfilemanager.Models.FilesModel;
import com.multiscreenlimited.smallfilemanager.R;
import com.multiscreenlimited.smallfilemanager.Utils.Unzip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity {

    public String path;
    public MainActivity mainActivity;
    public static String TAG = "MainActivity";
    private static String sortTypeString;
    public Toolbar toolbar;
    private ArrayList<FilesModel> filesArrayList;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter2;
    private InterstitialAd mInterstitialAd;
    private com.facebook.ads.InterstitialAd interstitialAd;
    public String copyFileName;
    private SharedPreferences sharedPreferences;
    public Menu navMenuLogIn;
    public MenuItem menuItem1, menuItem2, menuItem3;
    private FloatingActionsMenu rightLabels;
    private FloatingActionButton rightLabels_1, rightLabels_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3643397521517163/9089466582");


        interstitialAd = new com.facebook.ads.InterstitialAd(this, "2457641170935866_2468830076483642");

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
                interstitialAd.loadAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });
        if (!interstitialAd.isAdLoaded())
            interstitialAd.loadAd();

        toolbar = findViewById(R.id.toolbar);

        rightLabels = findViewById(R.id.right_labels);
        rightLabels_1 = new FloatingActionButton(this);
        rightLabels_2 = new FloatingActionButton(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        invalidateOptionsMenu();

        rightLabels_1.setTitle(getString(R.string.arrangedate_string));
        rightLabels_2.setTitle(getString(R.string.arrangename_string));
        rightLabels_1.setSize(FloatingActionButton.SIZE_MINI);
        rightLabels_2.setSize(FloatingActionButton.SIZE_MINI);
        rightLabels_1.setColorNormalResId(R.color.colorPrimaryDark);
        rightLabels_2.setColorNormalResId(R.color.colorPrimaryDark);
        rightLabels_1.setColorPressedResId(R.color.colorPrimaryDark);
        rightLabels_2.setColorPressedResId(R.color.colorPrimaryDark);
        rightLabels_1.setIcon(R.drawable.ic_date_range_black_24dp);
        rightLabels_2.setIcon(R.drawable.ic_format_color_text_black_24dp);
        rightLabels.addButton(rightLabels_1);
        rightLabels.addButton(rightLabels_2);

        rightLabels_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(sortTypeString, 1).apply();
                updateListView();
            }
        });

        rightLabels_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(sortTypeString, 2).apply();
                updateListView();
            }
        });

        path = getPath();
        setTitle(path);

        rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        int spacingInPixels = 9;
        rv.addItemDecoration(new FilesAdaptor.SpacesItemDecoration(spacingInPixels));

        if (sharedPreferences.getBoolean("firststart", true)) {
            showCaseView();
            sharedPreferences.edit().putBoolean("firststart", false).apply();
        }

        loadAd();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
               loadAd();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateListView();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_manu, menu);
        navMenuLogIn = menu;
        menuItem1 = navMenuLogIn.findItem(R.id.item1);
        menuItem2 = navMenuLogIn.findItem(R.id.item2);
        menuItem3 = navMenuLogIn.findItem(R.id.item3);
        updateMenuTitles();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                String currentPath = getPath();
                Unzip.copyData unzip = new Unzip.copyData(mainActivity, copyFileName, currentPath);
                unzip.execute();
                sharedPreferences.edit().remove("copy").apply();
                menuItem1.setVisible(false);
                return true;
            case R.id.item2:
                String[] itemsOfDialog = {getString(R.string.appmanage_string), getString(R.string.lagtv_youtube_string)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.settings_string));
                builder.setItems(itemsOfDialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                try {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                    startActivity(intent);
                                }
                                break;
                            case 1:
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("https://www.youtube.com/channel/UCWlcN1iDirTn9CL0_gAktTw"));
                                startActivity(intent);
                                break;
                            default:
                        }
                    }
                });
                builder.show();
                return true;
            case R.id.item3:
                final EditText input = new EditText(mainActivity);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText(getString(R.string.new_folder_string));
                input.setLayoutParams(lp);
                new AlertDialog.Builder(mainActivity)
                        .setTitle(getString(R.string.rename_string))
                        .setView(input)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Unzip(mainActivity).createFolder(getPath(), input.getText().toString().trim());
                            }
                        })
                        .show();
                return true;
            case R.id.item4:
                final EditText input2 = new EditText(mainActivity);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input2.setText(getString(R.string.new_file));
                input2.setLayoutParams(lp2);
                new AlertDialog.Builder(mainActivity)
                        .setTitle(getString(R.string.new_file))
                        .setView(input2)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    new Unzip(mainActivity).createFile(getPath(), input2.getText().toString().trim());
                                } catch (IOException e) {
                                    Toast.makeText(mainActivity, e.toString(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        })
                        .show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void updateMenuTitles() {
        if (navMenuLogIn != null) {
            if (sharedPreferences.contains("copy")) {
                copyFileName = sharedPreferences.getString("copy", null);
                menuItem1.setVisible(true);
            } else {
                menuItem1.setVisible(false);
            }
        }
    }

    public void updateListView() {
        int sortType = sharedPreferences.getInt(sortTypeString, 2);

            filesArrayList = new ArrayList<>();
            File dir = new File(path);
            if (!dir.canRead()) {
                setTitle(getTitle() + " (inaccessible)");
            }

            File directory = new File(path);
            File[] files = directory.listFiles();
            for (File file : files) {
                String fileName = file.getName();
                FilesModel newApp = new FilesModel();
                newApp.setFileName(fileName);
                newApp.setFilePath(file.getPath());

                if (file.isDirectory()) {
                    newApp.setDirectory(true);
                } else {
                    newApp.setDirectory(false);
                }

                if (getFileExtension(file).equalsIgnoreCase(".apk")) {
                    newApp.setAPK(true);
                    newApp.setDirectory(false);
                    newApp.setZip(false);
                    newApp.setImage(false);
                } else {
                    newApp.setAPK(false);
                }

                if (getFileExtension(file).equalsIgnoreCase(".zip")) {
                    newApp.setAPK(false);
                    newApp.setDirectory(false);
                    newApp.setZip(true);
                    newApp.setImage(false);
                } else {
                    newApp.setZip(false);
                }

                if (getFileExtension(file).equalsIgnoreCase(".jpeg") || getFileExtension(file).equalsIgnoreCase(".jpg") ||
                        getFileExtension(file).equalsIgnoreCase(".png")) {
                    newApp.setAPK(false);
                    newApp.setDirectory(false);
                    newApp.setZip(false);
                    newApp.setImage(true);
                } else {
                    newApp.setImage(false);
                }
                newApp.setDateCreated(file.lastModified());

                filesArrayList.add(newApp);
            }

            if (sortType == 2) {
                Collections.sort(filesArrayList, new Comparator<FilesModel>() {
                    @Override
                    public int compare(FilesModel s1, FilesModel s2) {
                        return s1.getFileName().compareToIgnoreCase(s2.getFileName());
                    }
                });
            }else if (sortType == 1){
                Collections.sort(filesArrayList, new Comparator<FilesModel>() {
                    @Override
                    public int compare(FilesModel s1, FilesModel s2) {
                        return s2.getDateCreated().toString().compareToIgnoreCase(s1.getDateCreated().toString());
                    }
                });
            }

            adapter2 = new FilesAdaptor(filesArrayList,
                    MainActivity.this);
            rv.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();
        }



    @Override
    public boolean onSupportNavigateUp() {
        if (getIntent().hasExtra("path")) {
            onBackPressed();
        } else {
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        interstitialAd.destroy();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        interstitialAd.destroy();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        interstitialAd.destroy();
        super.onDestroy();
    }

    public String getFileExtension(File file) {
        String extension = "";

        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            extension = "";
        }

        return extension;

    }

    public String getPath() {
        String path = "";
        path = Environment.getExternalStorageDirectory().toString();
        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
        }
        return path;
    }

    public void vibrate(int milliseconds) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(milliseconds);
        }
    }


    public void showInterstialAd() {
        if (interstitialAd.isAdLoaded()) {
            interstitialAd.show();
        } else if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
        }
    }

    public void loadAd(){
        if (ConsentInformation.getInstance(MainActivity.this).getConsentStatus() == ConsentStatus.PERSONALIZED) {

                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("2FC8EBE8F595EC01D561955CFADE63C2").build());

        }else if (ConsentInformation.getInstance(MainActivity.this).getConsentStatus() == ConsentStatus.UNKNOWN || ConsentInformation.getInstance(MainActivity.this).getConsentStatus() == ConsentStatus.NON_PERSONALIZED){
            mInterstitialAd.loadAd(new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).addTestDevice("2FC8EBE8F595EC01D561955CFADE63C2").build());
        }
    }

    public Bundle getNonPersonalizedAdsBundle() {
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        return extras;
    }

    public void showCaseView() {
        View view = findViewById(R.id.viewNormal);
        View view2 = findViewById(R.id.viewNormal2);

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);
        sequence.setConfig(config);
        sequence.addSequenceItem(view,
                getString(R.string.showcase1), getString(android.R.string.ok));
        sequence.addSequenceItem(view,
                getString(R.string.showcase2), getString(android.R.string.ok));
        sequence.addSequenceItem(view2,
                getString(R.string.showcase3), getString(android.R.string.ok));
        sequence.addSequenceItem(view,
                getString(R.string.showcase4), getString(android.R.string.ok));
        sequence.addSequenceItem(rightLabels,
                getString(R.string.showcase5), getString(android.R.string.ok));
        sequence.start();
    }
}