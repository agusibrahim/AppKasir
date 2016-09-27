package com.agusibrahim.appkasir.Model;
import java.util.*;
import android.database.*;
import com.agusibrahim.appkasir.*;
import android.content.*;

public class Produk
{
	protected String nama;
	protected String sn;
	protected long harga;

	public Produk(String nama, String sn, long harga) {
		this.nama = nama;
		this.sn = sn;
		this.harga = harga;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNama() {
		return nama;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSn() {
		return sn;
	}

	public void setHarga(long harga) {
		this.harga = harga;
	}

	public long getHarga() {
		return harga;
	}
	public static ArrayList<Produk> getInit(Context ctx){
		ArrayList<Produk> prod=new ArrayList<Produk>();
		Cursor cur=new DBHelper(ctx).semuaData();
		cur.moveToFirst();
		for(int i=0;i<cur.getCount();i++){
			cur.moveToPosition(i);
			String nama=cur.getString(cur.getColumnIndex("nama"));
			String sn=cur.getString(cur.getColumnIndex("sn"));
			long harga=cur.getLong(cur.getColumnIndex("harga"));
			prod.add(new Produk(nama, sn, harga));
		}
		return prod;
	}
}
