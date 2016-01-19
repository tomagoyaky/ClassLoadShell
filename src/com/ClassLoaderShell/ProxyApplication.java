package com.ClassLoaderShell;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

import com.ClassLoaderShell.utils.Logger;
import com.ClassLoaderShell.utils.StackTraceUtil;

public class ProxyApplication extends ShellEntry implements UncaughtExceptionHandler{

	private static final boolean JAVAStyle = true;
	private static final boolean NativeStyle = !JAVAStyle;
	private static Context context;

	
	@Override
	protected void attachBaseContext(Context baseContext) {
		super.attachBaseContext(baseContext);
		Logger.log(Log.DEBUG, StackTraceUtil.getMethodWithClassName());
		
		// 在比较靠前的地方加载我们的dex文件
		if(JAVAStyle){
			DexClassLoaderWithJava(baseContext);
		}else{
			DexClassLoaderWithNative(baseContext);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.log(Log.DEBUG, StackTraceUtil.getMethodWithClassName());
		context = this;

		try{
			System.loadLibrary("ClassLoaderShell");
		} catch(UnsatisfiedLinkError ex){
			Logger.log(Log.ERROR, ex.getMessage());
		}

		DexClassLoaderWithNative(context);
		DexProcess(this.dexPath);
		
		// XXX 01加载dex文件
		// (这步在attachBaseContext中完成)
		
		// XXX 02根据选择还原Applicaion
		if(JAVAStyle){
			ResumeApplicationWithJava(context);
		}else{
			ResumeApplicationWithNative(context);
		}
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Logger.log(Log.ERROR, ex.getMessage());
		new AlertDialog.Builder(context).setTitle("提示").setCancelable(false)  
	        .setMessage("程序崩溃了...").setNeutralButton("我知道了", new OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	                System.exit(0);  
	            }  
	        })  
	        .create().show();  
	}
	
}
