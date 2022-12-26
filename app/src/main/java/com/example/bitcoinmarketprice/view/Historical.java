package com.example.bitcoinmarketprice.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bitcoinmarketprice.R;
import com.example.bitcoinmarketprice.vm.MainViewModel;

public class Historical extends Fragment {

    MainViewModel viewModel;

    public Historical() {
        // Required empty public constructor
    }

    public static Historical newInstance() {
        return new Historical();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historical, container, false);
        setRecyclerView(view);
        return view;
    }


    private void setRecyclerView(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        HistoricPriceAdapter adapter = new HistoricPriceAdapter(viewModel, this);
        ItemSeparatorDecoration decoration = new ItemSeparatorDecoration(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(decoration);
    }
}