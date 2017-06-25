package com.agusibrahim.appkasir.Widget;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.*;
import android.widget.*;
import com.agusibrahim.appkasir.Model.*;
import android.text.*;
import com.agusibrahim.appkasir.Fragment.productFragment;
import com.agusibrahim.appkasir.Utils;
import com.agusibrahim.appkasir.Adapter.*;
import java.text.*;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.google.zxing.BarcodeFormat;
import java.util.ArrayList;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import java.util.*;
import android.view.inputmethod.InputMethodManager;
import android.content.DialogInterface;
import android.content.ContentValues;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.agusibrahim.appkasir.MainActivity;
import com.agusibrahim.appkasir.R;
import com.koushikdutta.async.http.*;
import org.json.*;

public class ProdukDialog
{
	private String current = "";
	DecoratedBarcodeView barcodeView;
	boolean lampufles=false;

	private InputMethodManager imm;
	public ProdukDialog(final Context ctx, final Produk dataset){
		String positiveTxt="Tambahkan";
		imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		AlertDialog.Builder dlg=new AlertDialog.Builder(ctx);
		View form=LayoutInflater.from(ctx).inflate(R.layout.produkdlg, null);
		barcodeView = (DecoratedBarcodeView) form.findViewById(R.id.scannerdlg);
		Utils.setupScanner(barcodeView);
		final EditText nama=(EditText) form.findViewById(R.id.namaproduk);
		final EditText kodeprod=(EditText) form.findViewById(R.id.kodeproduk);
		final EditText harga=(EditText) form.findViewById(R.id.harga);
		final EditText stok=(EditText) form.findViewById(R.id.stok);
		final ImageButton scanbtn=(ImageButton) form.findViewById(R.id.scanbtn);
		
		
		// Jika dataset tidak null yang berarti itu adalah mode PEMBARUAN/EDIT
		// Maka kolom akan di isi, serta ditambabkan tombol Neutral (Hapus)
		if(dataset!=null){
			nama.setText(dataset.getNama());
			kodeprod.setText(dataset.getSn());
			harga.setText(Utils.priceFormat(""+ dataset.getHarga()));
			stok.setText(""+dataset.getStok());
			positiveTxt="Perbarui";
			dlg.setNeutralButton("Hapus", null);
		}
		scanbtn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1) {
					if(barcodeView.getVisibility()==View.GONE){
						barcodeView.setVisibility(View.VISIBLE);
						barcodeView.resume();
					}else{
						barcodeView.setVisibility(View.GONE);
						barcodeView.pause();
					}
				}
			});
		dlg.setView(form);
		dlg.setTitle(positiveTxt+" Produk");
		dlg.setPositiveButton(positiveTxt, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					barcodeView.pause();
					ContentValues data = new ContentValues();
					data.put("nama", nama.getText().toString());
					data.put("sn", kodeprod.getText().toString());
					data.put("harga", Long.parseLong(harga.getText().toString().replaceAll("[a-zA-Z\\.]", "")));
					data.put("stok", Integer.parseInt(stok.getText().toString()));
					// Jika mode penambahan
					if(dataset==null){
						MainActivity.dataproduk.tambah(data);
					// Jika mode EDIT
					}else{
						MainActivity.dataproduk.perbarui(dataset, data);
					}
					
				}
			});
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener(){
				@Override
				public void onCancel(DialogInterface p1) {
					barcodeView.pause();
				}
			});
		dlg.setNegativeButton("Batal", null);
		final AlertDialog dialog=dlg.create();
		dialog.show();
		imm.showSoftInput(nama, InputMethodManager.SHOW_IMPLICIT);
		// Override tombol Hapus
		Button hapusBtn=dialog.getButton(AlertDialog.BUTTON3);
		hapusBtn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1) {
					Toast.makeText(p1.getContext(), "Tekan lama untuk menghapus", Toast.LENGTH_LONG).show();
				}
			});
		hapusBtn.setOnLongClickListener(new View.OnLongClickListener(){
				@Override
				public boolean onLongClick(View p1) {
					MainActivity.dataproduk.hapus(dataset);
					dialog.dismiss();
					Toast.makeText(p1.getContext(), "Terhapus", Toast.LENGTH_SHORT).show();
					return false;
				}
			});
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
				if(nama.getText().length()>3&&kodeprod.getText().length()>5&&harga.getText().length()>2&&stok.getText().length()>0) btn.setEnabled(true);
				else btn.setEnabled(false);
				if(harga.isFocused()){
					Utils.priceEdit(harga, p1,this);
				}
			}
			@Override
			public void afterTextChanged(Editable p1) {
			}
		};
		BarcodeCallback callback = new BarcodeCallback() {
			@Override
			public void possibleResultPoints(List<ResultPoint> p1) {
			}
			
			@Override
			public void barcodeResult(BarcodeResult result) {
				kodeprod.setText(result.getText());
				barcodeView.setVisibility(View.GONE);
				barcodeView.pause();
			}
		};
		barcodeView.decodeContinuous(callback);
		nama.addTextChangedListener(watcher);
		kodeprod.addTextChangedListener(watcher);
		harga.addTextChangedListener(watcher);
		stok.addTextChangedListener(watcher);
		
	}
}
