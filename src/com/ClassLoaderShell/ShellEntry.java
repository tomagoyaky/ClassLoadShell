package com.ClassLoaderShell;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;

import com.ClassLoaderShell.utils.AssetsUtil;
import com.ClassLoaderShell.utils.Logger;
import com.ClassLoaderShell.utils.RefInvokeUtil;
import com.ClassLoaderShell.utils.StackTraceUtil;

import dalvik.system.BaseDexClassLoader;

/**
 * ShellEntry为设计的外壳入口点,它所做的工作如下:
 * (1)加载dex文件
 * (2)将Application还原为原来的
 * */
public class ShellEntry extends Application{

	protected String dexPath;
	protected String odexPath;
	protected String libPath;
	private File odex;
	private File libs;
	private File dex;
	
	@Override
	protected void attachBaseContext(Context baseContext) {
		super.attachBaseContext(baseContext);
		Logger.log(Log.DEBUG, StackTraceUtil.getMethodWithClassName());
		InitEnvironment(baseContext);
	}
	
	private void releaseAssetsFile(Context baseContext, String fileName, String dstDir){
		Logger.log(Log.DEBUG, StackTraceUtil.getMethodWithClassName());

		String FilePath = dstDir + File.separator + fileName;
		File targetFile = new File(FilePath);
		if(!targetFile.exists()){
			Logger.log(Log.WARN, "release file from apk's assets:" + fileName);
		}else{
			targetFile.delete();
		}
		AssetsUtil.CopyAssertJarToFile(baseContext, fileName, targetFile);
		if(targetFile.exists())
			Logger.log(Log.INFO, "filePath:" + FilePath);
		else
			Logger.log(Log.ERROR, "Not exist, filePath:" + FilePath);
	}
	
	private void InitEnvironment(Context baseContext) {
		Logger.log(Log.DEBUG, StackTraceUtil.getMethodWithClassName());

		odex = baseContext.getDir(Constants.payload_odex, 	MODE_PRIVATE);
		libs = baseContext.getDir(Constants.payload_lib, 	MODE_PRIVATE);
		dex  = baseContext.getDir(Constants.payload_dex, 	MODE_PRIVATE);
		
		this.dexPath 	= dex.getAbsolutePath() + "/" + Constants.dexFileName;
		this.odexPath 	= odex.getAbsolutePath();
		this.libPath 	= libs.getAbsolutePath();
		
		releaseAssetsFile(baseContext, Constants.dexFileName, 		dex.getAbsolutePath());
		releaseAssetsFile(baseContext, "libClassLoaderShell.so", 	libs.getAbsolutePath());
	}
	
	@SuppressWarnings("unchecked")
	protected void DexClassLoaderWithJava(Context baseContext) {
		Logger.log(Log.DEBUG, StackTraceUtil.getMethodWithClassName());
		
		// XXX 01 获取ClassDexLoader
//		ClassLoader curClassLoader = (ClassLoader) RefInvokeUtil.getFieldObject(Constants.ClassPath.android_app_LoadedApk, wr.get(), "mClassLoader");
//		if(baseContext.getClassLoader().equals(curClassLoader)){
//			Logger.log(Log.WARN, "classLoader attach method is same");
//		}
//		DexClassLoader dexLoader = new DexClassLoader(
//				this.dexPath, 
//				this.odexPath, 
//				this.libPath, 
//				baseContext.getClassLoader());
		
		BaseDexClassLoader dexLoader = new BaseDexClassLoader(
				this.dexPath, 
				odex,
				this.libPath, 
				baseContext.getClassLoader());
		// XXX 02 把当前进程的DexClassLoader 设置成了被加壳apk的DexClassLoader
		Object currentActivityThread = RefInvokeUtil.invokeStaticMethod(Constants.ClassPath.android_app_ActivityThread, "currentActivityThread", new Class[] {}, new Object[] {});
		String packageName = baseContext.getPackageName();
		ArrayMap<String, WeakReference<?>> mPackages = (ArrayMap<String, WeakReference<?>>) 
				RefInvokeUtil.getFieldObject(Constants.ClassPath.android_app_ActivityThread, currentActivityThread, "mPackages");
		WeakReference<?> wr = (WeakReference<?>) mPackages.get(packageName);
		
		RefInvokeUtil.setFieldObject(Constants.ClassPath.android_app_LoadedApk, "mClassLoader", wr.get(), dexLoader);
	}

