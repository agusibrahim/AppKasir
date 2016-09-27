package com.agusibrahim.appkasir;

import android.app.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import de.codecrafters.tableview.*;
import de.codecrafters.tableview.toolkit.*;
import com.agusibrahim.appkasir.Adapter.*;
import com.agusibrahim.appkasir.Model.*;
import java.util.*;
import de.codecrafters.tableview.listeners.*;
import android.widget.*;
import android.support.v4.content.*;
import android.view.*;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity 
{
	public static ProdukDataAdapter dataproduk;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		TableGue tableView = (TableGue) findViewById(R.id.tableView);
		dataproduk=new ProdukDataAdapter(this, Produk.getInit(this));
		tableView.setDataAdapter(dataproduk);
		tableView.addDataClickListener(new DataClickListener());
		tableView.addDataLongClickListener(new DataLongClickListener());
    }
	
	private class DataClickListener implements TableDataClickListener<Produk> {
        @Override
        public void onDataClicked(int rowIndex, Produk clickedData) {
            final String carString = clickedData.getNama()+" Harganya "+clickedData.getHarga();
            Toast.makeText(MainActivity.this, carString, Toast.LENGTH_SHORT).show();
        }
    }
	private class DataLongClickListener implements TableDataLongClickListener<Produk> {
        @Override
        public boolean onDataLongClicked(int rowIndex, Produk clickedData) {
            new ProdukDialog(MainActivity.this, clickedData);
			return true;
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_add:
				new ProdukDialog(MainActivity.this, null);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
