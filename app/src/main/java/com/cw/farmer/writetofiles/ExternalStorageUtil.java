package com.cw.farmer.writetofiles;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class ExternalStorageUtil {

    public static boolean isExternalStorageMounted(){
        String dirState = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(dirState)){
            return true;
        }else{
            return false;
        }
    }

//    public static String getPublicExternalStorageBaseDir(String dirType){
//        String ret = "";
//
//        if(isExternalStorageMounted()){
//            File file = Environment.getExternalStorageDirectory();
//            ret = file.getAbsolutePath();
//        }
//
//        return ret;
//    }


    public static String getPrivateExternalStorageBaseDir(Context context, String dirType){


        String ret = "";
        if(isExternalStorageMounted()){
            File file = context.getExternalFilesDir(dirType);
            ret = file.getAbsolutePath();
        }
        return ret;
    }
}
