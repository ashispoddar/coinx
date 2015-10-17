package com.x.coin.coinx2.dal;

/**
 * Created by ashispoddar on 10/16/15.
 */
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.x.coin.coinx2.model.CardInfo;


public class LocalDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "COINX.DB";
    private static final String DATABASE_TABLE_CREATE = "CREATE TABLE CARDS (id INTEGER PRIMARY KEY,guid TEXT UNIQUE,fname TEXT,lname TEXT,expiry TEXT,card_alias TEXT,card_type TEXT, time_created TEXT, time_updated TEXT)";
    private static final String DATABASE_TABLE_CREATE_INDEX = "CREATE INDEX guidIndex ON CARDS(guid);";
    private static final String DATABASE_TABLE_DELETE = "DROP TABLE IF EXISTS CARDS";

    //// TODO: 10/16/15 :make sure we have index on guid...
    private static final String COL_NAME_ID = "id";
    private static final String COL_NAME_GUID = "guid";
    private static final String COL_NAME_FNAME = "fname";
    private static final String COL_NAME_LNAME = "lname";
    private static final String COL_NAME_EXPIRY = "expiry";
    private static final String COL_NAME_CARDALIAS = "card_alias";
    private static final String COL_NAME_CARDTYPE = "card_type";
    private static final String COL_NAME_TIMECREATED = "time_created";
    private static final String COL_NAME_TIMEUPDATED = "time_updated";



    public LocalDBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_TABLE_CREATE);
        db.execSQL(DATABASE_TABLE_CREATE_INDEX);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DATABASE_TABLE_DELETE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public void insertCard(CardInfo cardInfo) {
        SQLiteDatabase db  = getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COL_NAME_GUID, cardInfo.getGuid());
        values.put(COL_NAME_FNAME, cardInfo.getFirstName());
        values.put(COL_NAME_LNAME, cardInfo.getLastName());
        values.put(COL_NAME_EXPIRY, cardInfo.getExpiry());
        values.put(COL_NAME_CARDALIAS, cardInfo.getCardNumber());
        values.put(COL_NAME_CARDTYPE, cardInfo.getType());
        values.put(COL_NAME_TIMECREATED, cardInfo.getTimeCreated());
        values.put(COL_NAME_TIMEUPDATED, cardInfo.getTimeUpdated());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("CARDS", "", values);
        long text = newRowId;
    }
    public CardInfo getCard(String cardGuid) {

        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {"id","fname","lname","guid","card_alias","card_type", "expiry", "time_created","time_updated"};
        String selection = "guid=?";
        String[] selectionArgs = { cardGuid };

        Cursor cursor = db.query(
                "Cards",  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            String fName = cursor.getString(cursor.getColumnIndexOrThrow("fname"));
            String lName = cursor.getString(cursor.getColumnIndexOrThrow("lname"));
            String expiry = cursor.getString(cursor.getColumnIndexOrThrow("expiry"));
            String guid = cursor.getString(cursor.getColumnIndexOrThrow("guid"));
            String cardAlias = cursor.getString(cursor.getColumnIndexOrThrow("card_alias"));
            String cardType = cursor.getString(cursor.getColumnIndexOrThrow("card_type"));
            String timeCreated = cursor.getString(cursor.getColumnIndexOrThrow("time_created"));
            String timeUpdated = cursor.getString(cursor.getColumnIndexOrThrow("time_updated"));

            CardInfo cardInfo = new CardInfo(fName, lName, cardType, cardAlias, expiry, guid);
            cardInfo.setTimeCreated(timeCreated);
            cardInfo.setTimeUpdated(timeUpdated);
        }
        return null;
    }
    public void updateCard(CardInfo cardInfo) {
    }
}
