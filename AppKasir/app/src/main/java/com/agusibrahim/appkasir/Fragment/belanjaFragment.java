package com.agusibrahim.appkasir.Fragment;
import android.support.v4.app.*;
import android.os.*;
import android.view.*;
import com.agusibrahim.appkasir.R;
import com.agusibrahim.appkasir.*;
import com.agusibrahim.appkasir.Adapter.*;
import de.codecrafters.tableview.toolkit.*;
import java.util.*;
import com.agusibrahim.appkasir.Model.Produk;
import android.support.design.widget.*;
import android.text.*;
import android.widget.*;

public class belanjaFragment extends Fragment
{
	private BottomSheetBehavior bottomSheetBehavior;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.belanja, container, false);
		tabelBelanjaan velanjaan=(tabelBelanjaan) v.findViewById(R.id.belanjaan);
		velanjaan.setDataAdapter(MainActivity.dataBalanjaan);
		
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		FloatingActionButton fabshop=(FloatingActionButton) view.findViewById(R.id.fab_shopping);
		TextView totaljum=(TextView) view.findViewById(R.id.totaljumlah);
		bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheet));
		
		fabshop.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1) {
					new inputProdukScanner(p1.getContext()).shoping();
				}
			});
		
		if(BelanjaanDataAdapter.total!=0){
			totaljum.setText("Rp. "+BelanjaanDataAdapter.PRICE_FORMATTER.format(BelanjaanDataAdapter.total));
			bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
		}else{
			bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
		}
	}
	
	
}
