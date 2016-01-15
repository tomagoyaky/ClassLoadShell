package com.ClassLoaderShell.utils;

public class StackTraceUtil {

	private static final int level = 3;
	public static boolean StackTraceEnable = true;
	public static final String StackTraceUnableMsg = "StackTrace Is Unable ";
	// testFunc()
	public static String getMethodName(){
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[level];
		if(StackTraceEnable)
			return stackTraceElement.getMethodName();
		else
			return StackTraceUnableMsg;
	}
	
	// com.tomagoyaky.testFunc()
	public static String getMethodWithClassName(){
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[level];
		if(StackTraceEnable)
			return stackTraceElement.getClassName() + "->" + stackTraceElement.getMethodName();
		else
			return StackTraceUnableMsg;
	}
}
