package com.example.app;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MainActivity extends AppCompatActivity {

    TabLayout mTab;
    ViewPager mVP;

    View view1 ;
    View view2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTab=findViewById(R.id.tab);
        mVP=findViewById(R.id.v1);
        view1= LayoutInflater.from(this).inflate(R.layout.ts_v_1,null);
        view2= LayoutInflater.from(this).inflate(R.layout.ts_v_2,null);
        MyPageAdapter pageAdapter=new MyPageAdapter(new String[]{"Star","Gold"});
        mVP.setAdapter(pageAdapter);
        mTab.setupWithViewPager(mVP);
    }

    private class MyPageAdapter extends PagerAdapter{
        private String[] titles;

        public MyPageAdapter(String[] title){
            titles=title;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (position==0){
                container.addView(view1);
            }else {
                container.addView(view2);
            }
            return position==0?view1:view2;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (position==0){
                container.removeView(view1);
            }else {
                container.removeView(view2);
            }
        }
    }
}
