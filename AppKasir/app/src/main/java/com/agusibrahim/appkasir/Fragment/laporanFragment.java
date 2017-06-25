package com.agusibrahim.appkasir.Fragment;
import android.support.v4.app.Fragment;
import android.view.*;
import android.os.*;
import com.agusibrahim.appkasir.R;

public class laporanFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.frag_laporan, container, false);
		return v;
	}
}
