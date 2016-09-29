package com.agusibrahim.appkasir;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.journeyapps.barcodescanner.*;
import android.view.inputmethod.*;
import java.util.*;
import com.google.zxing.*;
import android.text.*;
import com.agusibrahim.appkasir.Model.*;
import android.support.v7.app.AlertDialog;

public class inputProdukScanner
{
	Context ctx;
	DecoratedBarcodeView barcodeView;
	InputMethodManager imm;
	Produk produk_terindentifikasi=null;
	public inputProdukScanner(Context ctx){
		this.ctx=ctx;
		this.imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	public void tambahkanProduk(){
		View v=LayoutInflater.from(ctx).inflate(R.layout.inputproduk_scanner, null);
		final EditText namaproduk=(EditText) v.findViewById(R.id.new_namaproduk);
		final EditText hargaproduk=(EditText) v.findViewById(R.id.new_hargaproduk);
		final EditText stokproduk=(EditText) v.findViewById(R.id.new_stok);
		final Button tambahkanbtn=(Button) v.findViewById(R.id.tambhakanbtn);
		final Button cancelbtn=(Button) v.findViewById(R.id.cancelbtn);
		final Button selesaibtn=(Button) v.findViewById(R.id.selesaibtn);
		
		namaproduk.setEnabled(false);
		hargaproduk.setEnabled(false);
		stokproduk.setEnabled(false);
		final BarcodeCallback callback = new BarcodeCallback() {
			@Override
			public void barcodeResult(BarcodeResult result) {
				if (result.getText() != null) {
					namaproduk.setEnabled(true);
					hargaproduk.setEnabled(true);
					stokproduk.setEnabled(true);
					namaproduk.requestFocus();
					produk_terindentifikasi=Produk.getBySN(ctx, result.getText());
					if(produk_terindentifikasi!=null){
						tambahkanbtn.setText("Perbarui");
						namaproduk.setText(produk_terindentifikasi.getNama());
						hargaproduk.setText(""+produk_terindentifikasi.getHarga());
						stokproduk.setText(""+produk_terindentifikasi.getStok());
					}else{
						tambahkanbtn.setText("Tambahkan");
					}
					imm.showSoftInput(namaproduk, InputMethodManager.SHOW_IMPLICIT);
					barcodeView.setStatusText(result.getText());
				}
			}
			@Override
			public void possibleResultPoints(List<ResultPoint> resultPoints) {
			}
		};
		barcodeView = (DecoratedBarcodeView) v.findViewById(R.id.scanner);
		barcodeView.setStatusText("Arahkan ke barcode");
		ArrayList<BarcodeFormat> formatList = new ArrayList<BarcodeFormat>();
		formatList.add(BarcodeFormat.EAN_13);
		barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formatList, null, null));
		AlertDialog.Builder dlg=new AlertDialog.Builder(ctx);
		dlg.setView(v);
		dlg.setCancelable(false);
		dlg.setTitle("Tambahkan Produk");
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener(){
				@Override
				public void onCancel(DialogInterface p1) {
					barcodeView.pause();
				}
			});
		final AlertDialog dialog=dlg.create();
		cancelbtn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1) {
					barcodeView.pause();
					dialog.dismiss();
				}
			});
			
		tambahkanbtn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1) {
					ContentValues data = new ContentValues();
					data.put("nama", namaproduk.getText().toString());
					data.put("sn", barcodeView.getStatusView().getText().toString());
					data.put("harga", Long.parseLong(hargaproduk.getText().toString()));
					data.put("stok", Integer.parseInt(stokproduk.getText().toString()));
					if(produk_terindentifikasi==null) MainActivity.dataproduk.tambah(data);
					else MainActivity.dataproduk.perbarui(produk_terindentifikasi, data);
					imm.hideSoftInputFromWindow(namaproduk.getWindowToken(), 0);
					namaproduk.setText("");
					hargaproduk.setText("");
					stokproduk.setText("");
					namaproduk.setEnabled(false);
					hargaproduk.setEnabled(false);
					stokproduk.setEnabled(false);
					barcodeView.setStatusText("Arahkan ke barcode");
				}
			});
		dialog.show();
		tambahkanbtn.setEnabled(false);
		TextWatcher watcher=new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
			}
			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
				String s=stokproduk.getText().toString();
				int stok=0;
				if(s.length()>0){
					stok=Integer.parseInt(s);
				}
				if(namaproduk.getText().length()>3&&stok>0&&hargaproduk.getText().length()>2) tambahkanbtn.setEnabled(true);
				else tambahkanbtn.setEnabled(false);
			}
			@Override
			public void afterTextChanged(Editable p1) {
			}
		};
		namaproduk.addTextChangedListener(watcher);
		hargaproduk.addTextChangedListener(watcher);
		stokproduk.addTextChangedListener(watcher);
		barcodeView.decodeContinuous(callback);
		barcodeView.resume();
	}
}
