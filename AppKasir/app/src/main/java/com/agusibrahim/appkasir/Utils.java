package com.agusibrahim.appkasir;
import java.text.*;
import android.widget.*;
import android.text.*;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.google.zxing.BarcodeFormat;
import java.util.ArrayList;
import android.view.View;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import java.util.Random;

public class Utils
{
	private static boolean lampufles=false;
	private static String lorem="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec mauris lacus, imperdiet sit amet faucibus a, ultricies a nisl. Proin lacinia orci sed pulvinar mattis. Curabitur at purus ac nulla dignissim commodo eget a massa. Sed dictum dolor tincidunt ante tempor, pretium tristique massa ornare. In hac habitasse platea dictumst. Morbi ut nibh eu est placerat lacinia. Quisque sed neque nec justo condimentum iaculis sed id sem. Phasellus ut elementum orci. Phasellus pretium egestas ultricies. Nulla vestibulum non elit sit amet tristique. Morbi lobortis accumsan dictum.";
	private static String _priceFormat(String s){
		double parsed = Double.parseDouble(s);
		String formatted = NumberFormat.getCurrencyInstance().format(parsed);
		return formatted;
	}
	public static String priceFormat(String s){
		return _priceFormat(s);
	};
	public static String priceFormat(long s){
		return _priceFormat(""+s);
	};
	public static void priceEdit(EditText et, CharSequence p1, TextWatcher watcher){
		et.removeTextChangedListener(watcher);

		String cleanString = p1.toString().replaceAll("[a-zA-Z\\.]", "").replace("Rp","");
		if(cleanString.length()<1){
			et.addTextChangedListener(watcher);
			return;
		}
		String formatted=priceFormat(cleanString);
		et.setText(formatted);
		et.setSelection(formatted.length());

		et.addTextChangedListener(watcher);
	}
	public static void setupScanner(final DecoratedBarcodeView barcodeView) {
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
	public static int randint(int minimum, int maximum){
		Random rn = new Random();
		int range = maximum - minimum + 1;
		int randomNum =  rn.nextInt(range) + minimum;
		return randomNum;
	}
	public static String randWord(){
		String[] loremparts=lorem.split(" ");
		String name1=loremparts[Utils.randint(0, loremparts.length-2)];
		String name2=loremparts[Utils.randint(0, loremparts.length-2)];
		return capitalizeString( name1)+" "+capitalizeString( name2);
	}
	public static String capitalizeString(String string) {
		char[] chars = string.toLowerCase().toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			} else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
				found = false;
			}
		}
		return String.valueOf(chars);
	}
	
}
