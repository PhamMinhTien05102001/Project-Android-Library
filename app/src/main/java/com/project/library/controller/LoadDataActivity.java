package com.project.library.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.project.library.R;
import com.project.library.databinding.ActivityLoadDataBinding;
import com.project.library.model.Book;
import com.project.library.model.BookCallAPI;
import com.project.library.model.BookDatabase;
import com.project.library.model.Photo;
import com.project.library.model.ServiceAPI;
import com.project.library.view.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadDataActivity extends AppCompatActivity {
    private ActivityLoadDataBinding binding;
    private BookDatabase bookDatabase;
    private PhotoViewPagerAdapter photoViewPagerAdapter;
    private List<Photo> listPhoto;
    private String[] info = {"Quiet space", "Variety Of Books", "Library Cafeteria"};
    private static  int Count = 0;  // Xử lý việc back về từ MainActivity
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(binding.viewpagerSlide.getCurrentItem() == listPhoto.size() - 1){
                binding.viewpagerSlide.setCurrentItem(0);
                binding.tvShowInfo.setText(info[0]);
            }
            else{
                binding.viewpagerSlide.setCurrentItem(binding.viewpagerSlide.getCurrentItem() + 1);
                binding.tvShowInfo.setText(info[binding.viewpagerSlide.getCurrentItem()]);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bookDatabase = BookDatabase.getInstance(this);

        binding.progressBar.setIndeterminateDrawable(new ThreeBounce());

        //===============set up cho slide photo
        photoViewPagerAdapter = new PhotoViewPagerAdapter(GetListPhoto());
        binding.viewpagerSlide.setAdapter(photoViewPagerAdapter);
        binding.circleIndicator.setViewPager(binding.viewpagerSlide);

        binding.viewpagerSlide.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() { //Xét AUTO RUN cho slide
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 1500);
            }
        });

        binding.viewpagerSlide.setPageTransformer(new ZoomOutPageTransformer());   //set hiệu ứng cho viewpager
        binding.tvShowInfo.setText(info[0]);

        //=================Call API
        CallAPI();

    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        Count++;
        if(Count > 1){      // Lần đầu tiên là lần load Form, lần sau thì tắt app
            Count = 0;
            finish();
        }

    }

    private List<Photo> GetListPhoto() {
        listPhoto = new ArrayList<>();
        listPhoto.add(new Photo(R.drawable.slide_1));
        listPhoto.add(new Photo(R.drawable.slide_2));
        listPhoto.add(new Photo(R.drawable.slide_3));
        return listPhoto;
    }


    public boolean CheckExistBook(String Id){       // Check xem có trùng với ID đã có trong csdl ko
        List<Book> data = bookDatabase.bookDAO().GetListAllBookByBookId(Id);
        for(Book i : data){
            if( i.getBookId().equals(Id)){
                return true;
            }
        }
        return false;
    }

    private void CallAPI(){
        ServiceAPI.serviceApi.GetAllBook().enqueue(new Callback<BookCallAPI>() {
            @Override
            public void onResponse(Call<BookCallAPI> call, Response<BookCallAPI> response) {
                BookCallAPI bookCallAPI = response.body();

                binding.progressBar.setMax(bookCallAPI.getResults());
                List<Book> list = bookCallAPI.getBooks();
                int count = 0;

                for(Book i : list){
                    count += 1;
                    if(!CheckExistBook(i.getBookId())){
                        bookDatabase.bookDAO().insert(i);
                    }
                    binding.progressBar.setProgress(count);
                }

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoadDataActivity.this, MainActivity.class);
                        intent.putExtra("no_back", "no");
                        startActivity(intent);
                    }
                }, 1000);

            }

            @Override
            public void onFailure(Call<BookCallAPI> call, Throwable t) {
                boolean CheckIsNull = false;

                if(bookDatabase.bookDAO().GetListAllBook().isEmpty()){
                    CheckIsNull = true;
                }

                Handler mHandler = new Handler();
                boolean finalCheckIsNull = CheckIsNull;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoadDataActivity.this, MainActivity.class);
                        if(!finalCheckIsNull){
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoadDataActivity.this, "Need Internet To Load Data ", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    }
                }, 1000);

            }
        });
    }

}