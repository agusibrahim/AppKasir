package com.agusibrahim.appkasir;
import android.content.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import com.agusibrahim.appkasir.Model.*;

public class ProdukDialog
{
	public ProdukDialog(final Context ctx, final Produk dataset){
		String positiveTxt="Tambahkan";
		AlertDialog.Builder dlg=new AlertDialog.Builder(ctx);
		View form=LayoutInflater.from(ctx).inflate(R.layout.produkdlg, null);
		final TextView nama=(TextView) form.findViewById(R.id.namaproduk);
		final TextView kodeprod=(TextView) form.findViewById(R.id.kodeproduk);
		final TextView harga=(TextView) form.findViewById(R.id.harga);
		if(dataset!=null){
			nama.setText(dataset.getNama());
			kodeprod.setText(dataset.getSn());
			harga.setText(""+dataset.getHarga());
			positiveTxt="Perbarui";
			dlg.setNeutralButton("Hapus", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2) {
						MainActivity.dataproduk.hapus(dataset);
					}
				});
		}
		dlg.setView(form);
		dlg.setTitle(positiveTxt+" Produk");
		dlg.setPositiveButton(positiveTxt, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					ContentValues data = new ContentValues();
					data.put("nama", nama.getText().toString());
					data.put("sn", kodeprod.getText().toString());
					data.put("harga", Long.parseLong(harga.getText().toString()));
					if(dataset==null){
						MainActivity.dataproduk.tambah(data);
					}else{
						MainActivity.dataproduk.perbarui(dataset, data);
					}
				}
			});
		dlg.setNegativeButton("Batal", null);
		dlg.show();
	}
}
