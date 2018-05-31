package com.erel.chillsounds;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.erel.chillsounds.favorites.FavoritesFragment;
import com.erel.chillsounds.library.LibraryFragment;

public class MainFragmentAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments = new Fragment[2];

    MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments[position] == null){
            Fragment fragment = new Fragment();
            if(position == 0){
                fragment = new FavoritesFragment();
            }else if(position == 1){
                fragment = new LibraryFragment();
            }
            fragments[position] = fragment;
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
