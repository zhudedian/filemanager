package com.ider.filemanager.util;

import java.io.File;

/**
 * Created by Eric on 2017/9/9.
 */

public class FileDelete {

    public static void delete(File file){
        if (file.isDirectory()){
            if (file.exists()) {
                dirDelete(file);
            }
        }else {
            if (file.exists()){
                file.delete();
            }
        }
    }
    private  static void dirDelete(File dir){
        File[] files = dir.listFiles();
        for (File file:files){
            if (file.isDirectory()){
                dirDelete(file);
            }else {
                file.delete();
            }
        }
        dir.delete();
    }
}
