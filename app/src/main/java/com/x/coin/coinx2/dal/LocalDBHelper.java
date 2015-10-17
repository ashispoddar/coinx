package com.x.coin.coinx2.dal;

/**
 * Created by ashispoddar on 10/16/15.
 */
import android.database.sqlite.*;
import android.content.Context;
import android.content.ContentValues;

import com.x.coin.coinx2.model.CardInfo;


public class LocalDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "COINX.DB";
    private static final String DATABASE_TABLE_CREATE = "CREATE TABLE CARDS (id INTEGER PRIMARY KEY,guid TEXT UNIQUE,fname TEXT,lname TEXT,expiry TEXT,card_alias TEXT,card_type TEXT)";
    private static final String DATABASE_TABLE_CREATE_INDEX = "CREATE INDEX guidIndex ON COINX.DB (guid);";
    private static final String DATABASE_TABLE_DELETE = "DROP TABLE IF EXISTS COINX.DB";

    //// TODO: 10/16/15 :make sure we have index on guid...
    private static final String COL_NAME_ID = "id";
    private static final String COL_NAME_GUID = "guid";
    private static final String COL_NAME_FNAME = "fname";
    private static final String COL_NAME_LNAME = "lname";
    private static final String COL_NAME_EXPIRY = "expiry";
    private static final String COL_NAME_CARDALIAS = "card_alias";
    private static final String COL_NAME_CARDTYPE = "card_type";



    public LocalDBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
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

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("CARDS","",values);
        long text = newRowId;
    }
    public CardInfo getCard(String cardGuid) {
        return null;
    }
    public void updateCard(CardInfo cardInfo) {
        return;
    }
}
