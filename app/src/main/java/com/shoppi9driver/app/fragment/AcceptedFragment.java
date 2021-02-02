package com.shoppi9driver.app.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.shoppi9driver.app.Activity.OrderDetailActivity;
import com.shoppi9driver.app.R;
import com.shoppi9driver.app.adapter.ViewPagerAdapter;
import com.shoppi9driver.app.api.APIClient;
import com.shoppi9driver.app.api.APIInterface;
import com.shoppi9driver.app.app.AppController;
import com.shoppi9driver.app.utility.Config;
import com.shoppi9driver.app.utility.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class AcceptedFragment extends Fragment {

    TextView mapLocation_txt,viewOrderdetail_txt,deliveryLocation_txt,tv_order_no,tv_order_date,totalItems_txt,paymentmode_txt,store_name,
            tv_store_address,distance_txt,deliverNameTo_txt,deliveryAddress_txt,nodata_txt;
    static SharedPreferences prefs;
    String headerToken,header,order_id;
    APIInterface apiInterface;
    Singleton singletonObj = Singleton.getInstance();
    String latitude,longitude;
    CardView card_view;
    String orderId, shop_name, shop_lat, shop_long, shopDescription, shopPhone1, shopEmail1, shopAddress_line, shopFull_address,
            cust_name, cust_phone, cust_address, cust_latitude, cust_longitude, cust_note;
    double distance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.acceptfragment, container, false);

        mapLocation_txt = view.findViewById(R.id.mapLocation_txt);
        viewOrderdetail_txt = view.findViewById(R.id.viewOrderdetail_txt);
        deliveryLocation_txt = view.findViewById(R.id.deliveryLocation_txt);

        card_view = view.findViewById(R.id.card_view);
        nodata_txt = view.findViewById(R.id.nodata_txt);
        tv_order_no = view.findViewById(R.id.tv_order_no);
        tv_order_date = view.findViewById(R.id.tv_order_date);
        store_name = view.findViewById(R.id.store_name);
        tv_store_address = view.findViewById(R.id.tv_store_address);
        distance_txt = view.findViewById(R.id.distance_txt);
        deliverNameTo_txt = view.findViewById(R.id.deliverNameTo_txt);
        deliveryAddress_txt = view.findViewById(R.id.deliveryAddress_txt);
        totalItems_txt = view.findViewById(R.id.totalItems_txt);
        paymentmode_txt = view.findViewById(R.id.paymentmode_txt);


        mapLocation_txt.setPaintFlags(mapLocation_txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        viewOrderdetail_txt.setPaintFlags(viewOrderdetail_txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        deliveryLocation_txt.setPaintFlags(deliveryLocation_txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        prefs = getActivity().getSharedPreferences("APP_SHARED", MODE_PRIVATE);
//        user_id = prefs.getString("userId", null);
        headerToken = prefs.getString("header_token", null);
        header = "Bearer " + headerToken;

        onClick();
        callToAcceptOrder_ws();
        return view;
    }

    private void onClick() {

        viewOrderdetail_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        });

        mapLocation_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + shop_lat + "," + shop_long));
                startActivity(intent);
            }
        });

        deliveryLocation_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + cust_latitude + "," + cust_longitude));
                startActivity(intent);
            }
        });
    }

    public void callToAcceptOrder_ws() {
        String url = Config.ACCEPTEDORDER_URL;

        final ProgressDialog pDialog = singletonObj.createProgressDialog(getActivity());
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        StringRequest jsonObjRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response is", response.toString());
                        pDialog.hide();
                        parseProductResponse(response);
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

    private void parseProductResponse(String response) {
        try {
            JSONObject jsnobject = new JSONObject(response);
            String success = jsnobject.getString("success");
            if (success.equalsIgnoreCase("1")) {
                JSONArray jsonArray = jsnobject.getJSONArray("data");
                System.out.println("jsonArray :" + jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    orderId = jObj.getString("id");
                    String seen = jObj.getString("seen");
                    shop_name = jObj.getString("shop_name");
                    shop_lat = jObj.getString("shop_lat");
                    shop_long = jObj.getString("shop_long");

                    JSONObject jsonObjectshop = jObj.getJSONObject("shop_data");
                    shopDescription = jsonObjectshop.getString("description");
                    shopPhone1 = jsonObjectshop.getString("phone1");
                    shopEmail1 = jsonObjectshop.getString("email1");
                    shopAddress_line = jsonObjectshop.getString("address_line");
                    shopFull_address = jsonObjectshop.getString("full_address");

                    String total = jObj.getString("total");
                    String time = jObj.getString("time");

                    JSONObject jsonObjectcustomer = jObj.getJSONObject("customer_data");
                    cust_name = jsonObjectcustomer.getString("name");
                    cust_phone = jsonObjectcustomer.getString("phone");
                    cust_address = jsonObjectcustomer.getString("address");
                    cust_latitude = jsonObjectcustomer.getString("latitude");
                    cust_longitude = jsonObjectcustomer.getString("longitude");
                    cust_note = jsonObjectcustomer.getString("note");
                    String cust_payment_id = jsonObjectcustomer.getString("payment_id");
                }
                nodata_txt.setVisibility(View.GONE);
                card_view.setVisibility(View.VISIBLE);
                getDistance();
                setData();
            } else {
                card_view.setVisibility(View.GONE);
                nodata_txt.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDistance() {

        Location locationA = new Location("Shop_address");
        locationA.setLatitude(Double.parseDouble(shop_lat));
        locationA.setLongitude(Double.parseDouble(shop_long));
        Location locationB = new Location("Cust_address");
        locationB.setLatitude(Double.parseDouble(cust_latitude));
        locationB.setLongitude(Double.parseDouble(cust_longitude));

        distance = locationA.distanceTo(locationB);
    }


    private void setData() {
        tv_order_no.setText(orderId);
        //tv_order_date.setText();
        store_name.setText(shop_name);
        tv_store_address.setText(shopAddress_line);
        distance_txt.setText("" + distance);
        deliverNameTo_txt.setText(cust_name);
        deliveryAddress_txt.setText(cust_address);
    }
}
