package com.contactdemo.databaseHandler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.contactdemo.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class perform CRUD (Create, Read, Update, Delete) operation with SqLite.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // variable declaration

    // database version
    private static final int DATABASE_VERSION = 1;

    // database name
    private static final String DATABASE_NAME = "addressbook";

    // table name
    private static final String TABLE_NAME = "contact";

    // table column name
    private static final String KEY_ID = "ID";
    private static final String KEY_IMAGE_PATH = "IMAGE_PATH";
    private static final String KEY_FIRST_NAME = "FIRST_NAME";
    private static final String KEY_lAST_NAME = "LAST_NAME";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_PHONE_NO = "PHONE";


    // constructor
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE  TABLE
        String CREATE_RECORD_DETAIL = "CREATE TABLE " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_IMAGE_PATH + " TEXT,"
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_lAST_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PHONE_NO + " INTEGER "
                + ")";

        db.execSQL(CREATE_RECORD_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //creating table again
        onCreate(db);
    }

    /**
     * To insert a single record in sqLite database
     *
     * @param recordModel (RecordModel): value that to be passed to store in database
     */
    public void addRecord(ContactModel recordModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_IMAGE_PATH, recordModel.getImagePath());
        values.put(KEY_FIRST_NAME, recordModel.getFirstName());
        values.put(KEY_lAST_NAME, recordModel.getLastName());
        values.put(KEY_EMAIL, recordModel.getEmail());
        values.put(KEY_PHONE_NO, recordModel.getPhoneNumber());

        // Inserting row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /**
     * To update value in sqLite database
     *
     * @param recordModel (RecordModel) : value that to be update in sqLite database
     */
    public void updateRecord(ContactModel recordModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_IMAGE_PATH, recordModel.getImagePath());
        values.put(KEY_FIRST_NAME, recordModel.getFirstName());
        values.put(KEY_lAST_NAME, recordModel.getLastName());
        values.put(KEY_EMAIL, recordModel.getEmail());
        values.put(KEY_PHONE_NO, recordModel.getPhoneNumber());

        // updating row
        db.update(TABLE_NAME, values, KEY_ID + " = " + recordModel.getId(), null);
        db.close();
    }

    /**
     * To remove data from sqLite database
     *
     * @param recordModel (RecordModel) : value that to be remove from sqLite database
     */
    public void deleteRecord(ContactModel recordModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID + "=" + recordModel.getId(), null);

        db.close();
    }

    /**
     * To get all record from sqLite database
     *
     * @return List<RecordModel> : it return list of record from sqLite database
     */
    @SuppressLint("Range")
    public List<ContactModel> getAllRecord() {
        List<ContactModel> recordList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ContactModel addRecord = new ContactModel();

                addRecord.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                addRecord.setImagePath(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));
                addRecord.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                addRecord.setLastName(cursor.getString(cursor.getColumnIndex(KEY_lAST_NAME)));
                addRecord.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                addRecord.setPhoneNumber(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NO)));


                recordList.add(addRecord);
            } while (cursor.moveToNext());
        }
        return recordList;
    }

    /**
     * To get all sorted record from sqLite database
     *
     * @return List<RecordModel> : it return list of record from sqLite database
     */
    @SuppressLint("Range")
    public List<ContactModel> getAllSortedRecord() {
        List<ContactModel> recordList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME +
                " ORDER BY " + KEY_FIRST_NAME + " COLLATE NOCASE ASC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ContactModel addRecord = new ContactModel();

                addRecord.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                addRecord.setImagePath(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));
                addRecord.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                addRecord.setLastName(cursor.getString(cursor.getColumnIndex(KEY_lAST_NAME)));
                addRecord.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                addRecord.setPhoneNumber(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NO)));

                recordList.add(addRecord);
            } while (cursor.moveToNext());
        }
        return recordList;
    }

    /**
     * To check that any record found in sqLite database or not.
     *
     * @return boolean : it return true if realm database count greater than 0 else return false
     */
    public boolean isRecordFound() {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = " SELECT  * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount() > 0;

    }

    /**
     * To delete all record from sqLite database
     */
    public void deleteAll() {
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(TABLE_NAME, null, null);

        db.close();
    }


}
