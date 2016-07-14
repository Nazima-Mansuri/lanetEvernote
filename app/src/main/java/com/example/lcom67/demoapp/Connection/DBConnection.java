package com.example.lcom67.demoapp.Connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lcom67.demoapp.Beans.Contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcom67 on 1/7/16.
 */

public class DBConnection extends SQLiteOpenHelper {

    SQLiteDatabase db;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "demo";

    public static final String TABLE_SIGNUP = "signup";
    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_IMAGE = "images";

    public static final String SIGNUP_ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL_ID = "email";
    public static final String PASSWORD = "password";
    public static final String MOBILE_NO = "mobile_no";
    public static final String ADDRESS = "address";

    public static final String NOTES_ID = "notes_id";
    public static final String NOTES_TITLE = "notes_title";
    public static final String DESCRIPTION = "description";

    public static final String IMAGE_PATH = "image_path";
    public static final String CAMERA_IMAGE = "camera_image";

    public DBConnection(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public DBConnection(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME, null, 1, errorHandler);
    }

    public long AddData(Contacts contact) {
        SQLiteDatabase db1 = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(NAME, contact.getName());
        cv.put(EMAIL_ID, contact.getEmail());
        cv.put(PASSWORD, contact.getPasswd());
        cv.put(MOBILE_NO, contact.getMobile());
        cv.put(ADDRESS, contact.getAddress());

        long transactId = db1.insert(TABLE_SIGNUP, null, cv);
        db1.close();
        return transactId;
    }

    public void AddNotes(Contacts contact) {
        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(NOTES_TITLE, contact.getNotes_Title());
        cv.put(DESCRIPTION, contact.getNotes_Description());
        cv.put(IMAGE_PATH, contact.getmImagePath());
        cv.put(CAMERA_IMAGE, contact.getCameraImage());
        cv.put(SIGNUP_ID, contact.getId());

        db.insert(TABLE_NOTES, null, cv);
        db.close();
    }

    public String getTitle(long id) {
        db = this.getReadableDatabase();

        String query = new String("select " + NOTES_TITLE + " from " + TABLE_NOTES + " where " + NOTES_ID + " = " + id);
        Cursor result = db.rawQuery(query, null);

        String returnString = ""; // Your default if none is found

        if (result.moveToFirst()) {
            Contacts contact = new Contacts();
            returnString = result.getString(0);
            contact.setNotes_Title(returnString);
        }
        result.close();

        return returnString;
    }

    public String getDescription(long id) {
        db = this.getReadableDatabase();
        String query = new String("select " + DESCRIPTION + " from " + TABLE_NOTES + " where " + NOTES_ID + " = " + id);
        Cursor result = db.rawQuery(query, null);

        String returnString = ""; // Your default if none is found

        if (result.moveToFirst()) {
            Contacts contact = new Contacts();
            returnString = result.getString(0);
            contact.setNotes_Description(returnString);
        }
        result.close();

        return returnString;
    }

    public String getImageName(long id) {
        db = this.getReadableDatabase();
        String query = new String("select " + IMAGE_PATH + " from " + TABLE_NOTES + " where " + NOTES_ID + " = " + id);
        Cursor result = db.rawQuery(query, null);

        String returnString = ""; // Your default if none is found

        if (result.moveToFirst()) {
            Contacts contact = new Contacts();
            returnString = result.getString(0);
            contact.setmImagePath(returnString);
        }
        result.close();

        return returnString;
    }

    public String getCameraImageName(long id) {
        db = this.getReadableDatabase();
        String query = new String("select " + CAMERA_IMAGE + " from " + TABLE_NOTES + " where " + NOTES_ID + " = " + id);
        Cursor result = db.rawQuery(query, null);

        String returnString = ""; // Your default if none is found

        if (result.moveToFirst()) {
            Contacts contact = new Contacts();
            returnString = result.getString(0);
            contact.setCameraImage(returnString);
        }
        result.close();

        return returnString;
    }

