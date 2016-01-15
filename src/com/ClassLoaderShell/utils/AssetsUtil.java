package com.ClassLoaderShell.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

public class AssetsUtil {

	public static void CopyAssertJarToFile(Context context, String fileName, File file) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            if(file.exists()){
            	Logger.log(Log.ERROR, file.getAbsolutePath() + " is exist.");
            	return;
            }
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
