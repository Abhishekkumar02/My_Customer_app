package com.example.pc.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pc on 12/13/2019.
 */

public class CustomerListView extends RecyclerView.Adapter<CustomerListView.ViewHolder> {
    Context context;
    List<CustomerDetails> customerList;
    public CustomerListView(Context context,List<CustomerDetails>customerList){
        this.context = context;
        this.customerList = customerList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CustomerDetails customerDetails =  customerList.get(position);
        holder.lblName.setText(customerDetails.getName());
        holder.lblId.setText(customerDetails.getId());
        holder.lblPhone.setText(customerDetails.getPhone());
        holder.lblPhone2.setText(customerDetails.getPhone2());
        holder.lblCity.setText(customerDetails.getCity());
        holder.lblAddress.setText(customerDetails.getAddress());
        holder.lblDevice.setText(customerDetails.getDevice());
        holder.lblAge.setText(customerDetails.getAge());
        holder.lblDate.setText(customerDetails.getDate());
        holder.lblNotes.setText(customerDetails.getNotes());
        holder.lblPrice.setText(customerDetails.getPrice());
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lblName,lblId,lblPhone,lblPhone2,lblCity,lblAddress,lblDevice,lblAge,lblDate,lblNotes,lblPrice;
        public ViewHolder(View itemView) {
            super(itemView);
            lblName = (TextView)itemView.findViewById(R.id.lblName);
            lblId = (TextView)itemView.findViewById(R.id.lblId);
            lblPhone = (TextView)itemView.findViewById(R.id.lblPhone);
            lblPhone2 = (TextView)itemView.findViewById(R.id.lblPhone2);
            lblCity = (TextView)itemView.findViewById(R.id.lblCity);
            lblAddress = (TextView)itemView.findViewById(R.id.lblAddress);
            lblDevice = (TextView)itemView.findViewById(R.id.lblDevice);
            lblAge = (TextView)itemView.findViewById(R.id.lblAge);
            lblDate = (TextView)itemView.findViewById(R.id.lblDate);
            lblNotes = (TextView)itemView.findViewById(R.id.lblNotes);
            lblPrice = (TextView)itemView.findViewById(R.id.lblPrice);
        }
    }
}
