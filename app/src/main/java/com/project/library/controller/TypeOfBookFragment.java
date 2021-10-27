package com.project.library.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.library.R;
import com.project.library.databinding.FragmentHomeBinding;
import com.project.library.databinding.FragmentTypeOfBookBinding;
import com.project.library.model.Book;
import com.project.library.model.BookDAO;
import com.project.library.model.BookDatabase;
import com.project.library.view.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class TypeOfBookFragment extends Fragment {
    private FragmentTypeOfBookBinding binding;
    private View view;
    private String TypeOfBook;
    private BookRecyclerViewAdapter bookRecyclerViewAdapter;
    private FragmentActivity myContext;
    private List<Book> listBook;
    public TypeOfBookFragment(String s) {
        TypeOfBook = s;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTypeOfBookBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //Set LinearLayout cho recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.rcvDataHome.setLayoutManager(linearLayoutManager);

        //=================== Khởi tạo cho BookRecyclerViewAdapter
        if(MainActivity.GetDataForTextSearch().isEmpty()){
            MainActivity.SetDataForTextSearch("");
        }

        listBook = GetListBook(TypeOfBook);
        bookRecyclerViewAdapter = new BookRecyclerViewAdapter(listBook);// -> Bước này chuyển lên hàm construc để cho lần đầu chạy đã có dữ liệu
        binding.rcvDataHome.setAdapter(bookRecyclerViewAdapter);
        binding.rcvDataHome.addItemDecoration(new SimpleDividerItemDecoration(getContext()));   // Xét đường thẳng xen kẽ giữa các item_recyclerview

        bookRecyclerViewAdapter.setClickItemBookListener(new BookRecyclerViewAdapter.ClickItemBookListener() {
            @Override
            public void onClickItemBook(Book book) {
                InfoBookFragment infoBookFragment = new InfoBookFragment();
                FragmentTransaction fragmentTransaction = myContext.getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putSerializable("all_info_book", book);
                infoBookFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.content_fragment, infoBookFragment);
                fragmentTransaction.addToBackStack(InfoBookFragment.class.getName());
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity) context;
    }


    private List<Book> GetListBook(String typeOfBook) {
        if(typeOfBook.equals("ALL BOOK")){
            return BookDatabase.getInstance(getContext()).bookDAO().GetListAllBook();
        }
        else{
            return BookDatabase.getInstance(getContext()).bookDAO().GetListAllBookByCategory(typeOfBook);
        }
    }

}