package com.agusibrahim.appkasir;
import android.content.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import com.agusibrahim.appkasir.Model.*;
import android.text.*;

public class ProdukDialog
{
	public ProdukDialog(final Context ctx, final Produk dataset){
		String positiveTxt="Tambahkan";
		AlertDialog.Builder dlg=new AlertDialog.Builder(ctx);
		View form=LayoutInflater.from(ctx).inflate(R.layout.produkdlg, null);
		final TextView nama=(TextView) form.findViewById(R.id.namaproduk);
		final TextView kodeprod=(TextView) form.findViewById(R.id.kodeproduk);
		final TextView harga=(TextView) form.findViewById(R.id.harga);
		// Jika dataset tidak null yang berarti itu adalah mode PEMBARUAN/EDIT
		// Maka kolom akan di isi, serta ditambabkan tombol Neutral (Hapus)
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
					// Jika mode penambahan
					if(dataset==null){
						MainActivity.dataproduk.tambah(data);
					// Jika mode EDIT
					}else{
						MainActivity.dataproduk.perbarui(dataset, data);
					}
				}
			});
		dlg.setNegativeButton("Batal", null);
		final AlertDialog dialog=dlg.create();
		dialog.show();
		// Dibawah ini adalah fungsi agar Button OK di disable jika data belum terisi atau jumlahnya kurang dari kriteria yang ditentukan
		// Gunakan textWatcher disetiap kolom
		final Button btn= dialog.getButton(AlertDialog.BUTTON1);
		if(dataset==null) btn.setEnabled(false);
		TextWatcher watcher=new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
			}
			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
				if(nama.getText().length()>3&&kodeprod.getText().length()>5&&harga.getText().length()>2) btn.setEnabled(true);
				else btn.setEnabled(false);
			}
			@Override
			public void afterTextChanged(Editable p1) {
			}
		};
		nama.addTextChangedListener(watcher);
		kodeprod.addTextChangedListener(watcher);
		harga.addTextChangedListener(watcher);
	}
}
