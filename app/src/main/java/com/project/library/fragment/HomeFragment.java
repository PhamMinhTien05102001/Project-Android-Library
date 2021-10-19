package com.project.library.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.project.library.DepthPageTransformer;
import com.project.library.MyViewPagerAdapter;
import com.project.library.R;
import com.project.library.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private  View view;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ViewPager2 myViewPager;
    private TabLayout myTabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //Set up TabLayout
        myViewPagerAdapter = new MyViewPagerAdapter(getActivity());
        myViewPager = view.findViewById(R.id.ViewPager);
        myTabLayout = view.findViewById(R.id.TabLayout);

        myViewPager.setAdapter(myViewPagerAdapter);

        new TabLayoutMediator(myTabLayout, myViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String title = "";
                switch (position){
                    case  0:
                        title = "Tất cả";
                        break;
                    case  1:
                        title = "IT";
                        break;
                    case  2:
                        title = "WIBU";
                        break;
                    case  3:
                        title = "Anime";
                        break;
                }
                tab.setText(title);
            }
        }).attach();

        myViewPager.setPageTransformer(new DepthPageTransformer());

        return view;
    }
}