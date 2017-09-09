package com.ider.filemanager.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.filemanager.R;
import com.ider.filemanager.db.MFile;
import com.ider.filemanager.db.MyData;

import java.util.List;

/**
 * Created by Eric on 2017/8/29.
 */

public class FileAdapter extends ArrayAdapter<MFile> {

    private int resourceId;
    private List<MFile> selectFiles;
    public FileAdapter(Context context, int textViewResourceId, List<MFile> objects, List<MFile> selects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        selectFiles = selects;
    }
    @Override
    public View getView(int posetion, View convertView, ViewGroup parent){
        final MFile mFile = getItem(posetion);
        View view;
        FileAdapter.ViewHolder viewHolder;
        if (convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder =new FileAdapter.ViewHolder();
            viewHolder.name = (TextView)view.findViewById(R.id.file_name);
            viewHolder.size = (TextView)view.findViewById(R.id.file_size);
            viewHolder.checkBox = (CheckBox)view.findViewById(R.id.checkbox);
            viewHolder.draw = (ImageView)view.findViewById(R.id.file_image);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (FileAdapter.ViewHolder) view.getTag();
        }
        viewHolder.name.setText(mFile.getFileName());
        viewHolder.size.setText(mFile.getFileSize());
        if (mFile.getFileType()==1){
            viewHolder.size.setVisibility(View.GONE);
            viewHolder.draw.setImageResource(R.drawable.item_dir);
        }else {
            viewHolder.size.setVisibility(View.VISIBLE);
            if (mFile.getFileType() == 2) {
                viewHolder.draw.setImageResource(R.drawable.item_video);
            } else if (mFile.getFileType() == 3) {
                viewHolder.draw.setImageResource(R.drawable.item_music);
            } else if (mFile.getFileType() == 4) {
                viewHolder.draw.setImageResource(R.drawable.item_photo);
            } else if (mFile.getFileType() == 5) {
                viewHolder.draw.setImageResource(R.drawable.item_apk);
            } else if (mFile.getFileType() == 6) {
                viewHolder.draw.setImageResource(R.drawable.item_zip);
            } else if (mFile.getFileType() == 7) {
                viewHolder.draw.setImageResource(R.drawable.item_pdf);
            } else if (mFile.getFileType() == 8) {
                viewHolder.draw.setImageResource(R.drawable.item_file);
            } else {
                viewHolder.draw.setImageResource(R.drawable.item_default);
            }
        }
        if (MyData.isShowCheck){
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            if (selectFiles.contains(mFile)){
                viewHolder.checkBox.setChecked(true);
            }else {
                viewHolder.checkBox.setChecked(false);
            }
        }else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
        return view;
    }
    class ViewHolder{
        ImageView draw;
        TextView name,size;
        CheckBox checkBox;
    }
}
