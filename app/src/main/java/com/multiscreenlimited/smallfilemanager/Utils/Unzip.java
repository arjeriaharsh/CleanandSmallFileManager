package com.multiscreenlimited.smallfilemanager.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.multiscreenlimited.smallfilemanager.Activity.MainActivity;
import com.multiscreenlimited.smallfilemanager.R;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;


public class Unzip {

    public MainActivity context;

    public Unzip(MainActivity mainActivity) {
        context = mainActivity;
    }


    public static void unzip(Context context,String Filepath, String DestinationFolderPath) {
        try {
            ZipFile zipFile = new ZipFile(Filepath);
            if (zipFile.isEncrypted()) {
                Toast.makeText(context, "Locked", Toast.LENGTH_SHORT).show();
            }
            zipFile.extractAll(DestinationFolderPath);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    public static class unZip extends AsyncTask<Integer, Integer, String> {

        public File file;
        public ProgressDialog progressDialog;
        public WeakReference<MainActivity> mainActivityWeakReference;
        public MainActivity context;
        public unZip(MainActivity mainActivity, File file) {
            this.file = file;
            mainActivityWeakReference = new WeakReference<>(mainActivity);
            context = mainActivityWeakReference.get();
        }

        @Override
        protected String doInBackground(Integer... params) {
                unzip(context, file.getPath(), file.getParent());
            return "Extracting";
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Toast.makeText(context, context.getString(R.string.extracted), Toast.LENGTH_SHORT).show();
            context.updateListView();
            context.showInterstialAd();

        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setTitle(context.getString(R.string.extracting_string) + file.getName());
            progressDialog.setMessage(context.getString(R.string.extracting_string));
            progressDialog.setMax(100);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }
    }

    public static class deleteFile extends AsyncTask<Integer, Integer, String> {

        public File file;
        public ProgressDialog progressDialog;
        public WeakReference<MainActivity> mainActivityWeakReference;
        public MainActivity context;

        public deleteFile(MainActivity mainActivity, File file) {
            this.file = file;
            mainActivityWeakReference = new WeakReference<>(mainActivity);
            context = mainActivityWeakReference.get();
        }

        @Override
        protected String doInBackground(Integer... params) {
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    deleteRecursive(child);
                }
                if (file.delete()) {

                }
            } else {
                if (file.delete()) {

                }
            }
            return "Deleted";
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Toast.makeText(context, context.getString(R.string.deleted_string), Toast.LENGTH_SHORT).show();
            context.updateListView();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setTitle(context.getString(R.string.deleting_string) + file.getName());
            progressDialog.setMessage(context.getString(R.string.deleting_string));
            progressDialog.setMax(100);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }
    }

    public static class addZip extends AsyncTask<Integer, Integer, String> {

        public File file;
        public ProgressDialog progressDialog;
        public WeakReference<MainActivity> mainActivityWeakReference;
        public MainActivity context;

        public addZip(MainActivity mainActivity, File file) {
            this.file = file;
            mainActivityWeakReference = new WeakReference<>(mainActivity);
            context = mainActivityWeakReference.get();
        }

        @Override
        protected String doInBackground(Integer... params) {
            zip(file.getPath(), file.getParent() + File.separator + file.getName() + ".zip");
            return "Deleted";
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Toast.makeText(context, context.getString(R.string.compressing_completed_string), Toast.LENGTH_SHORT).show();
            context.updateListView();
            context.showInterstialAd();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setTitle(context.getString(R.string.adding_to_zip_string) + file.getName());
            progressDialog.setMessage(context.getString(R.string.adding_to_zip_string));
            progressDialog.setMax(100);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }
    }

    public static class copyData extends AsyncTask<Integer, Integer, String> {

        public String file;
        public String file2;

        public ProgressDialog progressDialog;
        public WeakReference<MainActivity> mainActivityWeakReference;
        public MainActivity context;

        public copyData(MainActivity mainActivity, String file, String file2) {
            this.file = file;
            this.file2 = file2;
            mainActivityWeakReference = new WeakReference<>(mainActivity);
            context = mainActivityWeakReference.get();
        }

        @Override
        protected String doInBackground(Integer... params) {
            if (new File(file).isDirectory()) {
                try {
                    new Unzip(context).copyDirectoryOneLocationToAnotherLocation(new File(file), new File(file2 + File.separator + new File(file).getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    new Unzip(context).copyFile(new File(file), new File(file2 + File.separator + "copied-" + new File(file).getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "Copied";
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Toast.makeText(context, "Copying of File is completed.", Toast.LENGTH_SHORT).show();
            context.updateListView();
            context.showInterstialAd();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Copying File");
            progressDialog.show();
            progressDialog.setMax(100);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }
    }

    public static void zip(String targetPath, String destinationFilePath) {
        try {
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.COMP_DEFLATE);

            ZipFile zipFile = new ZipFile(destinationFilePath);

            File targetFile = new File(targetPath);
            if (targetFile.isFile()) {
                zipFile.addFile(targetFile, parameters);
            } else if (targetFile.isDirectory()) {
                zipFile.addFolder(targetFile, parameters);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFileName(File file, String newName) {

        String currentFileName = file.getName();

        File from = new File(file.getParent(), currentFileName);
        File to = new File(file.getParent(), newName.trim());
        if (from.renameTo(to)) {
            context.updateListView();
        }

    }

    public void createFolder(String folderLocation, String folderName) {
        File folder = new File(folderLocation +
                File.separator + folderName);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            context.updateListView();
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void createFile(String fileLocation, String fileName) throws IOException {
        if (new File(fileLocation + File.separator + fileName).createNewFile())
            Toast.makeText(context, fileName + "Created", Toast.LENGTH_SHORT).show();
        context.updateListView();
    }

    public void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                Log.d("Copy1", String.valueOf(targetLocation.mkdir()));
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);

            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    public void copyFile(File src, File dst) throws IOException {
        FileInputStream var2 = new FileInputStream(src);
        FileOutputStream var3 = new FileOutputStream(dst);
        byte[] var4 = new byte[1024];

        int var5;
        while ((var5 = var2.read(var4)) > 0) {
            var3.write(var4, 0, var5);
        }

        var2.close();
        var3.close();

    }
}
