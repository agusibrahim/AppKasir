package com.agusibrahim.appkasir.Fragment;
import android.support.v4.app.*;
import android.os.*;
import android.view.*;
import com.agusibrahim.appkasir.*;
import com.agusibrahim.appkasir.Adapter.*;
import de.codecrafters.tableview.toolkit.*;
import java.util.*;
import com.agusibrahim.appkasir.Model.Produk;
import android.support.design.widget.*;
import android.text.*;
import android.widget.*;
import com.agusibrahim.appkasir.Widget.*;
import de.codecrafters.tableview.listeners.*;
import com.agusibrahim.appkasir.Model.*;
import android.support.v7.app.AlertDialog;
import com.koushikdutta.async.http.WebSocket;
import org.json.JSONObject;

public class belanjaFragment extends Fragment
{
	public static TextView totaljum;
	public static BottomSheetBehavior bottomSheetBehavior;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.belanja, container, false);
		tabelBelanjaan velanjaan=(tabelBelanjaan) v.findViewById(R.id.belanjaan);
		velanjaan.setDataAdapter(MainActivity.dataBalanjaan);
		velanjaan.addDataClickListener(new DtaClickListener());
		velanjaan.addDataLongClickListener(new DataLongClickListener());
		setHasOptionsMenu(true);
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		FloatingActionButton fabshop=(FloatingActionButton) view.findViewById(R.id.fab_shopping);
		totaljum=(TextView) view.findViewById(R.id.totaljumlah);
		bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheet));
		final inputProdukScanner shop=new inputProdukScanner(view.getContext());
		shop.setOnProductIdentified(new inputProdukScanner.OnProductIndentified(){
				@Override
				public void onIdentified(Produk produk) {
					MainActivity.dataBalanjaan.tambah(produk, -1);
					// Update totalJumalah di BottomSheet
					totaljum.setText(Utils.priceFormat(BelanjaanDataAdapter.total));
					publishProd(produk);
					android.util.Log.d("prod", "Identified :"+produk.getNama());
				}
			});
		fabshop.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1) {
					shop.shoping();
				}
			});
		
		if(BelanjaanDataAdapter.total!=0){
			totaljum.setText(Utils.priceFormat(BelanjaanDataAdapter.total));
			bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
		}else{
			bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
		}
	}
	private class DtaClickListener implements TableDataClickListener<Belanjaan> {
		@Override
		public void onDataClicked(int p1, Belanjaan belanjaan) {
			
			//new inputProdukScanner(getActivity()).shoping();
			new EditorDialog(getActivity(), belanjaan, totaljum);
		}
    }
	private class DataLongClickListener implements TableDataLongClickListener<Belanjaan> {
        @Override
        public boolean onDataLongClicked(int rowIndex, Belanjaan belanjaan) {
            //showdlg(belanjaan.getProduk().getNama(), belanjaan.getProduk().getHarga(), belanjaan.getQuantity());
			return true;
        }
    }
	private void publishProd(Produk p){
		for(WebSocket ws:MainActivity._sockets){
			try {
				JSONObject jd=new JSONObject();
				jd.put("n", p.getNama());
				jd.put("s", "pcs");
				jd.put("q", 1);
				jd.put("no", p.getSn());
				jd.put("p", p.getHarga());
				ws.send(jd.toString());
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Err: "+e.getMessage(),1).show();
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.mainmenu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
}
