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
	private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
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
				render=renderString("Rp. "+PRICE_FORMATTER.format(produk.getHarga()));
				break;
		}
		return render;
	}
	public void tambah(ContentValues val){
		getData().add(new Produk(val.getAsString("nama"), val.getAsString("sn"), val.getAsLong("harga")));
		new DBHelper(getContext()).tambah(val);
		notifyDataSetChanged();
	}
	public void hapus(Produk produk){
		getData().remove(produk);
		new DBHelper(getContext()).delete(produk.getSn());
		notifyDataSetChanged();
	}
	public void perbarui(Produk produk, ContentValues newdata){
		int idx=getData().indexOf(produk);
		getData().set(idx, new Produk(newdata.getAsString("nama"), newdata.getAsString("sn"), newdata.getAsLong("harga")));
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
