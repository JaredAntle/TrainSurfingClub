package com.example.install.trainsurfingclub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by web on 2017-02-07.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    /**
     * Keep track of database version
     *
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Create name of database
     */
    private static final String DATABASE_NAME = "trainsurfingclub";

    /**
     * Create names of all tables
     */
    private static final String TABLE_RECORDS = "record";

    /**
     * Record Table column names
     */
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_URL = "url";
    private static final String KEY_DESCRIPTION = "description";

    /**
     * Create statements for all tables
     */
    private static final String CREATE_RECORDS_TABLE = "CREATE TABLE " + TABLE_RECORDS + "(" + KEY_ID +
            " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_URL + " TEXT)";


    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_RECORDS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

    /**
     * CRUD OPERATIONS FOR THE DATABASE AND TABLES
     * CREATE
     * READ
     * UPDATE
     * DELETE
     */

    /**
     * Create Operations
     */
    public void addRecord(Record record){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, record.getTitle());
        values.put(KEY_DESCRIPTION, record.getDescription());
        values.put(KEY_URL, record.getUrl());
        db.insert(TABLE_RECORDS, null, values);
    }

    /**
     * Read Operations
     */
    public Record getRecord(int id){
        /**
         * Create readable database
         */
        SQLiteDatabase db = this.getReadableDatabase();
        /**
         * Create cursor (which is able to move through and access database records)
         * Have it store all records retrieved from the db.query()
         * cursor starts by pointing at record 0
         * Databases do not have a record 0
         * We use cursor.moveToFirst() to have it at the first record returned
         */
        Cursor cursor = db.query(TABLE_RECORDS,
                new String[] {KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_URL},
                "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();

        Record record = new Record(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return record;
    }

    public ArrayList<Record> getAllRecords(){
        ArrayList<Record> recordList = new ArrayList<Record>();
        String selectQuery = "SELECT * FROM " + TABLE_RECORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                Record record = new Record();
                record.setId(Integer.parseInt(cursor.getString(0)));
                record.setTitle(cursor.getString(1));
                record.setDescription(cursor.getString(2));
                record.setUrl(cursor.getString(3));
                recordList.add(record);
            } while(cursor.moveToNext());
        }
        return recordList;
    }

    /**
     * UPDATE OPERATIONS
     */
    public int updateRecord(Record record) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, record.getTitle());
        values.put(KEY_DESCRIPTION, record.getDescription());
        values.put(KEY_URL, record.getUrl());
        return db.update(TABLE_RECORDS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(record.getId())});
    }

    /**
     * DELETE OPERATIONS
     */
    public void deleteRecord(long record_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_RECORDS, KEY_ID + " = ?", new String[]{String.valueOf(record_id)});
    }

    /**
     * Close database connection
     */
    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen()) {
            db.close();
        }
    }
}
