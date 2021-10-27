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
import java.util.List;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBookBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait ...");

        categoryAdapter = new CategoryAdapter(getContext(), R.layout.item_select_category, GetListCategory());
        binding.spinnerCategory.setAdapter(categoryAdapter);

        // ==================set màu cho star
        Drawable progress = binding.rbPostRating.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.BLUE);
        //======================

        bookPostAPI = new Book();

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
                        PostAPI();
                    }
                    else{
                        Toast.makeText(getContext(), "No Internet ... Check Your Connection and Try Again", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        return binding.getRoot();
    }

    private List<Category> GetListCategory() {
        List<Category> data = new ArrayList<>();
        data.add(new Category("Web Development"));
        data.add(new Category("Artificial Intelligence"));
        data.add(new Category("DS and Algorithm"));
        return data;
    }

    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    private void PostAPI() {
        progressDialog.show();
        //bookId;
        bookPostAPI.setBookId(binding.etPostBookId.getText().toString());
        //bookName;
        bookPostAPI.setBookName(binding.etPostName.getText().toString());
        //author;
        bookPostAPI.setAuthor(binding.etPostAuthor.getText().toString());
        //available;
        bookPostAPI.setAvailable(Integer.parseInt(binding.etPostAvailable.getText().toString()));
//        private String category;
//        private String img;
        //description;
        bookPostAPI.setDescription(binding.etPostDescription.getText().toString());
        //star;
        bookPostAPI.setStar(binding.rbPostRating.getRating());
        //numberOfPages;
        bookPostAPI.setNumberOfPages(Integer.parseInt(binding.etPostPage.getText().toString()));

        RequestBody requestBodyBookId = RequestBody.create(MediaType.parse("multipart/form-data"), bookPostAPI.getBookId());
        RequestBody requestBodyBookName = RequestBody.create(MediaType.parse("multipart/form-data"), bookPostAPI.getBookName());
        RequestBody requestBodyAuthor = RequestBody.create(MediaType.parse("multipart/form-data"), bookPostAPI.getAuthor());
        RequestBody requestBodyAvailable = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(bookPostAPI.getAvailable()));
        RequestBody requestBodyCategory = RequestBody.create(MediaType.parse("multipart/form-data"), bookPostAPI.getCategory());
        //RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"), "abc");
        RequestBody requestBodyDescription = RequestBody.create(MediaType.parse("multipart/form-data"), bookPostAPI.getDescription());
        RequestBody requestBodyStar = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(bookPostAPI.getStar()));
        RequestBody requestBodyNumberOfPages = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(bookPostAPI.getNumberOfPages()));

        String strRealPath = RealPathUtil.getRealPath(getContext(), mUri);
//        String strRealPath1 = getRealPathFromURI(mUri);
//
        Log.e("ABC", strRealPath);
        File file = new File(strRealPath);

        RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part muPart = MultipartBody.Part.createFormData("img", file.getName(), requestBodyImg);
        ServiceAPI.serviceApi.SendBookToAPI(requestBodyBookId, requestBodyBookName, requestBodyAuthor, requestBodyAvailable,
                requestBodyCategory, null, requestBodyDescription, requestBodyStar, requestBodyNumberOfPages).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Book book1 = response.body();
                if(book1 != null){
                    BookDatabase.getInstance(getContext()).bookDAO().insert(bookPostAPI);
                 }
                progressDialog.dismiss();
                Toast.makeText(getContext(), "ADD SUCCESS", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "ADD FAIL", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "URI : " + mUri, Toast.LENGTH_SHORT).show();

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