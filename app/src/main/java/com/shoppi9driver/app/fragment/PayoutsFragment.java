package com.shoppi9driver.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.shoppi9driver.app.R;

public class PayoutsFragment extends Fragment {

    private static String TAG = "PayoutsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.payouts_fragment, container, false);

//        rv_myorder = view.findViewById(R.id.rv_myorder);
//        nodata_txt = view.findViewById(R.id.nodata_txt);
//
//        setdataInList();
        return view;

    }
}
