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
	public static Snackbar totalbelanja;
	//private static String[][] dataBelanjaan = {{"",""}};
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
		CoordinatorLayout coor=(CoordinatorLayout) view.findViewById(R.id.kor);
		totalbelanja= Snackbar.make(coor, Html.fromHtml("TOTAL: 		<b>Rp. "+BelanjaanDataAdapter.PRICE_FORMATTER.format(BelanjaanDataAdapter.total)+"</b>"), Snackbar.LENGTH_INDEFINITE);
		totalbelanja.setAction("Bayar", new View.OnClickListener(){
				@Override
				public void onClick(View p1) {}
			});
		// Mencegah Snackbar hilang saat tombolnya ditekan
		// Diambil dari http://stackoverflow.com/a/39202774
		ViewGroup group = (ViewGroup) totalbelanja.getView();
		for(int i=0; i< group.getChildCount();i++){
			View v = group.getChildAt(i);
			if(v instanceof Button){
				v.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							
						}
					});
			}
		}
		if(BelanjaanDataAdapter.total!=0) totalbelanja.show();
	}
	
	
}
