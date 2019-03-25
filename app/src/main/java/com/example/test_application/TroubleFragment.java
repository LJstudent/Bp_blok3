package com.example.test_application;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TroubleFragment extends Fragment {

    public TroubleFragment() {
        // Required empty public constructor
    }

    public static TroubleFragment newInstance(String param1, String param2) {
        TroubleFragment fragment = new TroubleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trouble, container, false);


        return view;
    }

}