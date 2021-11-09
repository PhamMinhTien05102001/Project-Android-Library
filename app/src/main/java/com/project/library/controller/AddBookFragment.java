package com.project.library.controller;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.project.library.R;
import com.project.library.databinding.FragmentAddBookBinding;
import com.project.library.model.Book;
import com.project.library.model.BookCallAPI;
import com.project.library.model.BookDatabase;
import com.project.library.model.Category;
import com.project.library.model.ServiceAPI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookFragment extends Fragment {
    private FragmentAddBookBinding binding;
    public static final int PERMISSION_CODE= 1;
    public static final int IMAGE_PICK_CODE= 2;
    private Uri mUri;
    private ProgressDialog progressDialog;
    CategoryAdapter categoryAdapter;
    Book bookPostAPI;
    private String Save_Category = "";
    Map config = new HashMap();

    public static int State_MediaManager_ADD = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBookBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(getContext(), R.style.MyDialogStyle);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setCancelable(false);

        categoryAdapter = new CategoryAdapter(getContext(), R.layout.item_select_category, GetListCategory());
        binding.spinnerCategory.setAdapter(categoryAdapter);

        // ==================set màu cho star
        Drawable progress = binding.rbPostRating.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.BLUE);
        //======================

        bookPostAPI = new Book();

        //initCongif();

        binding.btnChoiceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        String []permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        PickImage();
                    }
                }
                else{
                    PickImage();
                }
            }
        });


        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(), categoryAdapter.getItem(i).getNameCategory(), Toast.LENGTH_SHORT).show();
                if(binding.rdoDefault.isChecked())
                    Save_Category = categoryAdapter.getItem(i).getNameCategory();
                    bookPostAPI.setCategory(Save_Category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.spinnerCategory.setEnabled(true);
        binding.etPostCategory.setEnabled(false);

        binding.rdoDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.spinnerCategory.setEnabled(true);
                binding.etPostCategory.setEnabled(false);
                binding.etPostCategory.setText("");

                bookPostAPI.setCategory(Save_Category);
            }
        });

        binding.rdoAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.spinnerCategory.setEnabled(false);
                binding.etPostCategory.setEnabled(true);
            }
        });

        binding.etPostCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bookPostAPI.setCategory(binding.etPostCategory.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.btnCalAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUri == null){
                    Toast.makeText(getContext(),"Please Choice Image", Toast.LENGTH_SHORT).show();
                }
                else if(CheckExistBookInDataBase(binding.etPostBookId.getText().toString())){
                    Toast.makeText(getContext(),"BookID Exist!!!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etPostCategory.getText().toString().isEmpty() && binding.rdoAnother.isChecked()){
                    Toast.makeText(getContext(),"Please Input Category!!!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etPostBookId.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please Input BookID!!!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etPostAvailable.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please Input Available!!!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etPostName.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please Input Name Of Book!!!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etPostAuthor.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please Input Author!!!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etPostPage.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please Input Pages!!!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etPostDescription.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please Input Description!!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        int i0 = Integer.parseInt(binding.etPostAvailable.getText().toString());
                        int i1 = Integer.parseInt(binding.etPostPage.getText().toString());
                    }catch (NumberFormatException nfe){
                        Toast.makeText(getContext(), "Please Check Format number : Available(Integer), Pages(Integer),", Toast.LENGTH_SHORT).show();
                        return;
                    }

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
                        progressDialog.show();

                        initCongif();
                        MediaManager.get().upload(mUri).callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {
                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {
                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                bookPostAPI.setImg(resultData.get("secure_url").toString());
                                //Toast.makeText(getContext(), "HIEN THI KQ:" + resultData.get("secure_url").toString(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {
                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {
                            }
                        }).option("folder", "books").dispatch();

                        Handler handler = new Handler(getContext().getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PostAPI();
                            }
                        }, 8000);

                    }
                    else{
                        Toast.makeText(getContext(), "No Internet ... Check Your Connection and Try Again", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        return binding.getRoot();
    }

    public boolean CheckExistBookInDataBase(String Id){       // Check xem có trùng với ID đã có trong csdl ko
        for(Book i : BookDatabase.getInstance(getContext()).bookDAO().GetListAllBook()){
            if( i.getBookId().equals(Id)){
                return true;
            }
        }
        return false;
    }

    private void initCongif() {
        config.put("cloud_name", "thefour123");
        config.put("api_key","717175813831459");
        config.put("api_secret","LgOqSL4uAr21kZIInC9Q8l7D0VI");

        if(State_MediaManager_ADD++ == 0 && EditBookFragment.State_MediaManager_EDIT++ == 0){
            MediaManager.init(getContext(), config);
        }


    }

    private List<Category> GetListCategory() {
        List<Category> data = new ArrayList<>();
        data.add(new Category("Web Development"));
        data.add(new Category("Artificial Intelligence"));
        data.add(new Category("DS and Algorithm"));
        return data;
    }

    private void PostAPI() {

        //bookId;
        bookPostAPI.setBookId(binding.etPostBookId.getText().toString());
        //bookName;
        bookPostAPI.setBookName(binding.etPostName.getText().toString());
        //author;
        bookPostAPI.setAuthor(binding.etPostAuthor.getText().toString());
        //available;
        bookPostAPI.setAvailable(Integer.parseInt(binding.etPostAvailable.getText().toString()));
        //description;
        bookPostAPI.setDescription(binding.etPostDescription.getText().toString());
        //star;
        bookPostAPI.setStar(binding.rbPostRating.getRating());
        //numberOfPages;
        bookPostAPI.setNumberOfPages(Integer.parseInt(binding.etPostPage.getText().toString()));

        //Toast.makeText(getContext(), "LINK ANH" + bookPostAPI.getImg(), Toast.LENGTH_SHORT).show();
        ServiceAPI.serviceApi.SendBookToAPI(bookPostAPI).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                BookDatabase.getInstance(getContext()).bookDAO().insert(bookPostAPI);
                Handler handler = new Handler(getContext().getMainLooper());
                CallAPIUpdateBook();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "ADD SUCCESS", Toast.LENGTH_SHORT).show();
                    }
                }, 5000);
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "ADD FAIL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CallAPIUpdateBook(){
        ServiceAPI.serviceApi.GetAllBook().enqueue(new Callback<BookCallAPI>() {
            @Override
            public void onResponse(Call<BookCallAPI> call, Response<BookCallAPI> response) {
                BookCallAPI bookCallAPI = response.body();

                List<Book> list = bookCallAPI.getBooks();
                int count = 0;

                for(Book i : list){
                   BookDatabase.getInstance(getContext()).bookDAO().update(i);
                }
            }

            @Override
            public void onFailure(Call<BookCallAPI> call, Throwable t) {

            }
        });
    }

    private void PickImage() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK);
        cameraIntent.setType("image/*");
        startActivityForResult(cameraIntent, IMAGE_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            //binding.ivShow.setImageURI(data.getData());
            Uri returnUri = data.getData();
            mUri = returnUri;

            Bitmap bitmapImage = null;
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.ivShow.setImageBitmap(bitmapImage);
            binding.tvInImg.setText(" ");
        }
    }
}