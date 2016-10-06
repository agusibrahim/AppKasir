package com.agusibrahim.appkasir;
import com.firebase.client.*;
import com.agusibrahim.appkasir.Model.*;
import android.content.ContentValues;
import android.util.*;

public class FireDB
{
	public FireDB(){
	}
	public void tambah(ContentValues val){
		Produk p=new Produk(val.getAsString("nama"), val.getAsString("sn"), val.getAsLong("harga"), val.getAsInteger("stok"));
		MainActivity.mFirebase.push().setValue(p);
	}
	public void hapus(Produk p){
		int pos=MainActivity.dataproduk.getData().indexOf(p);
		MainActivity.mFirebase.child(MainActivity.keyIndex.get(pos)).removeValue();
	}
	public void perbarui(Produk lama, ContentValues val){
		Produk baru=new Produk(val.getAsString("nama"), val.getAsString("sn"), val.getAsLong("harga"), val.getAsInteger("stok"));
		int pos=MainActivity.dataproduk.getData().indexOf(lama);
		MainActivity.mFirebase.child(MainActivity.keyIndex.get(pos)).setValue(baru);
	}
}
