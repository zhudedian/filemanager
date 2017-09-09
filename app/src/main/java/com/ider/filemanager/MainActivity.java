package com.ider.filemanager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ider.filemanager.db.MFile;
import com.ider.filemanager.db.MyData;
import com.ider.filemanager.util.FileCopy;
import com.ider.filemanager.util.FilePaste;
import com.ider.filemanager.view.PopuHolder;
import com.ider.filemanager.util.ScanFile;
import com.ider.filemanager.view.FileAdapter;
import com.ider.filemanager.view.ProDiaHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<MFile> mFiles = new ArrayList<>();
    private List<MFile> selectFiles = new ArrayList<>();
    private File selectFile= Environment.getExternalStorageDirectory();
    private ListView listView;
    private TextView filePath;
    private TextView delete,copy,paste,cancel;
    private TextView createDir;
    private LinearLayout allLinear;
    private RelativeLayout menuRelative;
    private CheckBox allCheck;
    private FileAdapter adapter;
    private boolean isCopyMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filePath = (TextView)findViewById(R.id.file_path);
        listView = (ListView)findViewById(R.id.list_view);
        allCheck = (CheckBox)findViewById(R.id.all_select);
        allLinear = (LinearLayout)findViewById(R.id.all_select_linear);
        menuRelative = (RelativeLayout) findViewById(R.id.menu_relative);
        createDir = (TextView)findViewById(R.id.create_dir);
        delete = (TextView)findViewById(R.id.delete);
        copy = (TextView)findViewById(R.id.copy);
        paste = (TextView)findViewById(R.id.paste);
        cancel = (TextView)findViewById(R.id.cancel);
        adapter = new FileAdapter(MainActivity.this,R.layout.file_list_item,mFiles,selectFiles);
        listView.setAdapter(adapter);


        setListener();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            init();
        }


    }
    private void init(){
        mFiles.clear();
        filePath.setText(selectFile.getPath());
        mFiles.addAll(ScanFile.scanDir(selectFile));
        adapter.notifyDataSetChanged();
    }

    private void initView(){
        if (selectFiles.size()>0){
            menuRelative.setVisibility(View.VISIBLE);
        }else {
            menuRelative.setVisibility(View.GONE);
        }
        if (MyData.isShowCheck) {
            if (mFiles.size() > 0) {
                allLinear.setVisibility(View.VISIBLE);
            }else {
                allLinear.setVisibility(View.GONE);
                allCheck.setChecked(false);
                MyData.isShowCheck = false;
            }
        }
        if (isCopyMode){
            allLinear.setVisibility(View.GONE);
            copy.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            paste.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        }else {
            copy.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            paste.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }
    private void setListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MFile mFile = mFiles.get(position);
                if (!MyData.isShowCheck) {
                    if (mFile.getFileType() == 1) {
                        selectFile = new File(mFile.getFilePath());
                        openDir();
                    }
                }else {
                    if (selectFiles.contains(mFile)){
                        selectFiles.remove(mFile);
                    }else {
                        selectFiles.add(mFile);
                    }
                    initView();
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MyData.isShowCheck = true;
                allLinear.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        allLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!allCheck.isChecked()){
                    allCheck.setChecked(true);
                    selectFiles.addAll(mFiles);
                }else {
                    allCheck.setChecked(false);
                    selectFiles.clear();
                }
                initView();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopuHolder.showConfirmDialog(MainActivity.this,listView,selectFiles,mHandler);
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCopyMode = true;
                allCheck.setChecked(false);
                MyData.isShowCheck = false;
                initView();
            }
        });
        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MFile> overs = new ArrayList<MFile>();
                final List<MFile> pastes = FilePaste.getPasteFiles(selectFiles,selectFile.getPath(),overs);
                isCopyMode =false;
                if (overs.size()>0){
                    PopuHolder.showOverWriteDialog(MainActivity.this,listView,pastes,overs,mHandler);
                }else {
                    new Thread() {
                        @Override
                        public void run() {
                            FileCopy.copy(pastes,mHandler);
                        }
                    }.start();
                    ProDiaHolder.showProgressDialog(MainActivity.this, "请稍后……", pastes.get(0).getFileName(), pastes.size(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileCopy.startCopy = false;
                            ProDiaHolder.close();
                        }
                    });
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCopyMode = false;
                selectFiles.clear();
                initView();
            }
        });
        createDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopuHolder.showCreateDirDialog(MainActivity.this,listView,selectFile.getPath(),mHandler);
            }
        });
    }
    private void openDir(){
        filePath.setText(selectFile.getPath());
        mFiles.clear();
        mFiles.addAll(ScanFile.scanDir(selectFile));
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
    }

    @Override
    public void onBackPressed() {
        if (!MyData.isShowCheck) {
            if (selectFile.getPath().equals(File.separator)) {
                finish();
            } else {
                selectFile = selectFile.getParentFile();
                openDir();
            }
        }else {
            MyData.isShowCheck = false;
            allLinear.setVisibility(View.GONE);
            allCheck.setChecked(false);
            selectFiles.clear();
        }
        initView();
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 0:
                    ProDiaHolder.increment();
                    Bundle bundle = msg.getData();
                    ProDiaHolder.setMessage(bundle.getString("message"));
                    break;
                case 1:
                    selectFiles.clear();
                    isCopyMode = false;
                    ProDiaHolder.close();
                    init();
                    initView();
                    break;
                case 2:
                    selectFiles.clear();
                    init();
                    initView();
                    break;
                case 3:

                    break;

            }
        }
    };
        @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length>0&& grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    init();
                }else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;

            default:
        }
    }
}
