package com.project.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ReportFragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.project.library.databinding.ActivityMainBinding;
import com.project.library.fragment.AddBookFragment;
import com.project.library.fragment.DeleteBookFragment;
import com.project.library.fragment.HomeFragment;
import com.project.library.fragment.UpdateBookFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;


    private static final int FRAGMENT_HOME = 1;
    private static final int FRAGMENT_ADD = 2;
    private static final int FRAGMENT_UP = 3;
    private static final int FRAGMENT_DEL = 4;
    private int FRAGMENT_NOW = FRAGMENT_HOME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("QUẢN LÝ SÁCH THƯ VIỆN");

        //Khi chọn 3 gạch ngang trên menu sẽ mở drawer layout ra
        setSupportActionBar(binding.toolBar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolBar, R.string.nav_open_drawer,
                                                                    R.string.nav_close_drawer);

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //=======================================================
        binding.navigationView.setItemIconTintList(null);       // Đặt lại màu mặt định của icon trên drawer

        binding.navigationView.setNavigationItemSelectedListener(this); // Set up sự kiện khi người dùng click

        ReplaceFragment(new HomeFragment());
        binding.navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);  // Chọn Home (tô đậm) trên drawer khi vừa mở app


    }

    @Override
    public void onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){ // Kiểm tra nếu đang mở thì đóng lại
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    private void ReplaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // Cài đặt sự kiện khi người dùng click vào
        int id = item.getItemId();

        if(id == R.id.nav_home){
            if(FRAGMENT_NOW != FRAGMENT_HOME){
                ReplaceFragment(new HomeFragment());
                FRAGMENT_NOW = FRAGMENT_HOME;
            }
        }
        else if(id == R.id.nav_add_book){
            if(FRAGMENT_NOW != FRAGMENT_ADD){
                ReplaceFragment(new AddBookFragment());
                FRAGMENT_NOW = FRAGMENT_ADD;
            }
        }
        else if(id == R.id.nav_update_book){
            if(FRAGMENT_NOW != FRAGMENT_UP){
                ReplaceFragment(new UpdateBookFragment());
                FRAGMENT_NOW = FRAGMENT_UP;
            }
        }
        else if(id == R.id.nav_delete_book){
            if(FRAGMENT_NOW != FRAGMENT_DEL){
                ReplaceFragment(new DeleteBookFragment());
                FRAGMENT_NOW = FRAGMENT_DEL;
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START); // Đóng Drawer lại
        return true;
    }
}