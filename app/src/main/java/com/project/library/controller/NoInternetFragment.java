package com.project.library.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.library.R;
import com.project.library.databinding.FragmentNoInternetBinding;

public class NoInternetFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private FragmentNoInternetBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoInternetBinding.inflate(inflater, container, false);

        binding.swiperefreshlayout.setOnRefreshListener(this);

        return binding.getRoot();
    }

    @Override
    public void onRefresh() {
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
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            if(MainActivity.Choice== 1)
                fragmentTransaction.replace(R.id.content_fragment, new AddBookFragment());
            else if(MainActivity.Choice == 2)
                fragmentTransaction.replace(R.id.content_fragment, new UpdateBookFragment());
            else if(MainActivity.Choice == 3)
                fragmentTransaction.replace(R.id.content_fragment, new DeleteBookFragment());

            fragmentTransaction.commit();
        }
        else{
            Toast.makeText(getContext(), "NO INTERNET...", Toast.LENGTH_SHORT).show();
            binding.swiperefreshlayout.setRefreshing(false);
        }
    }
}