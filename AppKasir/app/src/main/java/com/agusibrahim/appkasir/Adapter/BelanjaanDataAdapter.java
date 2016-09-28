package com.agusibrahim.appkasir.Adapter;
import de.codecrafters.tableview.*;
import android.view.*;
import java.util.*;
import com.agusibrahim.appkasir.Model.*;
import android.content.*;
import android.widget.*;
import java.text.*;
import com.agusibrahim.appkasir.*;
import com.agusibrahim.appkasir.Fragment.*;
import android.text.*;
import android.util.*;

public class BelanjaanDataAdapter extends TableDataAdapter
{
	public static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
	public static long total=0;
	public BelanjaanDataAdapter(Context ctx){
		super(ctx, new ArrayList<Belanjaan>());
	}
	@Override
	public View getCellView(int row, int column, ViewGroup p3) {
		Belanjaan belanjaan = (Belanjaan) getRowData(row);
		Produk produk=belanjaan.getProduk();
		View render=null;
		switch(column){
			case 0:
				render=renderString(produk.getNama());
				break;
			case 1:
				render=renderString("Rp. "+PRICE_FORMATTER.format(produk.getHarga()));
				break;
			case 2:
				render=renderString(""+belanjaan.getQuantity());
				break;
		}
		return render;
	}
	// Fungsi get Belanjaan melalui SN
	public Belanjaan getBelBySN(String sn){
		Belanjaan pp = null;
		for(Belanjaan b:getData()){
			if(b.getProduk().getSn().equals(sn)){
				pp=b;
				break;
			}
		}
		return pp;
	}
	public void tambah(Produk prod, int quantity){
		Belanjaan bel=getBelBySN(prod.getSn());
		// jika produk sudah ada dalam keranjang
		// maka tambahkan quantity
		if(bel!=null){
			int prodquantity=bel.getQuantity()+quantity;
			getData().set(getData().indexOf(bel), new Belanjaan(prod, prodquantity));
			total=total-bel.getProduk().getHarga();
			total=total+(bel.getProduk().getHarga()*prodquantity);
		}else{
			// jika tidak ada dalam meranjang
			// maka masukan ke keranjang
			getData().add(new Belanjaan(prod, quantity));
			total=total+prod.getHarga();
		}
		notifyDataSetChanged();
	}
	public void hapus(Produk produk){
		getData().remove(produk);
		total=total-produk.getHarga();
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
