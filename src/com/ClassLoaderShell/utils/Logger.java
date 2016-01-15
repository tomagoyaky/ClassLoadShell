package com.ClassLoaderShell.utils;

import android.util.Log;
public class Logger {

	private static final String TAG = "XXX";
	public static String log(int logType, String msg){
		
		// filter
		if(msg.equals(StackTraceUtil.StackTraceUnableMsg)){
			return StackTraceUtil.StackTraceUnableMsg;
		}
		
		switch (logType) {
		case Log.ASSERT:
		case Log.DEBUG:
			Log.d(TAG, msg); break;
		case Log.ERROR:
			Log.e(TAG, "[-]" + msg); break;
		case Log.INFO:
			Log.i(TAG, msg); break;
		case Log.VERBOSE:
			Log.v(TAG, msg); break;
		case Log.WARN:
			Log.w(TAG, "[!]" + msg); break;
		default:
			break;
		}
		return msg;
	}
}
