package com.ider.filemanager.util;

import com.ider.filemanager.db.MFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Eric on 2017/9/9.
 */

public class FilePaste {

    public static List<MFile> getPasteFiles(List<MFile> mFiles,String savePath,List<MFile> overWrites){
        List<MFile> list = new ArrayList<>();
        for (int i=0;i<mFiles.size();i++){
            MFile mFile = mFiles.get(i);
            String newPath = savePath.endsWith(File.separator)?savePath+mFile.getFileName(): savePath+File.separator+mFile.getFileName();
            File newFile = new File(newPath);
            if (newFile.exists()){
                if (mFile.getFileType() == 1) {
                    File file = new File(mFile.getFilePath());
                    getDirFile(list, overWrites ,file, savePath + File.separator + mFile.getFileName());
                } else {
                    mFile.setSavePath(savePath);
                    overWrites.add(mFile);
                }
            }else {
                if (mFile.getFileType() == 1) {
                    File file = new File(mFile.getFilePath());
                    getDirFile(list,file, savePath + File.separator + mFile.getFileName());
                } else {
                    mFile.setSavePath(savePath);
                    list.add(mFile);
                }
            }
        }
        return list;
    }

    private static void getDirFile(List<MFile> list,File dir,String savePath){
        File[] files = dir.listFiles();
        if (files!=null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getDirFile(list,file,savePath + File.separator + file.getName());
                } else {
                    MFile mFile = new MFile(ScanFile.getFileType(file), file.getName(), file.getPath(), FileUtil.getSize(file), savePath);
                    list.add(mFile);
                }
            }
        }
    }
    private static void getDirFile(List<MFile> list,List<MFile> overs,File dir,String savePath){
        File[] files = dir.listFiles();
        if (files!=null) {
            for (File file : files) {
                String newPath = savePath.endsWith(File.separator)?savePath+file.getName(): savePath+File.separator+file.getName();
                File newFile = new File(newPath);
                if (newFile.exists()){
                    if (file.isDirectory()){
                        getDirFile(list, overs ,file, savePath + File.separator + file.getName());
                    }else{
                        MFile mFile = new MFile(ScanFile.getFileType(file), file.getName(), file.getPath(), FileUtil.getSize(file), savePath);
                        overs.add(mFile);
                    }
                }else {
                    if (file.isDirectory()) {
                        getDirFile(list, file, savePath + File.separator + file.getName());
                    } else {
                        MFile mFile = new MFile(ScanFile.getFileType(file), file.getName(), file.getPath(), FileUtil.getSize(file), savePath);
                        list.add(mFile);
                    }
                }
            }
        }
    }
}
