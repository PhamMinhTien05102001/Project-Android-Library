package com.project.library.controller;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.library.R;
import com.project.library.databinding.FragmentUpdateBookBinding;
import com.project.library.model.Book;
import com.project.library.model.BookDatabase;

import java.util.ArrayList;
import java.util.List;

public class UpdateBookFragment extends Fragment {
    private FragmentUpdateBookBinding binding;
    private BookUpdateRecyclerViewAdapter bookUpdateRecyclerViewAdapter;
    private List<Book> listBook;
    private FragmentActivity myContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateBookBinding.inflate(inflater, container, false);

        listBook = getListBook("");
        bookUpdateRecyclerViewAdapter = new BookUpdateRecyclerViewAdapter(listBook);
        binding.revUpdate.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.revUpdate.setAdapter(bookUpdateRecyclerViewAdapter);

        bookUpdateRecyclerViewAdapter.setOnClickButtonEditListener(new BookUpdateRecyclerViewAdapter.OnClickButtonEditListener() {
            @Override
            public void EditBook(Book book) {
                EditBookFragment editBookFragment = new EditBookFragment();
                FragmentTransaction fragmentTransaction = myContext.getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putSerializable("book_edit", book);
                editBookFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.content_fragment, editBookFragment);
                fragmentTransaction.addToBackStack(EditBookFragment.class.getName());
                fragmentTransaction.commit();

            }
        });

        binding.etUpdateSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String key = binding.etUpdateSearch.getText().toString();
                listBook.clear();
                if(key.isEmpty()){
                    listBook.addAll(BookDatabase.getInstance(getContext()).bookDAO().GetListAllBook());
                }
                else{
                    for(Book b : BookDatabase.getInstance(getContext()).bookDAO().GetListAllBook()){
                        if(b.getBookId().toLowerCase().contains(key) || b.getBookName().toLowerCase().contains(key)
                                || b.getAuthor().toLowerCase().contains(key) || b.getCategory().toLowerCase().contains(key)
                                || b.getDescription().toLowerCase().contains(key))
                        {
                            listBook.add(b);
                        }
                    }

                }
                bookUpdateRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return binding.getRoot();
    }
    private List<Book> getListBook(String key) {
        if(key.isEmpty()){
            listBook = BookDatabase.getInstance(getContext()).bookDAO().GetListAllBook();
            return listBook;
        }
        else{
            List<Book> data = new ArrayList<>();
            for(Book i : BookDatabase.getInstance(getContext()).bookDAO().GetListAllBook()){
                if(i.getBookId().toLowerCase().contains(key) || i.getBookName().toLowerCase().contains(key)
                        || i.getAuthor().toLowerCase().contains(key) || i.getCategory().toLowerCase().contains(key)
                        || i.getDescription().toLowerCase().contains(key))
                {
                    data.add(i);
                }
            }
            listBook.clear();
            listBook = data;
            return listBook;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity) context;
    }
}