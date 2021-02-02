package com.shoppi9driver.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shoppi9driver.app.R;
import com.shoppi9driver.app.adapter.NotificationAdapter;
import com.shoppi9driver.app.adapter.OrderHistoryAdapter;
import com.shoppi9driver.app.model.NotificationModel;
import com.shoppi9driver.app.model.OrderHistoryModel;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    ImageView back_image;
    RecyclerView notification_rv;
    NotificationAdapter notificationAdapter;
    ArrayList<NotificationModel> notificationList = new ArrayList<NotificationModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        back_image = (ImageView)findViewById(R.id.back_image);
        notification_rv = (RecyclerView) findViewById(R.id.notification_rv);

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setdataInList();
    }

    public void setdataInList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        notification_rv.setLayoutManager(linearLayoutManager);
        for (int i = 0;i<6;i++){
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setTitle("Notification Title");
            notificationList.add(notificationModel);
        }
        notificationAdapter = new NotificationAdapter(notificationList);
        notification_rv.setAdapter(notificationAdapter);
    }
}
