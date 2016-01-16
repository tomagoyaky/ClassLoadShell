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
		tv.setText("hello world");
		setContentView(tv);
	}
}
