package com.shoppi9driver.app.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.location.Location;
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
import com.shoppi9driver.app.R;
import com.shoppi9driver.app.adapter.ViewPagerAdapter;
import com.shoppi9driver.app.app.AppController;
import com.shoppi9driver.app.utility.Config;
import com.shoppi9driver.app.utility.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class DoneFragment extends Fragment {

    TextView viewDetail_txt;
    TextView tv_order_id,tv_order_date,tv_totalitem,tv_paymentMode,store_name,tv_shopadress,distance_txt,deliverTo_txt,
            tv_deliveryAddress,nodata_txt;
    String orderId,shop_name,shop_lat,shop_long,shopDescription,shopPhone1,shopEmail1,shopAddress_line,shopFull_address,
            cust_name,cust_phone,cust_address,cust_latitude,cust_longitude,cust_note;
    double distance;
    CardView card_view;
    Singleton singletonObj = Singleton.getInstance();
    static SharedPreferences prefs;
    String headerToken, header;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.donefragment, container, false);

        viewDetail_txt = view.findViewById(R.id.viewDetail_txt);

        card_view = view.findViewById(R.id.card_view);
        nodata_txt = view.findViewById(R.id.nodata_txt);
        tv_order_id = view.findViewById(R.id.tv_order_id);
        tv_order_date = view.findViewById(R.id.tv_order_date);
        tv_totalitem = view.findViewById(R.id.tv_totalitem);
        tv_paymentMode = view.findViewById(R.id.tv_paymentMode);
        store_name = view.findViewById(R.id.store_name);
        tv_shopadress = view.findViewById(R.id.tv_shopadress);
        distance_txt = view.findViewById(R.id.distance_txt);
        deliverTo_txt = view.findViewById(R.id.deliverTo_txt);
        tv_deliveryAddress = view.findViewById(R.id.tv_deliveryAddress);

        viewDetail_txt.setPaintFlags(viewDetail_txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        prefs = getActivity().getSharedPreferences("APP_SHARED", MODE_PRIVATE);
//        user_id = prefs.getString("userId", null);
        headerToken = prefs.getString("header_token", null);
        header = "Bearer " + headerToken;

        callToCompletedOrder_ws();
        return view;
    }

    private void callToCompletedOrder_ws() {
        String url = Config.CompleteORDER_URL;

        final ProgressDialog pDialog = singletonObj.createProgressDialog(getActivity());
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, url,
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", "1");
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
//        tv_order_date.setText();
//        tv_totalitem.setText();
//        tv_paymentMode.setText();
        store_name.setText(shop_name);
        tv_shopadress.setText(shopAddress_line);
        distance_txt.setText("" + distance);
        deliverTo_txt.setText(cust_name);
        tv_deliveryAddress.setText(cust_address);
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
}
