package com.example.bitcoinmarketprice.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentPagerAdapter extends FragmentStateAdapter {

    private static final int NUM_PAGES = 2;

    public FragmentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return Bitcoin.newInstance();
        } else {
            return Historical.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
