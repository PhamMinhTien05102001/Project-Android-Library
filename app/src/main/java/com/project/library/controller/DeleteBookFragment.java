package com.project.library.controller;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.library.R;
import com.project.library.databinding.FragmentDeleteBookBinding;
import com.project.library.model.Book;
import com.project.library.model.BookDatabase;
import com.project.library.model.ServiceAPI;
import com.project.library.view.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteBookFragment extends Fragment {
    private FragmentDeleteBookBinding binding;
    ProgressDialog progressDialog;
    private List<Book> listBook;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeleteBookBinding.inflate(inflater, container, false);

        binding.recyclerviewDeleteBook.setLayoutManager(new LinearLayoutManager(getContext()));
        listBook = getListBook("");
        BookDeleteRecyclerViewAdapter bookDeleteRecyclerViewAdapter = new BookDeleteRecyclerViewAdapter(listBook);
        binding.recyclerviewDeleteBook.setAdapter(bookDeleteRecyclerViewAdapter);
        binding.recyclerviewDeleteBook.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait ...");

        binding.etDeleteSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String key = binding.etDeleteSearch.getText().toString();
                listBook = getListBook(key);
                BookDeleteRecyclerViewAdapter bookDeleteRecyclerViewAdapter = new BookDeleteRecyclerViewAdapter(listBook);
                binding.recyclerviewDeleteBook.setAdapter(bookDeleteRecyclerViewAdapter);
                OnClickButtonInfoAndDelete(bookDeleteRecyclerViewAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        OnClickButtonInfoAndDelete(bookDeleteRecyclerViewAdapter);

        return binding.getRoot();
    }

    public void OnClickButtonInfoAndDelete(BookDeleteRecyclerViewAdapter bookDeleteRecyclerViewAdapter){
        bookDeleteRecyclerViewAdapter.setOnClickButtonDeleteListener(new BookDeleteRecyclerViewAdapter.OnClickButtonDeleteListener() {
            @Override
            public void DeleteBook(Book book, int position) {
                new AlertDialog.Builder(getContext()).setTitle("Confirm Delete Book").setMessage("Are You Sure ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ServiceAPI.serviceApi.DeleteBookToAPI(book.get_id()).enqueue(new Callback<Book>() {
                                    @Override
                                    public void onResponse(Call<Book> call, Response<Book> response) {
                                        progressDialog.show();
                                        BookDatabase.getInstance(getContext()).bookDAO().delete(book);
                                        listBook.remove(position);
                                        bookDeleteRecyclerViewAdapter.notifyItemRemoved(position);
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<Book> call, Throwable t) {
                                        Toast.makeText(getContext(), "DELETE FAIL", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", null).show();
            }
        });

        bookDeleteRecyclerViewAdapter.setOnClickButtonInfoListener(new BookDeleteRecyclerViewAdapter.OnClickButtonInfoListener() {
            @Override
            public void InfoBook(Book book) {
                InfoBookFragment infoBookFragment = new InfoBookFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putSerializable("all_info_book", book);
                infoBookFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.content_fragment, infoBookFragment);
                fragmentTransaction.addToBackStack(InfoBookFragment.class.getName());
                fragmentTransaction.commit();
            }
        });
    }
    private List<Book> getListBook(String key) {
        if(key.isEmpty()){
            return BookDatabase.getInstance(getContext()).bookDAO().GetListAllBook();
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
            return data;
        }
    }
}