package com.agusibrahim.appkasir;
import android.app.*;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

public class Apps extends Application
{
	@Override
	public void onCreate() {
		super.onCreate();
		CustomActivityOnCrash.install(this);
	}
}
