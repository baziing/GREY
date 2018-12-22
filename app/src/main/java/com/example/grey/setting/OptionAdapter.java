package com.example.grey.setting;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.example.grey.R;

public class OptionAdapter extends ArrayAdapter<Option>{

    private int resourceId;

    public OptionAdapter(Context context, int textViewResourceId, List<Option>objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Option option=getItem(position);

        //优化效率
        View view;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        }else{
            view=convertView;
        }
//        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView optionImage=(ImageView)view.findViewById(R.id.option_image);
        TextView optionName=(TextView)view.findViewById(R.id.option_name);
        optionImage.setImageResource(option.getImageId());
        optionName.setText(option.getName());
        return view;
    }
}
