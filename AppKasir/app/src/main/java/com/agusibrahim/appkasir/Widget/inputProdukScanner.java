package com.agusibrahim.appkasir.Widget;
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
import com.agusibrahim.appkasir.Adapter.*;
import android.os.*;
import com.agusibrahim.appkasir.Fragment.*;
import com.agusibrahim.appkasir.*;

public class inputProdukScanner {
	Context ctx;
	DecoratedBarcodeView barcodeView;
	InputMethodManager imm;
	private Produk produk_terindentifikasi=null;
	AlertDialog.Builder dlg;
	boolean lampufles=false;
	public inputProdukScanner(Context ctx) {
		this.ctx = ctx;
		this.imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		dlg = new AlertDialog.Builder(ctx);
	}
	public void setupScanner() {
		barcodeView.setStatusText("Arahkan ke barcode");
		ArrayList<BarcodeFormat> formatList = new ArrayList<BarcodeFormat>();
		formatList.add(BarcodeFormat.EAN_13);
		barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formatList, null, null));
		// Toggle flashlight saat viewfinder barcode disentuh
		barcodeView.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1) {
					if (lampufles) barcodeView.setTorchOff();
					else barcodeView.setTorchOn();
				}
			});
		barcodeView.setTorchListener(new DecoratedBarcodeView.TorchListener(){

				@Override
				public void onTorchOn() {
					lampufles = true;
				}

				@Override
				public void onTorchOff() {
					lampufles = false;
				}
			});
	}
	public void shoping() {
		View v=LayoutInflater.from(ctx).inflate(R.layout.shopingscanner, null);
		final TextView namaproduk=(TextView) v.findViewById(R.id.scanNamaProduk);
		final TextView hargaproduk=(TextView) v.findViewById(R.id.scanHargaProduk);
		final TextView snProduk=(TextView) v.findViewById(R.id.scanSN);
		final CheckBox tanpakonf=(CheckBox) v.findViewById(R.id.autoaddcheck);
		barcodeView = (DecoratedBarcodeView) v.findViewById(R.id.scannershop);
		setupScanner();
		dlg.setView(v);
		dlg.setTitle("Pemindai Barcode");
		dlg.setCancelable(false);
		dlg.setNegativeButton("Selesai", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					barcodeView.pause();
				}
			});
		// ini hanya buat nampilin tombol "Tambahkan" pada dialog
		// onclick sengaja ga di isi
		// Meng-override onClick (lihat okBtn) agar saat di klik dialog kaga ngilang
		dlg.setPositiveButton("Tambahkan", null);
		AlertDialog dialog=dlg.show();
		// tombol positive (Tambahkan)
		final Button okBtn=dialog.getButton(AlertDialog.BUTTON1);
		okBtn.setEnabled(false);
		tanpakonf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton p1, boolean checked) {
					if (checked) okBtn.setEnabled(false);
					else okBtn.setEnabled(true);
				}
			});
		// Override onClick positiveButton pada dialog, 
		okBtn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1) {
					MainActivity.dataBalanjaan.tambah(produk_terindentifikasi, -1);
					// Update totalJumalah di BottomSheet
					belanjaFragment.totaljum.setText("Rp. " + BelanjaanDataAdapter.PRICE_FORMATTER.format(BelanjaanDataAdapter.total));
				}
			});

		barcodeView.decodeContinuous(new BarcodeCallback(){
				@Override
				public void barcodeResult(BarcodeResult result) {
					produk_terindentifikasi = Produk.getBySN(ctx, result.getText());
					// Jika produk kedaftar di Database produk
					// Kalo ngga ya diem
					if (produk_terindentifikasi != null) {
						namaproduk.setText(produk_terindentifikasi.getNama());
						hargaproduk.setText("Rp. " + BelanjaanDataAdapter.PRICE_FORMATTER.format(produk_terindentifikasi.getHarga()));
						snProduk.setText(result.getText());
						namaproduk.setVisibility(View.VISIBLE);
						hargaproduk.setVisibility(View.VISIBLE);
						// Jika mode otomatis (tanpa konfirm) di cek
						if (tanpakonf.isChecked()) {
							okBtn.setEnabled(false);
							MainActivity.dataBalanjaan.tambah(produk_terindentifikasi, -1);
							// Update totalJumalah di BottomSheet
							belanjaFragment.totaljum.setText("Rp. " + BelanjaanDataAdapter.PRICE_FORMATTER.format(BelanjaanDataAdapter.total));
							// Pause dulu kamera jika sudah berhasil mengidentifikasi produk
							// setelah 2dtk baru di resume
							// ini utk menghindari scan beruntun, dlm waktu 2dtk jauhkan barcode dari kamera atau aplikasi akan mengupdate status Quantity-nya
							barcodeView.pause();
							Handler handler = new Handler();
							handler.postDelayed(new Runnable() {
									@Override
									public void run() {
										barcodeView.resume();
									}
								}, 2000);
						} else {
							okBtn.setEnabled(true);
						}
					}
				}

				@Override
				public void possibleResultPoints(List<ResultPoint> p1) {
				}
			});
		barcodeView.resume();
	}
	public void tambahkanProduk() {
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
					produk_terindentifikasi = Produk.getBySN(ctx, result.getText());
					if (produk_terindentifikasi != null) {
						tambahkanbtn.setText("Perbarui");
						namaproduk.setText(produk_terindentifikasi.getNama());
						hargaproduk.setText("" + produk_terindentifikasi.getHarga());
						stokproduk.setText("" + produk_terindentifikasi.getStok());
					} else {
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
		setupScanner();
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
					if (produk_terindentifikasi == null) MainActivity.dataproduk.tambah(data);
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
				if (s.length() > 0) {
					stok = Integer.parseInt(s);
				}
				if (namaproduk.getText().length() > 3 && stok > 0 && hargaproduk.getText().length() > 2) tambahkanbtn.setEnabled(true);
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
