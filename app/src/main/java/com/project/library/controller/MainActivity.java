package com.project.library.controller;

import static android.os.SystemClock.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Database;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.project.library.R;
import com.project.library.databinding.ActivityMainBinding;
import com.project.library.controller.AddBookFragment;
import com.project.library.controller.DeleteBookFragment;
import com.project.library.controller.HomeFragment;
import com.project.library.controller.UpdateBookFragment;
import com.project.library.model.Book;
import com.project.library.model.BookCallAPI;
import com.project.library.model.BookDatabase;
import com.project.library.model.ServiceAPI;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    private BookDatabase bookDatabase;
    private HomeFragment homeFragment;

    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_ADD = 2;
    public static final int FRAGMENT_UP = 3;
    public static final int FRAGMENT_DEL = 4;
    public static final int FRAGMENT_NI = 5;
    public static int FRAGMENT_INFO_EDIT_BOOK = 0;
    public static int FRAGMENT_NOW = FRAGMENT_HOME;
    public static String TEXT_SEARCH = "";
    public static int Choice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Library Management");

        //Khi chọn 3 gạch ngang trên menu sẽ mở drawer layout ra
        setSupportActionBar(binding.toolBar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolBar, R.string.nav_open_drawer,
                                                                    R.string.nav_close_drawer);

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //=======================================================
        binding.navigationView.setItemIconTintList(null);       // Đặt lại màu mặt định của icon trên drawer

        binding.navigationView.setNavigationItemSelectedListener(this); // Set up sự kiện khi người dùng click


        binding.navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);  // Chọn Home (tô đậm) trên drawer khi vừa mở app

        //=======================Khoi tao DATABASE cua BOOK
        bookDatabase = BookDatabase.getInstance(this);

        homeFragment = new HomeFragment();
        ReplaceFragment(homeFragment);
    }
    public static void SetDataForTextSearch(String data){
        TEXT_SEARCH = data;
    }
    public static String GetDataForTextSearch(){
        return TEXT_SEARCH;
    }
    @Override
    public void onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){ // Kiểm tra nếu đang mở thì đóng lại
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(FRAGMENT_NOW == 1 || FRAGMENT_NOW == 2 || FRAGMENT_NOW == 3 || FRAGMENT_NOW == 4 || FRAGMENT_NOW == 5 ){
            if(FRAGMENT_INFO_EDIT_BOOK == 6){    // Đã mở InfoBookFragment thì reset FRAGMENT_INFO_BOOK = 0 và chuyển lại fragment cũ
                FRAGMENT_INFO_EDIT_BOOK = 0;
                super.onBackPressed();
            }
            else{
                OpenDialogExit(Gravity.CENTER);
            }
        }
        else{
            super.onBackPressed();      // Trường hợp của InfoBookFragment
        }

    }

    private void OpenDialogExit(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_exit);

        Window window = dialog.getWindow();
        //if(window != null){
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttribute = window.getAttributes();
            windowAttribute.gravity = gravity;
            window.setAttributes(windowAttribute);
        //}

        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        Button btn_no = dialog.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void ReplaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // Cài đặt sự kiện khi người dùng click vào
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        int id = item.getItemId();

        if(id == R.id.nav_home){
            Choice = 0;
            if(FRAGMENT_NOW != FRAGMENT_HOME){
                ReplaceFragment(new HomeFragment());
                FRAGMENT_NOW = FRAGMENT_HOME;

            }
        }
        else if(id == R.id.nav_add_book){
            Choice = 1;
            if(connected){
                if(FRAGMENT_NOW != FRAGMENT_ADD){
                    ReplaceFragment(new AddBookFragment());
                    FRAGMENT_NOW = FRAGMENT_ADD;
                }
            }
            else{
                ReplaceFragment(new NoInternetFragment());
                Toast.makeText(MainActivity.this, "No Internet ... Check Your Connection and Try Again", Toast.LENGTH_SHORT).show();
                FRAGMENT_NOW = FRAGMENT_NI;
            }

        }
        else if(id == R.id.nav_update_book){
            Choice = 2;
            if(connected){
                if(FRAGMENT_NOW != FRAGMENT_UP){
                    ReplaceFragment(new UpdateBookFragment());
                    FRAGMENT_NOW = FRAGMENT_UP;
                }
            }
            else{
                ReplaceFragment(new NoInternetFragment());
                Toast.makeText(MainActivity.this, "No internet ... Please check connected internet again", Toast.LENGTH_SHORT).show();
                FRAGMENT_NOW = FRAGMENT_NI;
            }

        }
        else if(id == R.id.nav_delete_book){
            Choice = 3;
            if(connected){
                if(FRAGMENT_NOW != FRAGMENT_DEL){
                    ReplaceFragment(new DeleteBookFragment());
                    FRAGMENT_NOW = FRAGMENT_DEL;
                }
            }
            else{
                ReplaceFragment(new NoInternetFragment());
                Toast.makeText(MainActivity.this, "No internet ... Please check connected internet again", Toast.LENGTH_SHORT).show();
                FRAGMENT_NOW = FRAGMENT_NI;
            }

        }

        binding.drawerLayout.closeDrawer(GravityCompat.START); // Đóng Drawer lại
        return true;
    }


}
