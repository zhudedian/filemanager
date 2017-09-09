package com.ider.filemanager.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ider.filemanager.db.MFile;
import com.ider.filemanager.view.ProDiaHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;


/**
 * Created by Eric on 2017/9/9.
 */

public class FileCopy {
    public static boolean startCopy;
    public static boolean copy(List<MFile> mFiles, Handler handler){
        startCopy = true;
        for (int i=0;startCopy&&i<mFiles.size();i++){
            MFile mFile = mFiles.get(i);
            String savePath = mFile.getSavePath();
            File dir = new File(savePath);
            dir.mkdirs();
            String newPath = savePath.endsWith(File.separator)?savePath+mFile.getFileName():savePath+File.separator+mFile.getFileName();
            if (!copy(mFile.getFilePath(),newPath)){
                return false;
            }
            Message msg = new Message();
            msg.what = 0;
            Bundle bundle = new Bundle();
            if (i<mFiles.size()-1) {
                bundle.putString("message", mFiles.get(i + 1).getFileName());
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }
        handler.sendEmptyMessage(1);

        return true;
    }
    public static boolean copy(String oldPath,String newPath){
        startCopy = true;
        File file = new File(oldPath);
        if (file.isDirectory()){
            return copyFolder(oldPath,newPath);
        }else {
            return copyFile(oldPath,newPath);
        }
    }
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newFile = new File(newPath);
            if (oldfile.exists()&&startCopy) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[4096];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    if (startCopy) {
                        bytesum += byteread; //字节数 文件大小
                        fs.write(buffer, 0, byteread);
                    }else {
                        newFile.delete();
                        return false;
                    }
                }
                fs.close();
                inStream.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp;
            for (int i = 0; i < file.length&&startCopy; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    copyFile(temp.getPath(),newPath + File.separator + temp.getName());
                } else if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i]);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
