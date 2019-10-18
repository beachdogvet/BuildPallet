package com.lockheedmartin.buildpallet.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lockheedmartin.buildpallet.SpinnerItem;

import java.util.ArrayList;
import java.util.List;

public class LocationRepository extends BaseRepository {


    public LocationRepository(Context context) {
        super(context);
    }



    public boolean Create(String newLocationText) {

        boolean returnValue = true;
        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("locationName", newLocationText);

            db.insert("locations", null, values);
            db.close();

        }
        catch (Exception e)
        {
            Log.d("Exception thrown", "AddContact() " + e.getMessage());
            e.printStackTrace();
            returnValue = false;
        }

        return returnValue;
    }


    public boolean Delete(int locationId) {

        boolean returnValue = true;
        try {
            SQLiteDatabase db = getWritableDatabase();
            String[] whereArgs = { Long.toString(locationId) };
            db.delete("locations", "id=?" , whereArgs);
            db.close();
        }
        catch (Exception e)
        {
            Log.d("Exception thrown", "DeleteContact() " + e.getMessage());
            e.printStackTrace();
            returnValue = false;
        }
        return returnValue;
    }


    public boolean UpdateLocation(int locationId, String locationName) {

        boolean returnValue = true;

        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("locationName", locationName);
            String[] whereArgs = { Long.toString(locationId) };
            db.update("locations", values, " id=?", whereArgs);
            db.close();
        }
        catch (Exception e)
        {
            Log.d("Exception thrown", "UpdateLocation() " + e.getMessage());
            e.printStackTrace();
            returnValue = false;
        }

        return returnValue;
    }


    public List<SpinnerItem> GetAll() {

        SQLiteDatabase db = null;
        List<SpinnerItem> locations = new ArrayList<SpinnerItem>();
        String selectQuery = "SELECT id,locationName from locations order by locationName";

        try {
            db = getWritableDatabase();
            if (db != null) {
                Cursor cursor = db.rawQuery(selectQuery, null);
                //stateLocations.add("Select a State");
                if (cursor.moveToFirst()) {
                    do {
                        SpinnerItem si = new SpinnerItem(cursor.getInt(0), cursor.getString(1));
                        locations.add(si);
                    } while (cursor.moveToNext());

                    if (cursor != null)
                        cursor.close();
                }
            }
        } catch (Exception e) {
            Log.d("Exception thrown", "GetLocations() " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return locations;
    }






}
