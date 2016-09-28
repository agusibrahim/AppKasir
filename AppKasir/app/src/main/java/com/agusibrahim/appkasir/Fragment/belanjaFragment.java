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

public class belanjaFragment extends Fragment
{
	
	//private static String[][] dataBelanjaan = {{"",""}};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.belanja, container, false);
		tabelBelanjaan velanjaan=(tabelBelanjaan) v.findViewById(R.id.belanjaan);
		velanjaan.setDataAdapter(MainActivity.dataBalanjaan);
		return v;
	}
	
}
