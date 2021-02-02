package com.shoppi9driver.app.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shoppi9driver.app.R;
import com.shoppi9driver.app.adapter.OrderHistoryAdapter;

import com.shoppi9driver.app.app.AppController;
import com.shoppi9driver.app.model.OrderHistoryModel;
import com.shoppi9driver.app.utility.Config;
import com.shoppi9driver.app.utility.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Order_HistoryFragment extends Fragment {

    private static String TAG = "Order_HistoryFragment";
    RecyclerView rv_myorder;
    TextView nodata_txt;
    OrderHistoryAdapter orderHistoryAdapter;
    ArrayList<OrderHistoryModel> orderhistory_list = new ArrayList<OrderHistoryModel>();
    FloatingActionButton fab;
    private DatePickerDialog datePickerDialog;
    private int year;
    private int month;
    private int dayOfMonth;
    private Calendar calendar;
    Singleton singletonObj = Singleton.getInstance();
    static SharedPreferences prefs;
    String headerToken,header;
    String order_id,created_at,totalAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.orderhistory_fragment, container, false);

        rv_myorder = view.findViewById(R.id.rv_myorder);
        nodata_txt = view.findViewById(R.id.nodata_txt);
        fab = view.findViewById(R.id.fab);

        prefs = getActivity().getSharedPreferences("APP_SHARED", MODE_PRIVATE);
//        user_id = prefs.getString("userId", null);
        headerToken = prefs.getString("header_token", null);
        header = "Bearer " + headerToken;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp();
            }
        });
        if (isNetworkAvailable()) {
            callToAllOrder_ws();
        } else {
            //singletonObj.networkErrorDialog(LoginActivity.this);
        }

        return view;

    }

    private void callToAllOrder_ws() {
        String url = Config.ALLORDER_URL;

        final ProgressDialog pDialog = singletonObj.createProgressDialog(getActivity());
        pDialog.setCanceledOnTouchOutside(true);
        pDialog.show();
        StringRequest jsonObjRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response is", response.toString());
                        pDialog.hide();
                        try {
                            JSONObject jsnobject = new JSONObject(response);
                            JSONArray jsonArray = jsnobject.getJSONArray("data");
                            String success = jsnobject.getString("success");
                            System.out.println("jsonArray :" + jsonArray);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObj = jsonArray.getJSONObject(i);

                                order_id = jObj.getString("order_id");
                                created_at = jObj.getString("created_at");

                                JSONObject jsonObjectshop = jObj.getJSONObject("orders");
                                totalAmount = jsonObjectshop.getString("total");
                                 OrderHistoryModel orderHistoryModel = new OrderHistoryModel();
                                 orderHistoryModel.setOrder_id(order_id);
                                 orderHistoryModel.setCreated_at(created_at);
                                 orderHistoryModel.setTotal(totalAmount);
                                 orderhistory_list.add(orderHistoryModel);
                            }
                            setdataInList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
                pDialog.hide();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", header);
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjRequest);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
    public void setdataInList() {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            rv_myorder.setLayoutManager(linearLayoutManager);
            orderHistoryAdapter = new OrderHistoryAdapter(orderhistory_list);
            rv_myorder.setAdapter(orderHistoryAdapter);
        }
    }
