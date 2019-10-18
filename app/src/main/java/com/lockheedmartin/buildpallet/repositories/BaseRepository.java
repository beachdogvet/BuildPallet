package com.lockheedmartin.buildpallet.repositories;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.lockheedmartin.buildpallet.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BaseRepository extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "palletdb.db";
    public static final int DATABASE_VERSION = 1;
    public static final String OUTPUT_FILE_LOCATION_DIR = "/sdcard/ScannerFiles";

    public BaseRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void InitializeDB(Context context) {

        boolean sourceDatabaseFileExists = false;
        String targetDatabasePath = "/data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME;

        File targetDatabaseFile = new File(targetDatabasePath);
        if (!targetDatabaseFile.exists()) {
            try {
                String[] files = context.getAssets().list("");

                for (String entry : files) {
                    if (entry.startsWith(DATABASE_NAME)) {

                        File targetDatabasedirectory = new File("/data/data/" + context.getPackageName() + "/databases");
                        targetDatabasedirectory.mkdirs();
                        targetDatabaseFile.createNewFile();
                        sourceDatabaseFileExists = true;
                        break;
                    }
                }
                if (sourceDatabaseFileExists) {

                    if (targetDatabaseFile.exists()) {
                        CopyDB(context.getAssets().open(DATABASE_NAME),
                                new FileOutputStream(targetDatabasePath));
                    }
                }

            } catch (IOException e) {
                Log.d("Exception thrown", " InitializeDB() " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    public static void DeleteCurrentDatabase(Context context) {

        String targetDatabasePath = "/data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME;

        File targetDatabaseFile = new File(targetDatabasePath);
        if (targetDatabaseFile.exists()) {


            try {
                targetDatabaseFile.delete();
            }
            catch (Exception e) {
                Log.d("Exception thrown", " DeleteCurrentDatabase() " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public static void InitFileOutputLocation() {

        File outputFolder = new File(OUTPUT_FILE_LOCATION_DIR);
        if (!outputFolder.exists()) {

            outputFolder.mkdirs();
        }

    }


















    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
