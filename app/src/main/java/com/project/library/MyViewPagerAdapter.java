package com.project.library;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.project.library.fragment.TypeOfBookFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
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
        return new TypeOfBookFragment(title);
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
