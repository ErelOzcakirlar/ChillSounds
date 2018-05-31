package com.erel.chillsounds;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private ViewPager contentPager;
    private MainFragmentAdapter contentAdapter;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentPager = findViewById(R.id.contentPager);
        contentAdapter = new MainFragmentAdapter(getSupportFragmentManager());
        contentPager.setAdapter(contentAdapter);
        contentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    bottomNav.setSelectedItemId(R.id.favorites);
                }else if(position == 1){
                    bottomNav.setSelectedItemId(R.id.library);
                }
                onItemSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.favorites:
                        contentPager.setCurrentItem(0, true);
                        onItemSelected(0);
                        return true;
                    case R.id.library:
                        contentPager.setCurrentItem(1, true);
                        onItemSelected(1);
                        return true;
                        default:
                            return false;
                }
            }
        });

        onItemSelected(0);
    }

    private void onItemSelected(int index){
        if (index == 0) {
            setTitle(R.string.favorites);
        }else if (index == 1) {
            setTitle(R.string.library);
        }
    }
}
