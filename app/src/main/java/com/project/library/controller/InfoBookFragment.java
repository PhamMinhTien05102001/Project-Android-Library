package com.project.library.controller;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.project.library.databinding.FragmentInfoBookBinding;
import com.project.library.model.Book;

public class InfoBookFragment extends Fragment {
    private FragmentInfoBookBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoBookBinding.inflate(inflater, container, false);

        MainActivity.FRAGMENT_INFO_EDIT_BOOK = 6;

        // ==================set màu cho star
        Drawable progress = binding.rbInfoStarBook.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.BLUE);
        //=================================

        Bundle bundleReceive = getArguments();
        if(bundleReceive != null){
            Book book = (Book) bundleReceive.get("all_info_book");
            binding.rbInfoStarBook.setRating(book.getStar());
            Glide.with(getContext()).load(book.getImg()).into(binding.ivInfoAvatarBook);

            binding.tvInfoName.setText(Html.fromHtml("<b> Name : </b>" + "<i>" + book.getBookName() + "</i>"));
            binding.tvInfoBookID.setText(Html.fromHtml("<b> BookID : </b>" + "<i>" + book.getBookId() + "</i>"));
            binding.tvInfoAvailable.setText(Html.fromHtml("<b> Available : </b>" + "<i>" + book.getAvailable() + "</i>"));
            binding.tvInfoPage.setText(Html.fromHtml("<b> Page : </b>" + "<i>" + book.getNumberOfPages() + "</i>"));
            binding.tvInfoAuthor.setText(Html.fromHtml("<b> Author : </b>" + "<i>" + book.getAuthor() + "</i>"));
            binding.tvInfoCategory.setText(Html.fromHtml("<b> Category : </b>" + "<i>" + book.getCategory() + "</i>"));
            binding.tvInfoDescription.setText(Html.fromHtml("<b> Description : </b>" + "<i>" + book.getDescription() + "</i>"));
        }

        return binding.getRoot();
    }

}