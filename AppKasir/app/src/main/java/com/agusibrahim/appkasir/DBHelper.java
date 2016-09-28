package com.agusibrahim.appkasir;
import android.database.sqlite.*;
import android.content.*;
import android.database.*;

public class DBHelper extends SQLiteOpenHelper
{
	SQLiteDatabase dbw;
	public static String namaTable="produk";
	public DBHelper(Context ctx){
		super(ctx, namaTable+".db", null, 2);
		dbw=getWritableDatabase();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "+namaTable+" (sn TEXT null,nama TEXT null,harga INTEGER null, stok INTEGER null)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int p2, int p3) {
		db.execSQL("DROP TABLE IF EXISTS "+namaTable);
	}
	public void tambah(ContentValues val){
		dbw.insert(namaTable, null, val);
	}
	public void update(ContentValues val, String sn){
		dbw.update(namaTable, val, "sn="+sn, null);
	}
	public void delete(String sn){
		dbw.delete(namaTable, "sn="+sn, null);
	}
	
	public Cursor semuaData() {
        Cursor cur = dbw.rawQuery("SELECT * FROM "+namaTable, null);
        return cur;
    }
	public Cursor baca(String sn) {
        Cursor cur = dbw.rawQuery("SELECT * FROM "+namaTable+" WHERE sn="+sn, null);
        return cur;
    }
}
