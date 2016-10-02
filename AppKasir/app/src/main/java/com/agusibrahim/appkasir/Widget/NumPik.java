package com.agusibrahim.appkasir.Widget;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;

public class NumPik extends android.widget.NumberPicker {

	public NumPik(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void addView(View child) {
		super.addView(child);
		updateView(child);
	}

	@Override
	public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		updateView(child);
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, params);
		updateView(child);
	}

	private void updateView(View view) {
		if(view instanceof EditText){
			TextView txt= (EditText) view;
			txt.setTextSize(25);
			txt.setTypeface(null, Typeface.BOLD);
			txt.setTextColor(Color.parseColor("#333333"));
		}
	}

}
