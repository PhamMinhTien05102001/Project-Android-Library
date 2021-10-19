package com.project.library.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.project.library.DepthPageTransformer;
import com.project.library.MyViewPagerAdapter;
import com.project.library.R;
import com.project.library.databinding.FragmentTypeOfBookBinding;

public class TypeOfBookFragment extends Fragment {
    private TextView mtextView;
    private View view;
    private String TypeOfBook;
    public TypeOfBookFragment(String s) {
        TypeOfBook = s;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_type_of_book, container, false).getRootView();

        mtextView = view.findViewById(R.id.tv_show);
        mtextView.setText(TypeOfBook);
        return view;
    }
}