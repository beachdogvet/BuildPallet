package com.lockheedmartin.buildpallet.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lockheedmartin.buildpallet.Barcode;

import java.util.ArrayList;

public class BarcodeRepository extends BaseRepository {

    public BarcodeRepository(Context context) {
        super(context);
    }


    public boolean InsertBaggageTag(Barcode barcode){

        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("tag_short", barcode.getBagggeTagShort());
            values.put("tag_long", barcode.getBaggageTagLong());
            values.put("rank", barcode.getBaggageTagRank());
            values.put("first_name", barcode.getBaggageTagFirstName());
            values.put("last_name", barcode.getBaggageTagLastName());
            values.put("ssn", barcode.getBagggageTagSSN());
            values.put("poc_phone", barcode.getBaggageTagPocPhone());
            values.put("isBaggageTag", 1);

            db.insert("barcode", null, values);
            db.close();
        } catch (Exception e) {

            Log.e("Exception thrown", "BarcodeRepository: InsertBaggageTag() " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean InsertTag(Barcode barcode){

        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("tag_short", barcode.getBagggeTagShort());
            db.insert("barcode", null, values);
            db.close();
        } catch (Exception e) {

            Log.e("Exception thrown", "BarcodeRepository: InsertBaggageTag() " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }



     public ArrayList<String> GetAllBaggageShortTags() {

         SQLiteDatabase db = null;

         ArrayList<String> barcodeList = new ArrayList<String>();
         String selectQuery = "SELECT * from barcode";

         try
         {
             db = getWritableDatabase();
             if (db != null) {

                 Cursor cursor = db.rawQuery(selectQuery, null);

                 if (cursor.moveToFirst()) {
                     do {
                         barcodeList.add(cursor.getString(cursor.getColumnIndex("tag_short")));
                     } while (cursor.moveToNext());

                     if (cursor != null)
                         cursor.close();
                 }
             }
         }
         catch(Exception e){
             Log.e("Exception thrown", "BarcodeRepository: GetAll() Barcodes " + e.getMessage());
             e.printStackTrace();
         } finally{
             if (db != null) {
                 db.close();
             }
         }
        return barcodeList;
     }


     public boolean DeleteAll() {

         SQLiteDatabase db = getWritableDatabase();

         try {

             db.execSQL("delete from barcode");
             return true;
         }
         catch (SQLException e) {
             Log.e("Exception thrown", "BarcodeRepository: DeleteAll() Barcodes " + e.getMessage());
             e.printStackTrace();
             return false;
         }
         finally {
             if (db != null) {
                 db.close();
             }
         }

     }



}