	private String getValueFromApplicationMetaData(Context context, String keyName){
		String value = null;
		ApplicationInfo ai = null;
		try {
			ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			if (bundle != null && bundle.containsKey(keyName)){
				value = bundle.getString(keyName);
			}else{
				return null;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	protected void ResumeApplicationWithJava(Context context) {
		Logger.log(Log.DEBUG, StackTraceUtil.getMethodWithClassName());

		// XXX 0x1 从AndroidManifest.xml中的aplication节点中读取meta-data
		String appClassName = getValueFromApplicationMetaData(context, Constants.ClassPath.application_meta_data);
		Logger.log(Log.INFO, "appClassName:[" + appClassName + "]");
		if(appClassName != null){

			Object currentActivityThread = RefInvokeUtil.invokeStaticMethod(Constants.ClassPath.android_app_ActivityThread, "currentActivityThread", new Class[]{}, new Object[]{});
			Object mBoundApplication = RefInvokeUtil.getFieldObject(Constants.ClassPath.android_app_ActivityThread, currentActivityThread, "mBoundApplication");
			Object loadedApkInfo = RefInvokeUtil.getFieldObject("android.app.ActivityThread$AppBindData", mBoundApplication, "info");			
			Application oldApplication = (Application)RefInvokeUtil.getFieldObject(Constants.ClassPath.android_app_ActivityThread, currentActivityThread, "mInitialApplication"); // ProxyApplication
			
			// XXX 0x01 删除
			RefInvokeUtil.setFieldObject(Constants.ClassPath.android_app_LoadedApk, "mApplication", loadedApkInfo, null); //ProxyApplication
			ArrayList<Application> mAllApplications = (ArrayList<Application>)RefInvokeUtil.getFieldObject(
					Constants.ClassPath.android_app_ActivityThread,currentActivityThread, "mAllApplications");
			mAllApplications.remove(oldApplication);

			// XXX 0x02 为原来的Application创建
			ApplicationInfo appinfo_In_LoadedApk = (ApplicationInfo) RefInvokeUtil.getFieldObject(Constants.ClassPath.android_app_LoadedApk, loadedApkInfo, "mApplicationInfo");
			ApplicationInfo appinfo_In_AppBindData = (ApplicationInfo) RefInvokeUtil.getFieldObject("android.app.ActivityThread$AppBindData", mBoundApplication, "appInfo");
			appinfo_In_LoadedApk.className = appClassName;
			appinfo_In_AppBindData.className = appClassName;
			Application newApplication = (Application) RefInvokeUtil.invokeMethod(Constants.ClassPath.android_app_LoadedApk, "makeApplication", loadedApkInfo,
				new Class[] { boolean.class, Instrumentation.class },
				new Object[] { false, null });
			RefInvokeUtil.setFieldObject("android.app.ActivityThread", "mInitialApplication", currentActivityThread, newApplication);

			// XXX 0x04 设置上下文
			ArrayMap<Object, Object> mProviderMap = (ArrayMap<Object, Object>) RefInvokeUtil.getFieldObject(Constants.ClassPath.android_app_ActivityThread, currentActivityThread, "mProviderMap");
			Iterator<Object> it = mProviderMap.values().iterator();
			while (it.hasNext()) 
			{
				Object providerClientRecord = it.next();
				Object localProvider = RefInvokeUtil.getFieldObject("android.app.ActivityThread$ProviderClientRecord", providerClientRecord, "mLocalProvider");
				RefInvokeUtil.setFieldObject("android.content.ContentProvider", "mContext", localProvider, newApplication);
			}
			// XXX 0x05 调用Application中的onCreate()
			newApplication.onCreate();
		}
	}

	// TODO no implements
	protected native void DexClassLoaderWithNative(Context baseContext);
	protected native void ResumeApplicationWithNative(Context context);
	protected native void DexProcess(String dexFilePath);
}
