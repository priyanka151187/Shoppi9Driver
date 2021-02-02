package com.shoppi9driver.app.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.shoppi9driver.app.api.APIClient;
import com.shoppi9driver.app.api.APIInterface;
import com.shoppi9driver.app.app.AppController;
import com.shoppi9driver.app.model.NewOrderModel;
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

public class NewOrderFragment extends Fragment {

    TextView mapLocation_txt, link_deliveryaddress_txt, viewOrderdetail_txt, tv_order_id, tv_order_date, store_name,
            tv_store_address, distance_txt, deliverTo_txt, deliveryAddress_txt, nodata_txt;
    static SharedPreferences prefs;
    String headerToken, header;
    APIInterface apiInterface;
    Singleton singletonObj = Singleton.getInstance();
    String latitude, longitude;
    Button accept_btn, reject_btn;
    String orderId, shop_name, shop_lat, shop_long, shopDescription, shopPhone1, shopEmail1, shopAddress_line, shopFull_address,
            cust_name, cust_phone, cust_address, cust_latitude, cust_longitude, cust_note;
    double distance;
    CardView card_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.item_currentorder_layout, container, false);


        mapLocation_txt = view.findViewById(R.id.mapLocation_txt);
        link_deliveryaddress_txt = view.findViewById(R.id.link_deliveryaddress_txt);
        viewOrderdetail_txt = view.findViewById(R.id.viewOrderdetail_txt);
        accept_btn = view.findViewById(R.id.accept_btn);
        reject_btn = view.findViewById(R.id.reject_btn);

        card_view = view.findViewById(R.id.card_view);
        nodata_txt = view.findViewById(R.id.nodata_txt);
        tv_order_id = view.findViewById(R.id.tv_order_id);
        tv_order_date = view.findViewById(R.id.tv_order_date);
        store_name = view.findViewById(R.id.store_name);
        tv_store_address = view.findViewById(R.id.tv_store_address);
        distance_txt = view.findViewById(R.id.distance_txt);
        deliverTo_txt = view.findViewById(R.id.deliverTo_txt);
        deliveryAddress_txt = view.findViewById(R.id.deliveryAddress_txt);

        mapLocation_txt.setPaintFlags(mapLocation_txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        link_deliveryaddress_txt.setPaintFlags(link_deliveryaddress_txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        viewOrderdetail_txt.setPaintFlags(viewOrderdetail_txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        prefs = getActivity().getSharedPreferences("APP_SHARED", MODE_PRIVATE);
//        user_id = prefs.getString("userId", null);
        headerToken = prefs.getString("header_token", null);
        header = "Bearer " + headerToken;

        onClick();

        callToNewOrder_ws();
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

        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_Accept_ws();
            }
        });

        reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_Reject_ws();
            }
        });

        link_deliveryaddress_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + cust_latitude + "," + cust_longitude));
                startActivity(intent);
            }
        });
        mapLocation_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + shop_lat + "," + shop_long));
                startActivity(intent);
            }
        });
    }

    private void callToNewOrder_ws() {
        String url = Config.TODAYSORDER_URL;

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

    private void setData() {
        tv_order_id.setText(orderId);
        //  tv_order_date.setText();
        store_name.setText(shop_name);
        tv_store_address.setText(shopAddress_line);
        distance_txt.setText("" + distance);
        deliverTo_txt.setText(cust_name);
        deliveryAddress_txt.setText(cust_address);
    }

    //call catogry Api
    private void call_Accept_ws() {
        String wsName = "accept";

        Map<String, String> params = new HashMap<String, String>();
        params.put("order_id", "20");

        int checkingVal = 1;
        callTo_wsMethod(wsName, params, checkingVal);
    }

    //call catogry Api
    private void call_Reject_ws() {
        String wsName = "decline";

        Map<String, String> params = new HashMap<String, String>();
        params.put("order_id", "1");

        int checkingVal = 2;
        callTo_wsMethod(wsName, params, checkingVal);
    }

    //generic method for ws
    private void callTo_wsMethod(String wsName, final Map<String, String> req_params, final int checkingVal) {

        //call web service
        String url = Config.BASE_URL + wsName;

        final ProgressDialog pDialog = singletonObj.createProgressDialog(getActivity());
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response of activation ", response.toString());
                        pDialog.hide();

                        if (checkingVal == 1) {
                            JSONObject jsnobject = null;
                            try {
                                jsnobject = new JSONObject(response);
                                String success = jsnobject.getString("success");
                                String message = jsnobject.getString("message");
                                if (success.equalsIgnoreCase("1")) {
                                    Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if (checkingVal == 2) {
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params = req_params;
                return params;
            }

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
    private void getDistance() {

        Location locationA = new Location("Shop_address");
        locationA.setLatitude(Double.parseDouble(shop_lat));
        locationA.setLongitude(Double.parseDouble(shop_long));
        Location locationB = new Location("Cust_address");
        locationB.setLatitude(Double.parseDouble(cust_latitude));
        locationB.setLongitude(Double.parseDouble(cust_longitude));

        distance = locationA.distanceTo(locationB);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
