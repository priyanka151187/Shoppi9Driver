package com.shoppi9driver.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shoppi9driver.app.Activity.OrderDetailActivity;
import com.shoppi9driver.app.R;
import com.shoppi9driver.app.model.OrderHistoryModel;
import com.shoppi9driver.app.utility.MyUtils;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private List<OrderHistoryModel> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_order_id,tv_order_date,totalAmount_txt,tv_house,store_name,tv_order_item,viewDetail_txt;
        Button viewdetail_btn;

        public MyViewHolder(View view) {

            super(view);
            tv_order_id = (TextView) view.findViewById(R.id.tv_order_id);
            tv_order_date = (TextView) view.findViewById(R.id.tv_order_date);
            totalAmount_txt = (TextView) view.findViewById(R.id.totalAmount_txt);
            viewDetail_txt = (TextView) view.findViewById(R.id.viewDetail_txt);
        }
    }

    public OrderHistoryAdapter(List<OrderHistoryModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_layout, parent, false);

        context = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        OrderHistoryModel mList = modelList.get(position);

        holder.tv_order_id.setText(mList.getOrder_id());
        holder.tv_order_date.setText(mList.getCreated_at());
        holder.totalAmount_txt.setText(mList.getTotal());
     //   holder.tv_order_date.setText(MyUtils.converDateTime(mList.getCreated_at()));

        holder.viewDetail_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                context.startActivity(intent);
            }
        });
        holder.viewDetail_txt.setPaintFlags(holder.viewDetail_txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


}
