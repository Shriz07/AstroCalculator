package com.example.astroweather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyPagerAdapter extends FragmentStateAdapter
{
    private AstroInfo info;
    private static int NUM_ITEMS = 2;

    public MyPagerAdapter(FragmentActivity fragmentActivity, AstroInfo info)
    {
        super(fragmentActivity);
        this.info = info;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0)
            return SunFragment.newInstance(info);
        else
            return MoonFragment.newInstance(info);
    }

    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }
}
