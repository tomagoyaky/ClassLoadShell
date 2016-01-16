package com.ClassLoaderShell;

import java.lang.Thread.UncaughtExceptionHandler;

import com.ClassLoaderShell.utils.Logger;
import com.ClassLoaderShell.utils.StackTraceUtil;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ProxyApplication extends ShellEntry implements UncaughtExceptionHandler{

	private static final boolean JAVAStyle = true;
	private static final boolean NativeStyle = !JAVAStyle;
	private static Context context;

	static{
		System.loadLibrary("ClassLoaderShell");
	}
	
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
		Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
	}
	
}
