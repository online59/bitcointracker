package com.example.bitcoinmarketprice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bitcoinmarketprice.historyrecycler.HistoricPriceAdapter;
import com.example.bitcoinmarketprice.model.BitcoinMeta;
import com.example.bitcoinmarketprice.room.BitcoinPrice;
import com.example.bitcoinmarketprice.vm.MainViewModel;

import java.util.List;

public class HistoryPriceFragment extends Fragment {

    private RecyclerView historicPriceRecyclerView;
    private HistoricPriceAdapter historicPriceAdapter;

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
        setupViewModel();
        
        return  view;
    }

    private void reloadUi() {
        FragmentTransaction fragment = getParentFragmentManager().beginTransaction();
        fragment.setReorderingAllowed(false);
        fragment.detach(this).attach(this).commit();
    }

    private void setupRecyclerView(List<BitcoinPrice> data) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        historicPriceRecyclerView.setLayoutManager(layoutManager);

        historicPriceAdapter = new HistoricPriceAdapter(data, getContext());
        historicPriceRecyclerView.setAdapter(historicPriceAdapter);
        historicPriceAdapter.notifyDataSetChanged();

        historicPriceRecyclerView.setHasFixedSize(true);

    }

    private void setupViewModel() {
        MainViewModel viewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);
        viewModel.getBitcoinHistoricPrice(getContext()).observe(getViewLifecycleOwner(), bitcoinPrices -> {
            setupRecyclerView(bitcoinPrices);
            reloadUi();
        });
    }

    private void bindView(View view) {
        historicPriceRecyclerView = view.findViewById(R.id.history_recycler_view);

        MainViewModel viewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        TextView btnClear = view.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(view1 -> {
            viewModel.deleteAll(getContext());
            historicPriceAdapter.notifyDataSetChanged();
        });
    }
}