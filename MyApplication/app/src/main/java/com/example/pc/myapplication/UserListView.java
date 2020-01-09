package com.example.pc.myapplication;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pc on 12/14/2019.
 */

public class UserListView extends ArrayAdapter {
    Activity context;
    List<AddUserRead> userReadList;
    public UserListView(Activity context,List<AddUserRead> userReadList) {
        super(context,R.layout.add_user_layout,userReadList);
        this.context = context;
        this.userReadList = userReadList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AddUserRead read= userReadList.get(position);
        LayoutInflater inflater =context.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_user_layout,null,false);
        TextView tv1 = (TextView) view.findViewById(R.id.lbladduser);
        TextView tv2 = (TextView) view.findViewById(R.id.lbladdemail);
        tv1.setText(read.getUsername());
        tv2.setText(read.getEmail());
        return view;
    }


}
