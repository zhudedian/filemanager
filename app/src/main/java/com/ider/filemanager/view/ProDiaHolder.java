package com.ider.filemanager.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;




/**
 * Created by Eric on 2017/9/9.
 */

public class ProDiaHolder {

    private static ProgressDialog progressDialog;
    public static void showProgressDialog(Context context,String title,String message,int max,DialogInterface.OnClickListener listener){
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setTitle(title);
        progressDialog.setMax(max);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", listener);
        progressDialog.setMessage(message);
        progressDialog.show();
    }
    public static void increment(){
        progressDialog.incrementProgressBy(1);
    }
    public static void setMessage(String message){
        progressDialog.setMessage(message);
    }
    public static void close(){
        progressDialog.dismiss();
    }

}
