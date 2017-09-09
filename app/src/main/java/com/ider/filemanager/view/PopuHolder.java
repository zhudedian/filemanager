package com.ider.filemanager.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ider.filemanager.MainActivity;
import com.ider.filemanager.R;
import com.ider.filemanager.db.MFile;
import com.ider.filemanager.db.MyData;
import com.ider.filemanager.popu.PopuUtils;
import com.ider.filemanager.popu.PopupDialog;
import com.ider.filemanager.popu.Popus;
import com.ider.filemanager.util.FileCopy;
import com.ider.filemanager.util.FileDelete;

import java.io.File;
import java.util.List;

/**
 * Created by Eric on 2017/9/8.
 */

public class PopuHolder {

    public static void showCreateDirDialog(Context context,View parent,final String path,final Handler handler) {
        View view = View.inflate(context, R.layout.create_dir, null);
        Popus popup = new Popus(-1,-1,true,R.style.PopupWindowAnimation,view,R.layout.activity_main);
        PopupDialog popupDialog = PopuUtils.createPopupDialog(context, popup);
        popupDialog.showAtLocation(parent, Gravity.CENTER, 0, 0);
        final EditText dirName = (EditText) view.findViewById(R.id.dir_name);
        Button cancel = (Button)view.findViewById(R.id.cancel_action);
        Button ok = (Button)view.findViewById(R.id.ok_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopuUtils.dismissPopupDialog();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopuUtils.dismissPopupDialog();
                String dir = dirName.getText().toString();
                String newPath = path.endsWith(File.separator)?path+dir:path+File.separator+dir;
                File file = new File(newPath);
                file.mkdirs();
                handler.sendEmptyMessage(2);
            }
        });
    }
    public static void showConfirmDialog(Context context, View parent, final List<MFile> selects,final Handler handler) {

        View view = View.inflate(context, R.layout.confirm, null);
        Popus popup = new Popus(-1,-1,true,R.style.PopupWindowAnimation,view,R.layout.activity_main);
        PopupDialog popupDialog = PopuUtils.createPopupDialog(context, popup);
        popupDialog.showAtLocation(parent, Gravity.CENTER, 0, 0);
        TextView title = (TextView)view.findViewById(R.id.title);
        TextView fileName = (TextView)view.findViewById(R.id.file_name);
        Button cancel = (Button)view.findViewById(R.id.cancel_action);
        Button ok = (Button)view.findViewById(R.id.ok_action);
        title.setText("确认删除？");
        if (selects.size()==1){
            fileName.setText(selects.get(0).getFileName());
        }else {
            fileName.setText("已选择"+selects.size()+"文件");
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopuUtils.dismissPopupDialog();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopuUtils.dismissPopupDialog();
                for (int i =0;i<selects.size();i++){
                    File file = new File(selects.get(i).getFilePath());
                    FileDelete.delete(file);
                }
                handler.sendEmptyMessage(2);
            }
        });
    }
    public static void showOverWriteDialog(final Context context, final View parent, final List<MFile> pastes, final List<MFile> overs, final Handler handler){

        View view = View.inflate(context, R.layout.confirm, null);
        Popus popup = new Popus(-1,-1,true,R.style.PopupWindowAnimation,view,R.layout.activity_main);
        PopupDialog popupDialog = PopuUtils.createPopupDialog(context, popup);
        popupDialog.showAtLocation(parent, Gravity.CENTER, 0, 0);
        TextView title = (TextView)view.findViewById(R.id.title);
        LinearLayout allSelect = (LinearLayout)view.findViewById(R.id.all_select);
        final CheckBox allcheck = (CheckBox)view.findViewById(R.id.all_select_check);
        TextView fileName = (TextView)view.findViewById(R.id.file_name);
        Button cancel = (Button)view.findViewById(R.id.cancel_action);
        Button ok = (Button)view.findViewById(R.id.ok_action);
        if (overs.size()==1){
            allSelect.setVisibility(View.GONE);
        }else {
            allSelect.setVisibility(View.VISIBLE);
        }
        title.setText("该文件已存在！");
        fileName.setText(overs.get(0).getFileName());
        cancel.setText("跳过");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allcheck.isChecked()){
                    overs.clear();
                    PopuUtils.dismissPopupDialog();
                    ProDiaHolder.showProgressDialog(context, "请稍后……", pastes.get(0).getFileName(), pastes.size(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileCopy.startCopy = false;
                            ProDiaHolder.close();
                        }
                    });
                    new Thread() {
                        @Override
                        public void run() {
                            FileCopy.copy(pastes,handler);
                        }
                    }.start();

                }else {
                    overs.remove(0);
                    PopuUtils.dismissPopupDialog();
                    if (overs.size() > 0) {
                        showOverWriteDialog(context,parent,pastes,overs,handler);
                    } else {
                        ProDiaHolder.showProgressDialog(context, "请稍后……", pastes.get(0).getFileName(), pastes.size(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FileCopy.startCopy = false;
                                ProDiaHolder.close();
                            }
                        });
                        new Thread() {
                            @Override
                            public void run() {
                                FileCopy.copy(pastes,handler);
                            }
                        }.start();
                    }
                }
            }
        });
        ok.setText("覆盖");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allcheck.isChecked()){
                    pastes.addAll(overs);
                    overs.clear();
                    PopuUtils.dismissPopupDialog();
                    ProDiaHolder.showProgressDialog(context, "请稍后……", pastes.get(0).getFileName(), pastes.size(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileCopy.startCopy = false;
                            ProDiaHolder.close();
                        }
                    });
                    new Thread() {
                        @Override
                        public void run() {
                            FileCopy.copy(pastes,handler);
                        }
                    }.start();
                }else {
                    pastes.add(overs.get(0));
                    overs.remove(0);
                    PopuUtils.dismissPopupDialog();
                    if (overs.size() > 0) {
                        showOverWriteDialog(context,parent,pastes,overs,handler);
                    } else {
                        ProDiaHolder.showProgressDialog(context, "请稍后……", pastes.get(0).getFileName(), pastes.size(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FileCopy.startCopy = false;
                                ProDiaHolder.close();
                            }
                        });
                        new Thread() {
                            @Override
                            public void run() {
                                FileCopy.copy(pastes,handler);
                            }
                        }.start();
                    }
                }

            }
        });
        allcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
