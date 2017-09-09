package com.ider.filemanager.util;

import com.ider.filemanager.db.MFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2017/9/8.
 */

public class ScanFile {
    public static List<MFile> scanDir(File dir){
        List<MFile> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files!=null){
            for (File file:files){
                MFile mFile = new MFile(getFileType(file),file.getName(),file.getPath(),FileUtil.getSize(file));
                list.add(mFile);
            }
        }
        FileSort.sort(list);
        return list;
    }

    public static int getFileType(File file){
        if (file.isDirectory()) {
            return 1;
        } else if (FileUtil.getFileType(file).equals(FileUtil.str_video_type)){
            return 2;
        }else if (FileUtil.getFileType(file).equals(FileUtil.str_audio_type)){
            return 3;
        }else if (FileUtil.getFileType(file).equals(FileUtil.str_image_type)){
            return 4;
        }else if (FileUtil.getFileType(file).equals(FileUtil.str_apk_type)){
            return 5;
        }else if (FileUtil.getFileType(file).equals(FileUtil.str_zip_type)){
            return 6;
        }else if (FileUtil.getFileType(file).equals(FileUtil.str_pdf_type)){
            return 7;
        }else if (FileUtil.getFileType(file).equals(FileUtil.str_txt_type)){
            return 8;
        }else {
            return 9;
        }
    }
}
