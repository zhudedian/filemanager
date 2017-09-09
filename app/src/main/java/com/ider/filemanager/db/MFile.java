package com.ider.filemanager.db;

import static android.R.attr.type;

/**
 * Created by Eric on 2017/9/8.
 */

public class MFile {
    private int fileType;

    private boolean select;

    private String fileName;

    private String filePath;


    private String savePath;

    private String fileSize;

    public MFile(int type,String fileName,String size){
        this.fileType = type;
        this.fileName = fileName;
        this.fileSize = size;
    }
    public MFile(String fileName,String filePath){
        this.fileType = type;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public MFile(int type,String fileName,String filePath,String size){
        this.fileType = type;
        this.fileName = fileName;
        this.fileSize = size;
        this.filePath = filePath;
    }
    public MFile(int type,String fileName,String filePath,String size,String savePath){
        this.fileType = type;
        this.fileName = fileName;
        this.fileSize = size;
        this.filePath = filePath;
        this.savePath = savePath;
    }

    public void setFileName(String name){
        this.fileName = name;
    }

    public String getFileName(){
        return fileName;
    }

    public void setFilePath(String path){
        this.filePath = path;
    }

    public String getFilePath(){
        return filePath;
    }

    public void setFileSize(String size){
        this.fileSize = size;
    }

    public String getFileSize(){
        return fileSize;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
    public String getSavePath(){
        return savePath;
    }

    public void setFileType(int type){
        this.fileType = type;
    }

    public int getFileType(){
        return fileType;
    }

    public void setSelect(boolean select){
        this.select = select;
    }

    public boolean isSelect(){
        return select;
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof MFile){
            MFile mFile= (MFile) object;
            if (mFile.fileName.equals(this.fileName)&&mFile.filePath.equals(this.filePath)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    @Override
    public int hashCode(){
        return 2;
    }
}
