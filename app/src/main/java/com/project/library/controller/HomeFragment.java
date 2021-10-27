package com.project.library.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.project.library.databinding.FragmentHomeBinding;
import com.project.library.model.Book;
import com.project.library.model.BookCallAPI;
import com.project.library.model.BookDatabase;
import com.project.library.model.ServiceAPI;
import com.project.library.view.DepthPageTransformer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private FragmentHomeBinding binding;
    private View view;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ProgressDialog progressDialog;
    private static int Save_Index_TabLayout = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        binding.swipeRefreshLayout.setOnRefreshListener(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait ...");

        binding.TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Save_Index_TabLayout = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        myViewPagerAdapter = new MyViewPagerAdapter(getActivity());
        binding.ViewPager.setAdapter(myViewPagerAdapter);
        binding.ViewPager.setCurrentItem(Save_Index_TabLayout);

        new TabLayoutMediator(binding.TabLayout, binding.ViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {  //set tên cho các tab
                String title = "";
                switch (position){
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
                }
                tab.setText(title);

                //myViewPagerAdapter.notifyDataSetChanged();
            }
        }).attach();


        binding.ViewPager.setPageTransformer(new DepthPageTransformer());   //set hiệu ứng cho viewpager

        progressDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CallAPI();
            }
        }, 1000);
        return view;
    }

    @Override
    public void onRefresh() {
        progressDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CallAPI();
            }
        }, 2000);

        binding.swipeRefreshLayout.setRefreshing(false);
    }

    public boolean CheckExistBook(String Id){       // Check xem có trùng với ID đã có trong csdl ko
        List<Book> data = BookDatabase.getInstance(getContext()).bookDAO().GetListAllBookByBookId(Id);
        for(Book i : data){
            if( i.getBookId().equals(Id)){
                return true;
            }
        }
        return false;
    }

    private void CallAPI(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        if(connected){
            ServiceAPI.serviceApi.GetAllBook().enqueue(new Callback<BookCallAPI>() {
                @Override
                public void onResponse(Call<BookCallAPI> call, Response<BookCallAPI> response) {
                    BookCallAPI bookCallAPI = response.body();

                    List<Book> list = bookCallAPI.getBooks();

                    for(Book i : list){
                        if(!CheckExistBook(i.getBookId())){
                            BookDatabase.getInstance(getContext()).bookDAO().insert(i);
                        }
                    }

                    myViewPagerAdapter = new MyViewPagerAdapter(getActivity());
                    binding.ViewPager.setAdapter(myViewPagerAdapter);
                    binding.ViewPager.setCurrentItem(Save_Index_TabLayout);

                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Loading Data Done!!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<BookCallAPI> call, Throwable t) {
                     Toast.makeText(getContext(), "CAN'T LOAD DATA....", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
        else{
            Toast.makeText(getContext(), "YOU ARE OFFLINE....", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }
}