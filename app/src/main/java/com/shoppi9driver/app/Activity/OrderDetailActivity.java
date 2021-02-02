package com.shoppi9driver.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.shoppi9driver.app.R;
import com.shoppi9driver.app.adapter.OrderHistoryAdapter;
import com.shoppi9driver.app.adapter.ProductRecyclerAdapter;
import com.shoppi9driver.app.app.AppController;
import com.shoppi9driver.app.model.OrderHistoryModel;
import com.shoppi9driver.app.model.ProductOrderDetailModel;
import com.shoppi9driver.app.utility.Config;
import com.shoppi9driver.app.utility.MyUtils;
import com.shoppi9driver.app.utility.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {

    ImageView back_image;
    Button btn_mark_delivered;
    RecyclerView product_recycler;
    ProductRecyclerAdapter productRecyclerAdapter;
    ArrayList<ProductOrderDetailModel> productlist = new ArrayList<ProductOrderDetailModel>();
    Singleton singletonObj = Singleton.getInstance();
    String order_id, amount, payment_method,custname,custaddress,full_address,paymentStatus,date;
    String header;
    static SharedPreferences prefs;
    String headerToken;
    TextView tv_order_no,tv_order_date,tv_recivername,tv_custAddress,store_name,
            store_address,tv_order_item,tv_order_price,payment_mode,payment_status_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetail_activity);

        back_image = (ImageView) findViewById(R.id.back_image);
        btn_mark_delivered = (Button) findViewById(R.id.btn_mark_delivered);
        product_recycler = (RecyclerView) findViewById(R.id.product_recycler);

        tv_order_no = (TextView) findViewById(R.id.tv_order_no);
        tv_order_date = (TextView) findViewById(R.id.tv_order_date);
        tv_recivername = (TextView) findViewById(R.id.tv_recivername);
        tv_custAddress = (TextView) findViewById(R.id.tv_custAddress);
        store_name = (TextView) findViewById(R.id.store_name);
        store_address = (TextView) findViewById(R.id.store_address);
        tv_order_item = (TextView) findViewById(R.id.tv_order_item);
        tv_order_price = (TextView) findViewById(R.id.tv_order_price);
        payment_mode = (TextView) findViewById(R.id.payment_mode);
        payment_status_txt = (TextView) findViewById(R.id.payment_status_txt);

        prefs = getSharedPreferences("APP_SHARED", MODE_PRIVATE);
        headerToken = prefs.getString("header_token", null);
        header = "Bearer " + headerToken;


        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_mark_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailActivity.this, GetSignature.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        callToOrderDetail_ws();
        //setdataInList();
    }

    private void callToOrderDetail_ws() {
        String url = Config.ORDERDETAIL_URL + "18";//order_id; //"18";//order_id;

        final ProgressDialog pDialog = singletonObj.createProgressDialog(this);
        pDialog.setCanceledOnTouchOutside(true);
        pDialog.show();
        StringRequest jsonObjRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response is", response.toString());
                        pDialog.hide();

                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                 order_id = jObj.getString("order_id");
                                 amount = jObj.getString("amount");
                                 payment_method = jObj.getString("payment_method");
                                 paymentStatus = jObj.getString("payment_status");

                                JSONObject custJsonObject = jObj.getJSONObject("customer_data");
                                 custname = custJsonObject.getString("name");
                                 custaddress = custJsonObject.getString("address");

                                JSONObject created_dateObject = jObj.getJSONObject("created_date");
                                date = created_dateObject.getString("date");

                                JSONObject vendorinfoObject = jObj.getJSONObject("vendorinfo");
                                JSONObject vendorpersonelInfoObject = vendorinfoObject.getJSONObject("info");
                                 full_address = vendorpersonelInfoObject.getString("full_address");
//                                String custaddress = venderpersonelInfoObject.getString("address");

                            }

                            setData(order_id,date,amount,payment_method,custname,custaddress,full_address,paymentStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

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

    private void setData(String order_id,String date, String amount, String payment_method, String custname, String custaddress, String full_address,String payment_status) {

        tv_order_no.setText(order_id);
        tv_order_date.setText(date);   //MyUtils.converDateTime(date)
        tv_recivername.setText(custname);
        tv_custAddress.setText(custaddress);
       // store_name.setText();
        store_address.setText(full_address);
      //  tv_order_item.setText();
        tv_order_price.setText(amount);
        payment_mode.setText(payment_method);
        payment_status_txt.setText(payment_status);
    }

//    public void setdataInList() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//        product_recycler.setLayoutManager(linearLayoutManager);
//        for (int i = 0; i < 6; i++) {
//            ProductOrderDetailModel productOrderDetailModel = new ProductOrderDetailModel();
//            productOrderDetailModel.setTitle("Apple Shimla");
//            productlist.add(productOrderDetailModel);
//        }
//        productRecyclerAdapter = new ProductRecyclerAdapter(productlist);
//        product_recycler.setAdapter(productRecyclerAdapter);
//    }
}
