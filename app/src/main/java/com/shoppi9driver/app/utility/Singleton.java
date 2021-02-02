package com.shoppi9driver.app.utility;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.shoppi9driver.app.Activity.Login_Activity;
import com.shoppi9driver.app.R;

import org.json.JSONObject;

import java.io.Serializable;

public class Singleton implements Serializable {

    private static final Singleton instance = new Singleton();
    public int comingNotification = 0;
    public int selectedTab;

    public Singleton() {
    }

    public static Singleton getInstance() {
        return instance;
    }

    public void showCustomAlert(Context context, String message) {
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = LayoutInflater.from(context);

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.toast, null);

        TextView txtView = (TextView) toastRoot.findViewById(R.id.txtView);

        txtView.setText(message);

        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        // dialog.setMessage(Message);
        return dialog;
    }
}
