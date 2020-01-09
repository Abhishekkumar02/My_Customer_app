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

public class ClientListView extends ArrayAdapter {
    Activity context;
    List<ClientDetails> clientDetailsList;
    public ClientListView(Activity context,List<ClientDetails> userReadList) {
        super(context,R.layout.add_client_layout,userReadList);
        this.context = context;
        this.clientDetailsList = userReadList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ClientDetails read= clientDetailsList.get(position);
        LayoutInflater inflater =context.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_client_layout,null,false);
        TextView tv1 = (TextView) view.findViewById(R.id.lblviewclient);
        tv1.setText(read.getClientnames());
        return view;
    }


}
