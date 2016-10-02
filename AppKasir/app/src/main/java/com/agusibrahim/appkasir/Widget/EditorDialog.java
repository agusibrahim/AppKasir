package com.agusibrahim.appkasir.Widget;
import android.content.*;
import com.agusibrahim.appkasir.*;
import android.view.*;
import android.widget.*;
import com.agusibrahim.appkasir.Adapter.*;
import android.support.v7.app.*;
import android.support.v4.app.*;
import android.app.Dialog;
import android.os.*;
import com.agusibrahim.appkasir.Model.*;

public class EditorDialog
{
	public EditorDialog(Context ctx, final Belanjaan bel, final TextView totalbelanja){
		String nama=bel.getProduk().getNama();
		final long harga=bel.getProduk().getHarga();
		int val=bel.getQuantity();
		View v=LayoutInflater.from(ctx).inflate(R.layout.editproduk, null);
		final NumPik num=(NumPik) v.findViewById(R.id.numr);
		TextView hargaview= (TextView) v.findViewById(R.id.hargaview);
		final TextView totalview= (TextView) v.findViewById(R.id.totalview);
		AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
		dlg.setView(v);
		dlg.setTitle(nama);
		dlg.setPositiveButton("Set", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					MainActivity.dataBalanjaan.tambah(bel.getProduk(), num.getValue());
					totalbelanja.setText("Rp. "+BelanjaanDataAdapter.PRICE_FORMATTER.format(BelanjaanDataAdapter.total));
				}
			});
		dlg.setNeutralButton("Hapus Belanjaan", null);
		dlg.show();
		totalview.setText("Rp. "+BelanjaanDataAdapter.PRICE_FORMATTER.format(harga*val));
		hargaview.setText("Rp. "+BelanjaanDataAdapter.PRICE_FORMATTER.format(harga));
		num.setMinValue(1);
		num.setMaxValue(200);
		num.setValue(val);
		num.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
				@Override
				public void onValueChange(NumberPicker p1, int p2, int p3) {
					totalview.setText("Rp. "+BelanjaanDataAdapter.PRICE_FORMATTER.format(harga*num.getValue()));
				}
			});
		
	}
	
}
