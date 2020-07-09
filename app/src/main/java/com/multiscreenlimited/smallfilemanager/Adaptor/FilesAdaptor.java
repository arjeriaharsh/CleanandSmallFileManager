package com.multiscreenlimited.smallfilemanager.Adaptor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multiscreenlimited.smallfilemanager.Activity.MainActivity;
import com.multiscreenlimited.smallfilemanager.BuildConfig;
import com.multiscreenlimited.smallfilemanager.Models.FilesModel;
import com.multiscreenlimited.smallfilemanager.R;
import com.multiscreenlimited.smallfilemanager.Utils.Unzip;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class FilesAdaptor extends RecyclerView.Adapter<FilesAdaptor.MyViewHolder> {


    private ArrayList<FilesModel> filesList;
    private MainActivity mContext;
    public RelativeLayout mItem;
    private SharedPreferences sharedPreferences;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView popText;
        public ImageView iconView;

        public MyViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.nameText);
            popText = view.findViewById(R.id.sizeText);
            iconView = view.findViewById(R.id.iconView);
            mItem = view.findViewById(R.id.item);
        }
    }

    public FilesAdaptor(ArrayList<FilesModel> filesList, MainActivity mContext) {
        this.filesList = filesList;
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences("myPref", Context.MODE_PRIVATE);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row, parent, false);
                return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

                final FilesModel files = filesList.get(position);
                final String finalFilename = files.getFilePath();

                holder.nameText.setText(files.fileName);


                if (files.isDirectory) {
                    holder.iconView.setImageResource(R.drawable.ic_icon);
                    holder.popText.setText("Folder");
                } else if (files.isZip) {
                    holder.popText.setText(mContext.getString(R.string.zip_string));
                    holder.iconView.setImageResource(R.drawable.ic_zip);
                } else if (files.isAPK) {
                    holder.iconView.setImageResource(R.drawable.ic_android_black_24dp);
                    holder.popText.setText(mContext.getString(R.string.androidapkfile_string));
                } else if (files.isImage) {
                    holder.iconView.setImageResource(android.R.drawable.sym_def_app_icon);
                    holder.popText.setText("Image");
                } else {
                    holder.iconView.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
                    holder.popText.setText(mContext.getString(R.string.file_string));
                }

                mItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (new File(finalFilename).isDirectory()) {
                            Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
                            intent.putExtra("path", finalFilename);
                            mContext.startActivity(intent);
                        } else if (mContext.getFileExtension(new File(finalFilename)).equalsIgnoreCase(".zip")) {
                            final Dialog dialog = new Dialog(mContext);
                            dialog.setTitle("Extracting File");
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.setContentView(R.layout.zip_extract);
                            dialog.setCancelable(true);

                            TextView source = dialog.findViewById(R.id.source);
                            TextView destination = dialog.findViewById(R.id.destination);
                            Button button = dialog.findViewById(R.id.extractHereButton);
                            TextView nameOfFile = dialog.findViewById(R.id.nameOfFile);

                            source.setText("From: " + new File(finalFilename).getPath());
                            destination.setText("To: " + new File(finalFilename).getParent());
                            nameOfFile.setText(new File(finalFilename).getName());

                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Unzip.unZip unzip = new Unzip.unZip(mContext, new File(finalFilename));
                                    unzip.execute();
                                }
                            });
                            dialog.show();
                        } else {
                            File file = new File(finalFilename);
                            Uri photoURI = FileProvider.getUriForFile(mContext,
                                    BuildConfig.APPLICATION_ID + ".provider", file);
                            try {
                                openFile(mContext, file, photoURI);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

                mItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mContext.vibrate(50);
                        String[] itemsOfDialog = {mContext.getString(R.string.delete), mContext.getString(R.string.copy_string), mContext.getString(R.string.compress), mContext.getString(R.string.rename_string),
                                mContext.getString(R.string.properties)};
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(files.fileName);
                        builder.setItems(itemsOfDialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        new AlertDialog.Builder(mContext)
                                                .setTitle("Delete This file.")
                                                .setMessage("Do you really want to delete this file?")
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        Unzip.deleteFile unzip = new Unzip.deleteFile(mContext, new File(finalFilename));
                                                        unzip.execute();
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, null).show();
                                        break;
                                    case 1:
                                        mContext.menuItem1.setVisible(true);
                                        sharedPreferences.edit().putString("copy", files.getFilePath()).apply();
                                        Toast.makeText(mContext, files.getFilePath(), Toast.LENGTH_SHORT).show();
                                        break;

                                    case 2:
                                        Unzip.addZip addZip = new Unzip.addZip(mContext, new File(finalFilename));
                                        addZip.execute();
                                        break;
                                    case 3:
                                        final EditText input = new EditText(mContext);
                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT);
                                        input.setText(files.fileName);
                                        input.setLayoutParams(lp);
                                        new AlertDialog.Builder(mContext)
                                                .setTitle("Rename")
                                                .setView(input)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        new Unzip(mContext).setFileName(new File(finalFilename), input.getText().toString());
                                                    }
                                                })
                                                .show();
                                        break;
                                    case 4:
                                        showItemOptions(mContext, files.getFileName(), files.getFilePath(), String.valueOf(folderSize(new File(finalFilename))) + " MB", mContext.getFileExtension(new File(finalFilename)) );
                                    default:

                                }
                            }
                        });
                        builder.show();

                        return true;
                    }
                });

    }

    public static long folderSize(File directory) {
        if (directory.isDirectory()) {
            long length = 0;
            for (File file : directory.listFiles()) {
                if (file.isFile())
                    length += file.length();
                else
                    length += folderSize(file);
            }
            return (length / (1024 * 1024));
        } else if (directory.isFile()){
            return directory.length() / (1024 * 1024);
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
     return position;
    }


    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }

    public static void openFile(final Context context, File url, final Uri uri) {
        // Create URI
        File file = url;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg")
                || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else if (url.toString().contains(".apk")) {
            intent.setDataAndType(uri,
                    "application/vnd.android.package-archive");
        } else {
            showIntentOthers(context, intent, uri);
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);


    }

    public static void showIntentOthers(final Context context, final Intent intent, final Uri uri) {
        String[] itemsOfDialog = {"Image", "Text", "Video", "Others"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("How do you wanna open it?");
        builder.setItems(itemsOfDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        intent.setDataAndType(uri, "image/");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case 1:
                        intent.setDataAndType(uri, "text/plain");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case 2:
                        intent.setDataAndType(uri, "video/*");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case 3:
                        intent.setDataAndType(uri, "*/*");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    default:
                }
            }
        });
        builder.show();

    }

    public static void showItemOptions(final Context context, final String name, final String location, final String size, final String type) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom, null);
        TextView nameTV;
        TextView locationTV;
        TextView typeTV;
        TextView sizeTV;
        Button closeTV;

        nameTV = view.findViewById(R.id.filename);
        locationTV = view.findViewById(R.id.filelocation);
        typeTV = view.findViewById(R.id.filetype);
        sizeTV = view.findViewById(R.id.filesize);
        closeTV = view.findViewById(R.id.closeFileButton);

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(view);

        nameTV.setText(name);
        locationTV.setText("File Location: " + location);
        typeTV.setText("File Type: " + type);
        sizeTV.setText("File Size: " + size);

        closeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(name);



        dialog.show();


    }


}

