package com.agusibrahim.appkasir.Adapter;
import de.codecrafters.tableview.*;
import android.view.*;
import java.util.*;
import com.agusibrahim.appkasir.Model.*;
import android.content.*;
import android.widget.*;
import java.text.*;
import com.agusibrahim.appkasir.*;

public class ProdukDataAdapter extends TableDataAdapter
{
	public static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
	public ProdukDataAdapter(Context ctx, ArrayList<Produk> prod){
		super(ctx, prod);
	}
	@Override
	public View getCellView(int row, int column, ViewGroup p3) {
		Produk produk = (Produk) getRowData(row);
		View render=null;
		switch(column){
			case 0:
				render=renderString(produk.getNama());
				break;
			case 1:
				render=renderString(Utils.priceFormat(produk.getHarga()));
				break;
			case 2:
				render=renderString(""+produk.getStok());
				break;
		}
		return render;
	}
	private int getpos(Produk p){
		int pos = -1;
		for(Produk pp:getData()){
			if(pp.getSn().equals(p.getSn())){
				pos=getData().indexOf(pp);
				break;
			}
		}
		return pos;
	}
	public void tambah(ContentValues val){
		getData().add(new Produk(val.getAsString("nama"), val.getAsString("sn"), val.getAsLong("harga"), val.getAsInteger("stok")));
		new DBHelper(getContext()).tambah(val);
		notifyDataSetChanged();
	}
	public void hapus(Produk produk){
		getData().remove(produk);
		new DBHelper(getContext()).delete(produk.getSn());
		notifyDataSetChanged();
	}
	public void perbarui(Produk produk, ContentValues newdata){
		int idx=getpos(produk);//getData().indexOf(produk);
		if(idx>=0){
			
		}else{
			return;
		}
		getData().set(idx, new Produk(newdata.getAsString("nama"), newdata.getAsString("sn"), newdata.getAsLong("harga"), newdata.getAsInteger("stok")));
		new DBHelper(getContext()).update(newdata, produk.getSn());
		notifyDataSetChanged();
	}
	private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(14);
        return textView;
    }
	
}
