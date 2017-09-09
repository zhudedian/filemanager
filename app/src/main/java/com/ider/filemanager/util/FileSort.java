package com.ider.filemanager.util;

import com.ider.filemanager.db.MFile;


import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Eric on 2017/9/6.
 */

public class FileSort implements Comparator<MFile> {
    private Collator collator = Collator.getInstance(Locale.CHINA);

    public static void sort(List<MFile> list){
        Collections.sort(list,new FileSort());
    }

    @Override
    public int compare(MFile mFile1 , MFile mFile2){
        int value = collator.compare(String.valueOf(mFile1.getFileType()),String.valueOf(mFile2.getFileType()));
        if (value>0){
            return 1;
        }else if (value<0){
            return -1;
        }else {
            int value2 = collator.compare(mFile1.getFileName(),mFile2.getFileName());
            return value2;
        }
    }
}