    public String getUserName(long id) {
        db = this.getReadableDatabase();

        String query = new String("select " + NAME + " from " + TABLE_SIGNUP + " where " + SIGNUP_ID + " = " + id);
        Cursor result = db.rawQuery(query, null);

        String returnString = ""; // Your default if none is found

        if (result.moveToFirst()) {
            Contacts contact = new Contacts();
            returnString = result.getString(0);
            contact.setNotes_Title(returnString);
        }
        result.close();

        return returnString;
    }

    public String getEmailId(long id) {
        db = this.getReadableDatabase();

        String query = new String("select " + EMAIL_ID + " from " + TABLE_SIGNUP + " where " + SIGNUP_ID + " = " + id);
        Cursor result = db.rawQuery(query, null);

        String returnString = ""; // Your default if none is found

        if (result.moveToFirst()) {
            Contacts contact = new Contacts();
            returnString = result.getString(0);
            contact.setNotes_Title(returnString);
        }
        result.close();

        return returnString;
    }

    public void deleteNote(long id) {
        db = this.getWritableDatabase();
        String deleteQuery = "delete from " + TABLE_NOTES + " where " + NOTES_ID + "= " + id;
        db.execSQL(deleteQuery);
        db.close();
    }

    public List<Contacts> getAllUsers(String username) {
        List<Contacts> contactList = new ArrayList<Contacts>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SIGNUP + " where " + EMAIL_ID + "  =  '" + username + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contacts contact = new Contacts();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setEmail(cursor.getString(2));
                contact.setPasswd(cursor.getString(3));
                contact.setMobile(cursor.getString(4));
                contact.setAddress(cursor.getString(5));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public List<Contacts> getAllNotes(long id) {
        List<Contacts> contactList = new ArrayList<Contacts>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOTES + " where " + SIGNUP_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contacts contact = new Contacts();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setNotes_Title(cursor.getString(1));
                contact.setNotes_Description(cursor.getString(2));
                contact.setmImagePath(cursor.getString(3));
                contact.setCameraImage(cursor.getString(4));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Update Handwriting in Notes
    public boolean updateImage(long Id, String image_name)
    {
        ContentValues cv = new ContentValues();
        cv.put(IMAGE_PATH, image_name);
        return db.update(TABLE_NOTES, cv, NOTES_ID + "=" + Id, null) > 0;
    }

    // Updata camera Image
    public boolean updateCameraImage(long Id, String camera_image_name) {
        ContentValues cv = new ContentValues();
        cv.put(CAMERA_IMAGE, camera_image_name);
        return db.update(TABLE_NOTES, cv, NOTES_ID + "=" + Id, null) > 0;
    }

    // Update Notes Title and Description
    public boolean updateData(long Id, String Title, String Description) {
        ContentValues cv = new ContentValues();
        cv.put(NOTES_TITLE, Title);
        cv.put(DESCRIPTION, Description);
        return db.update(TABLE_NOTES, cv, NOTES_ID + "=" + Id, null) > 0;
    }

    // Update User Name
    public boolean updateUserName(long Id,String Username) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, Username);
        return db.update(TABLE_SIGNUP, cv, SIGNUP_ID + "=" + Id, null) > 0;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SIGNUP_TABLE = "create table " + TABLE_SIGNUP + " (" + SIGNUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + NAME
                + " TEXT," + EMAIL_ID + " TEXT," + PASSWORD + " TEXT," + MOBILE_NO + " TEXT," + ADDRESS + " TEXT )";
        sqLiteDatabase.execSQL(CREATE_SIGNUP_TABLE);

        String CREATE_NOTES = "create table " + TABLE_NOTES + " (" + NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NOTES_TITLE + " TEXT," + DESCRIPTION + " TEXT," + IMAGE_PATH + " TEXT ," + CAMERA_IMAGE + " TEXT ," + SIGNUP_ID + "  INTEGER )";
        sqLiteDatabase.execSQL(CREATE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_SIGNUP_TABLE = "DROP TABLE IF EXISTS " + TABLE_SIGNUP;
        sqLiteDatabase.execSQL(DROP_SIGNUP_TABLE);

        String DROP_NOTES_TABLE = "DROP TABLE IF EXISTS " + TABLE_NOTES;
        sqLiteDatabase.execSQL(DROP_NOTES_TABLE);
    }
}
