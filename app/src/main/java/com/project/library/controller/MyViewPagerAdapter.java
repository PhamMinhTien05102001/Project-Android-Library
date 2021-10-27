package com.project.library.controller;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.project.library.controller.TypeOfBookFragment;


public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String title = "";
        switch (position){      // Các tab trong tablayout trả về các Fragment tương ứng
            case  0:
                title = "ALL BOOK";
                break;
            case  1:
                title = "Web Development";
                break;
            case  2:
                title = "Artificial Intelligence";
                break;
            case  3:
                title = "DS and Algorithm";
                break;
            default:
                title = "ALL BOOK";
                break;
        }

        return new TypeOfBookFragment(title);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
}
