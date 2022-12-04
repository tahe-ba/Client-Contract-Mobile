package com.example.projetds;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dev-035.sqlite";
    public static final String TABLE_NAME = "client";
    public static final String CL_ID = "Id";
    public static final String CL_NAME = "name";
    public static final String CL_ADDRESS = "address";
    public static final String CL_TEL = "Tel";
    public static final String CL_FAX = "fax";
    public static final String CL_CONTACT = "contact";
    public static final String CL_TEL_CONTACT = "telContact";
    public static final String TABLE_NAME2 = "contrat";
    public static final String CO_ID = "Id";
    public static final String CO_CLIENT_ID = "IdClient";
    public static final String CO_REF = "reference";
    public static final String CO_START_D = "startDate";
    public static final String CO_END_D = "endDate";
    public static final String CO_ROYALTY = "royalty";

    public dataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (Id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,address TEXT,Tel INTEGER,fax INTEGER,contact TEXT,telcontact INTEGER)");
        db.execSQL("create table " + TABLE_NAME2 + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, IdClient INTEGER, reference TEXT, startDate TEXT, endDate TEXT, royalty INTEGER, FOREIGN KEY(IdClient) REFERENCES client(IdClient))");
    }

    // add new client
    public boolean addClient(String nom, String address, String Tel, String fax, String contact, String telContact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CL_NAME, nom);
        contentValues.put(CL_ADDRESS, address);
        contentValues.put(CL_TEL, Tel);
        contentValues.put(CL_FAX, fax);
        contentValues.put(CL_CONTACT, contact);
        contentValues.put(CL_TEL_CONTACT, telContact);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // save new contract linked to a client
    public boolean addContract(String reference, String startDate, String endDate, String royalty, Integer idClient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CO_CLIENT_ID, idClient.toString());
        contentValues.put(CO_REF, reference);
        contentValues.put(CO_START_D, startDate);
        contentValues.put(CO_END_D, endDate);
        contentValues.put(CO_ROYALTY, royalty);
        long result = db.insert(TABLE_NAME2, null, contentValues);
        return result != -1;
    }


    // update client
    public boolean updateClient(String id, String nom, String address, String Tel, String fax, String contact, String telContact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CL_ID, id);
        contentValues.put(CL_NAME, nom);
        contentValues.put(CL_ADDRESS, address);
        contentValues.put(CL_TEL, Tel);
        contentValues.put(CL_FAX, fax);
        contentValues.put(CL_CONTACT, contact);
        contentValues.put(CL_TEL_CONTACT, telContact);
        db.update(TABLE_NAME, contentValues, "Id = ?", new String[]{id});
        return true;
    }

    // update contrat and save client id

    public boolean updateContract(Integer id, String reference, String startDate, String endDate, String royalty, Integer idClient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CO_ID, id.toString());
        contentValues.put(CO_CLIENT_ID, idClient.toString());
        contentValues.put(CO_REF, reference);
        contentValues.put(CO_START_D, startDate);
        contentValues.put(CO_END_D, endDate);
        contentValues.put(CO_ROYALTY, royalty);
        db.update(TABLE_NAME2, contentValues, "Id = ?", new String[]{id.toString()});
        return true;
    }

    // delete client
    public int deleteClient(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "Id = ?", new String[]{id});
    }

    // delete contrat
    public Integer deleteContract(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME2, "Id = ?", new String[]{id});
    }

    // search client by name
    public Cursor searchClient(String nom) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where name like ?", new String[]{nom});
//        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);

        return res;
    }

    // search contract by client id and accept those contracts without client id
    public Cursor getContractByClientId(Integer idClient) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME2 + " where IdClient like ? or IdClient is null", new String[]{idClient.toString()});
        return res;
    }

    //search contract by client name with left outer join to get all contracts even those without clients (0)
    // query select * from contrat left outer join  client  on  contrat .IdClient =  client .Id where  client .name like "taha" or contrat .IdClient = 0
    public Cursor getContractByClientName(String clientName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME2 + " left outer join " + TABLE_NAME + " on " + TABLE_NAME2 + ".IdClient = " + TABLE_NAME + ".Id where " + TABLE_NAME + ".name like ? or " + TABLE_NAME2 + ".IdClient = 0", new String[]{clientName});
        return res;
    }

    // search contrat by reference
    public Cursor searchContract(String reference) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME2 + " where reference = ?", new String[]{reference});
        return res;
    }

    // delete all client
    public void deleteAllClient() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}