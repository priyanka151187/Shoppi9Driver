package com.shoppi9driver.app.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.shoppi9driver.app.Activity.ChangePasswordActivity;
import com.shoppi9driver.app.R;
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


public class MySettingFragment extends Fragment {

    static SharedPreferences prefs;
    String headerToken,user_id;
    Singleton singletonObj = Singleton.getInstance();
    EditText number_editTxt, alternatenumber_editText, city_editTxt, address_editTxt;
    String name, email, avatar;
    APIInterface apiInterface;
    String header;
    TextView update_btn,changepass_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        number_editTxt = view.findViewById(R.id.number_editTxt);
        alternatenumber_editText = view.findViewById(R.id.alternatenumber_editText);
        update_btn = view.findViewById(R.id.update_btn);
        address_editTxt = view.findViewById(R.id.address_editTxt);
        changepass_txt = view.findViewById(R.id.changepass_txt);


        prefs = getActivity().getSharedPreferences("APP_SHARED", MODE_PRIVATE);
        user_id = prefs.getString("userId", null);
        headerToken = prefs.getString("header_token", null);
        header = "Bearer " + headerToken;

        changepass_txt.setPaintFlags(changepass_txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        changepass_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation();
            }
        });
        call_GetSettings_ws();
        return view;
    }

    private void Validation() {
        String Number = number_editTxt.getText().toString();
        String AlternateNum = alternatenumber_editText.getText().toString();

        number_editTxt.setError(null);
        alternatenumber_editText.setError(null);

        if (Number.equalsIgnoreCase("")) {
            number_editTxt.requestFocus();
            number_editTxt.setError("Please enter Number");

        } else if (AlternateNum.equalsIgnoreCase("")) {
            alternatenumber_editText.requestFocus();
            alternatenumber_editText.setError("Please enter Alternate Number");

        } else {
            // hide the soft keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(alternatenumber_editText.getWindowToken(), 0);

            //Checking the connection
            if (isNetworkAvailable()) {
                call_UpdateSettings_ws();
            } else {
                //singletonObj.networkErrorDialog(LoginActivity.this);
            }
        }
    }

    //call catogry Api
    private void call_UpdateSettings_ws() {

        String url = Config.SETTING_URL;

        Map<String, String> params = new HashMap<String, String>();
        params.put("phone1", number_editTxt.getText().toString());
        params.put("phone2", alternatenumber_editText.getText().toString());

        int checkingVal = 1;
        callTo_wsMethod(url, params, checkingVal);
    }

    //generic method for ws
    private void callTo_wsMethod(String url, final Map<String, String> req_params, final int checkingVal) {

        final ProgressDialog pDialog = singletonObj.createProgressDialog(getActivity());
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response of activation ", response.toString());
                        pDialog.hide();
                        // Convert String to json object
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            String message = json.getString("message");
                            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(getActivity(),"" + error,Toast.LENGTH_LONG).show();
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


    private void call_GetSettings_ws() {
        String url = Config.SETTING_URL;
        final ProgressDialog pDialog = singletonObj.createProgressDialog(getActivity());
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        StringRequest jsonObjRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response is", response.toString());
                        pDialog.hide();

                        try {
                            JSONObject json = new JSONObject(response.toString());
                            String phone1 = json.getString("phone1");
                            String phone2 = json.getString("phone2");
                            String full_address = json.getString("full_address");

                            setdataInList(phone1,phone2,full_address);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(getActivity(),"" + error,Toast.LENGTH_LONG).show();
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

    private void setdataInList(String phone1, String phone2, String full_address) {

        alternatenumber_editText.setText(phone1);
        number_editTxt.setText(phone2);
        address_editTxt.setText(full_address);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
