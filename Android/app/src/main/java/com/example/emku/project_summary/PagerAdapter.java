package com.example.emku.project_summary;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by EmKu on 28.12.2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;
    public PagerAdapter(FragmentManager fm , int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                dosya_sec dosya_sec = new dosya_sec();
                return dosya_sec;
            case 1:
                veri_gir veri_gir = new veri_gir();
                return veri_gir;
            default:
                return  null;

        }
    }
}
