package com.shoppi9driver.app.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shoppi9driver.app.R;
import com.shoppi9driver.app.adapter.OrderHistoryAdapter;

import com.shoppi9driver.app.model.OrderHistoryModel;

import java.util.ArrayList;
import java.util.Calendar;

public class EarningFragment extends Fragment {

    private static String TAG = "EarningFragment";
    FloatingActionButton fab;
    LinearLayout totalorder_layout;
    OrderHistoryAdapter orderHistoryAdapter;
    ArrayList<OrderHistoryModel> orderhistory_list = new ArrayList<OrderHistoryModel>();
    private DatePickerDialog datePickerDialog;
    private int year;
    private int month;
    private int dayOfMonth;
    private Calendar calendar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.earning_fragment, container, false);

        totalorder_layout = view.findViewById(R.id.totalorder_layout);
        fab = view.findViewById(R.id.fab);

        onClick();
        return view;
    }

    private void onClick() {

        totalorder_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.fragment.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new Order_HistoryFragment(), "Order_HistoryFragment").commit();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp();
            }
        });

    }


    public void showPopUp() {
        final Dialog dialog = new Dialog(getActivity(),R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.filter_popup);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText searchOrder_editTxt = (EditText) dialog.findViewById(R.id.searchOrder_editTxt);
        Button cancel_btn = (Button)dialog.findViewById(R.id.cancel_btn);
        CardView startdateCard = (CardView)dialog.findViewById(R.id.startdateCard);
        TextView startdateTv = (TextView)dialog.findViewById(R.id.startdateTv);
        CardView endDateCard = (CardView)dialog.findViewById(R.id.endDateCard);
        TextView endDateTv = (TextView)dialog.findViewById(R.id.endDateTv);

        startdateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalender(startdateTv);
            }
        });

        endDateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalender(endDateTv);
            }
        });


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void openCalender(TextView textView){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, final int year, final int month, final int day) {
                        //    textView.setText(year + "-" + (month + 1) + "-" + day);
                        textView.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
}
