package com.shoppi9driver.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shoppi9driver.app.Activity.OrderDetailActivity;
import com.shoppi9driver.app.R;
import com.shoppi9driver.app.model.OrderHistoryModel;
import com.shoppi9driver.app.model.ProductOrderDetailModel;

import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.MyViewHolder> {

    private List<ProductOrderDetailModel> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_txt,nameTitle_txt,qty_txt,price_txt;
        CardView relative_layout;
        ImageView icon;

        public MyViewHolder(View view) {

            super(view);
            icon = (ImageView) view.findViewById(R.id.icon);
       //     name_txt = (TextView) view.findViewById(R.id.name_txt);
            nameTitle_txt = (TextView) view.findViewById(R.id.nameTitle_txt);
            qty_txt = (TextView) view.findViewById(R.id.qty_txt);
            price_txt = (TextView) view.findViewById(R.id.price_txt);
        }
    }

    public ProductRecyclerAdapter(List<ProductOrderDetailModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemorderdetail_product_layout, parent, false);

        context = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ProductOrderDetailModel mList = modelList.get(position);

        holder.nameTitle_txt.setText(mList.getTitle());
//        holder.planId_txt.setText(mList.getMem_plan_auto_id());
//        holder.rupee_txt.setText(mList.getPay_amount_week());
//        holder.date_txt.setText(mList.getNext_payout_date());
//        holder.upcoming_txt.setText(mList.getPay_status());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


}
