package com.ClassLoaderShell;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity{

	private Context context = null;
	private Class<?> clazz = null;
	protected static final String TAG = "tomagoyaky";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		TextView tv = new TextView(context);
		tv.setText(invokeRemoteNativeDexMethod("world"));
		setContentView(tv);
		
		StringBuilder sb = new StringBuilder();
		sb.append("");
		sb.toString();
	}

	private static native String invokeRemoteNativeDexMethod(String msg);
}
