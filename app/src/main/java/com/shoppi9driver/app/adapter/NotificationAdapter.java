package com.shoppi9driver.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shoppi9driver.app.Activity.OrderDetailActivity;
import com.shoppi9driver.app.R;
import com.shoppi9driver.app.model.NotificationModel;
import com.shoppi9driver.app.model.OrderHistoryModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<NotificationModel> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Title_txt;
        Button viewdetail_btn;

        public MyViewHolder(View view) {

            super(view);
            Title_txt = (TextView) view.findViewById(R.id.Title_txt);

        }
    }

    public NotificationAdapter(List<NotificationModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_layout, parent, false);

        context = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        NotificationModel mList = modelList.get(position);

        holder.Title_txt.setText(mList.getTitle());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


}
