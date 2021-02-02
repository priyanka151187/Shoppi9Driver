package com.shoppi9driver.app.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.shoppi9driver.app.R;
import com.shoppi9driver.app.app.AppController;
import com.shoppi9driver.app.utility.Config;
import com.shoppi9driver.app.utility.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    ImageView back_image,currentPass_img,newPass_img,confirmPass_img;
    EditText password_editTxt,newPassword_editTxt,alternatePass_editTxt;
    String header;
    static SharedPreferences prefs;
    String headerToken;
    Button submit_btn;
    Singleton singletonObj = Singleton.getInstance();
    boolean isShowpassword = true;
    boolean isnewShowpassword = true;
    boolean isconfirmShowpassword = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword_activity);

        back_image = (ImageView)findViewById(R.id.back_image);
        password_editTxt = (EditText)findViewById(R.id.password_editTxt);
        newPassword_editTxt = (EditText)findViewById(R.id.newPassword_editTxt);
        alternatePass_editTxt = (EditText)findViewById(R.id.alternatePass_editTxt);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        currentPass_img = (ImageView) findViewById(R.id.currentPass_img);
        newPass_img = (ImageView) findViewById(R.id.newPass_img);
        confirmPass_img = (ImageView) findViewById(R.id.confirmPass_img);


        prefs = getSharedPreferences("APP_SHARED", MODE_PRIVATE);
        headerToken = prefs.getString("header_token", null);
        header = "Bearer " + headerToken;

        onClick();
    }

    private void onClick(){
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation();
            }
        });

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        currentPass_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowpassword == true) {
                    currentPass_img.setImageResource(R.drawable.ic_eye_visibility);
                    password_editTxt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isShowpassword = false;
                }else {
                    currentPass_img.setImageResource(R.drawable.ic_eye_icon);
                    password_editTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isShowpassword = true;
                }
            }
        });
        newPass_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isnewShowpassword == true) {
                    newPass_img.setImageResource(R.drawable.ic_eye_visibility);
                    newPassword_editTxt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isnewShowpassword = false;
                }else {
                    newPass_img.setImageResource(R.drawable.ic_eye_icon);
                    newPassword_editTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isnewShowpassword = true;
                }
            }
        });

        confirmPass_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isconfirmShowpassword == true) {
                    confirmPass_img.setImageResource(R.drawable.ic_eye_visibility);
                    alternatePass_editTxt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isconfirmShowpassword = false;
                }else {
                    confirmPass_img.setImageResource(R.drawable.ic_eye_icon);
                    alternatePass_editTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isconfirmShowpassword = true;
                }
            }
        });


    }

    //validation for signup
    private void Validation() {

        String old_pass = password_editTxt.getText().toString();
        String new_pass = newPassword_editTxt.getText().toString();
        String confirm_pass = alternatePass_editTxt.getText().toString();


        if (old_pass.equalsIgnoreCase("")) {
            password_editTxt.requestFocus();
            password_editTxt.setError("Please Enter Old Password");
        } else if (new_pass.equalsIgnoreCase("")) {
            newPassword_editTxt.requestFocus();
            newPassword_editTxt.setError("Please Enter New Password");
        } else if (confirm_pass.equalsIgnoreCase("")) {
            alternatePass_editTxt.requestFocus();
            alternatePass_editTxt.setError("Please Enter Confirm Password");
        }  else {
            callChangePassword_ws();
        }
    }


    private void callChangePassword_ws() {

        String url = Config.CHANGEpASS_URL;

        final ProgressDialog pDialog = singletonObj.createProgressDialog(this);
        pDialog.setCanceledOnTouchOutside(true);
        pDialog.show();
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response is", response.toString());
                        pDialog.hide();

                        // Convert String to json object
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            String message = json.getString("message");
                            String success = json.getString("success");
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            if(success.equalsIgnoreCase("1")) {
                                finish();
                            }
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("current_password",  password_editTxt.getText().toString());
                params.put("password" , newPassword_editTxt.getText().toString());
                params.put("password_confirmation" , alternatePass_editTxt.getText().toString());
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

}
