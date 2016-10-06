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
	public void updateTotal(){
		long jm=0;
		for(Belanjaan b:getData()){
			jm=jm+(b.getProduk().getHarga()*b.getQuantity());
		}
		total=jm;
	}
	public void tambah(Produk prod, int quantity){
		Belanjaan bel=getBelBySN(prod.getSn());
		// jika produk sudah ada dalam keranjang
		// maka tambahkan quantity
		if(bel!=null){
			int prodquantity=bel.getQuantity()+1;
			if(quantity!=-1) prodquantity=quantity;
			getData().set(getData().indexOf(bel), new Belanjaan(prod, prodquantity));
		}else{
			// jika tidak ada dalam keranjang
			// maka masukan ke keranjang
			if(quantity==-1) quantity=1;
			getData().add(new Belanjaan(prod, quantity));
		}
		// update total belanja
		updateTotal();
		notifyDataSetChanged();
	}
	public void hapus(Belanjaan bel){
		getData().remove(bel);
		// update total belanja
		updateTotal();
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
