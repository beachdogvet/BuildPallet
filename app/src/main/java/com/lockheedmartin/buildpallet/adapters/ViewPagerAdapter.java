package com.lockheedmartin.buildpallet.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lockheedmartin.buildpallet.fragments.Fragment_Locations;
import com.lockheedmartin.buildpallet.fragments.Fragment_Options;
import com.lockheedmartin.buildpallet.fragments.Fragment_main;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"Barcodes", "Locations", "Options"};
    private int NUMBER_OF_VIEWS = 3;


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        position = position +1;
        Bundle bundle;

        switch(position) {
            case 1:
                Fragment_main fragmentMain = new Fragment_main();
//                bundle = new Bundle();
//                bundle.putString("messageToFragment", "View One");
//                fragment1.setArguments(bundle);
                return fragmentMain;
            case 2:

                Fragment_Locations fragment_locations = new Fragment_Locations();
                return fragment_locations;

            case 3:

                Fragment_Options fragment_options = new Fragment_Options();
                return fragment_options;

        }
        return null;
    }

    @Override
    public int getCount() {

        return NUMBER_OF_VIEWS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles[position];
    }




}
