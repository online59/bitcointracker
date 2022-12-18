package com.example.bitcoinmarketprice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitcoinmarketprice.historyrecycler.HistoricPriceAdapter;
import com.example.bitcoinmarketprice.vm.MainViewModel;

public class HistoryPriceFragment extends Fragment {

    private RecyclerView historicPriceRecyclerView;
    private HistoricPriceAdapter historicPriceAdapter;
    MainViewModel viewModel;

    public HistoryPriceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_price, container, false);

        bindView(view);
        setupRecyclerView();
        loadBitcoinHistoricPrice();

        return view;
    }

    private void setupRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        historicPriceRecyclerView.setLayoutManager(layoutManager);

        historicPriceAdapter = new HistoricPriceAdapter();
        historicPriceRecyclerView.setHasFixedSize(true);

    }

    private void loadBitcoinHistoricPrice() {

        viewModel.getBitcoinHistoricPrice().observe(getViewLifecycleOwner(), priceList -> {
            historicPriceAdapter.setListData(priceList);
            historicPriceRecyclerView.setAdapter(historicPriceAdapter);
        });
    }

    private void bindView(View view) {
        historicPriceRecyclerView = view.findViewById(R.id.history_recycler_view);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        TextView btnClear = view.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(view1 -> viewModel.deleteAll());
    }
}