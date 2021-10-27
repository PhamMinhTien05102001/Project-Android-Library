package com.project.library.controller;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.library.R;
import com.project.library.databinding.FragmentEditBookBinding;
import com.project.library.databinding.FragmentUpdateBookBinding;
import com.project.library.model.Book;
import com.project.library.model.BookDatabase;
import com.project.library.model.Category;
import com.project.library.model.ServiceAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBookFragment extends Fragment {
    private FragmentEditBookBinding binding;
    CategoryAdapter categoryAdapter;
    private Uri mUri;
    private ProgressDialog progressDialog;
    public static final int PERMISSION_CODE= 1;
    public static final int IMAGE_PICK_CODE= 2;
    private Book bookEdit;
    private String Save_Category = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditBookBinding.inflate(inflater, container, false);

        categoryAdapter = new CategoryAdapter(getContext(), R.layout.item_select_category, GetListCategory());
        binding.spinnerCategory.setAdapter(categoryAdapter);

        // ==================set màu cho star
        Drawable progress = binding.rbEditRating.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.BLUE);
        //======================

        MainActivity.FRAGMENT_INFO_EDIT_BOOK = 6;

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait ...");

        Bundle bundleReceive = getArguments();
        if(bundleReceive != null){
            bookEdit = (Book) bundleReceive.get("book_edit");
            binding.rbEditRating.setRating(bookEdit.getStar());

            if(!bookEdit.getImg().isEmpty()){
                Glide.with(getContext()).load(bookEdit.getImg()).into(binding.ivEditAvatarBook);
            }

            binding.etEditName.setText(bookEdit.getBookName());
            binding.tvEditBookId.setText(bookEdit.getBookId());
            binding.etEditAvailable.setText(String.valueOf(bookEdit.getAvailable()));
            binding.etEditPage.setText(String.valueOf(bookEdit.getNumberOfPages()));
            binding.etEditAuthor.setText(bookEdit.getAuthor());
            binding.etEditDescription.setText(bookEdit.getDescription());

            boolean Check_Category = false;
            int Index = 0;
            for(Category i : GetListCategory()){
                if(i.getNameCategory().equals(bookEdit.getCategory())){
                    binding.rdoDefault.setChecked(true);
                    binding.spinnerCategory.setEnabled(true);
                    binding.etEditCategory.setEnabled(false);
                    binding.spinnerCategory.setSelection(Index);
                    Check_Category = true;
                    break;
                }
                Index++;
            }
            if(Check_Category == false){
                binding.rdoAnother.setChecked(true);
                binding.spinnerCategory.setEnabled(false);
                binding.etEditCategory.setEnabled(true);
                binding.etEditCategory.setText(bookEdit.getCategory());
            }
        }

        binding.btnChangeImg.setOnClickListener(new View.OnClickListener() {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.rdoDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.spinnerCategory.setEnabled(true);
                binding.etEditCategory.setEnabled(false);
                binding.etEditCategory.setText("");
            }
        });

        binding.rdoAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.spinnerCategory.setEnabled(false);
                binding.etEditCategory.setEnabled(true);
            }
        });

        binding.btnEditAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.etEditCategory.getText().toString().isEmpty() && binding.rdoAnother.isChecked()){
                    Toast.makeText(getContext(),"Please Input Category!!!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etEditAvailable.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please Input Available!!!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etEditName.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please Input Name Of Book!!!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etEditAuthor.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please Input Author!!!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etEditPage.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please Input Pages!!!", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etEditDescription.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please Input Description!!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        int i0 = Integer.parseInt(binding.etEditAvailable.getText().toString());
                        int i1 = Integer.parseInt(binding.etEditPage.getText().toString());
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
                        UpdateAPI();
                    }
                    else{
                        Toast.makeText(getContext(), "No Internet ... Check Your Connection and Try Again", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        return binding.getRoot();
    }

    private void UpdateAPI(){
        progressDialog.show();

        Book bookNew = new Book();
        bookNew.set_id(bookEdit.get_id());
        bookNew.setImg(bookEdit.getImg()); // HIỆN TẠI CHƯA XỬ LÝ
        bookNew.setBookId(bookEdit.getBookId());
        bookNew.setBookName(binding.etEditName.getText().toString());
        bookNew.setAuthor(binding.etEditAuthor.getText().toString());
        bookNew.setStar(binding.rbEditRating.getRating());
        bookNew.setAvailable(Integer.parseInt(binding.etEditAvailable.getText().toString()));
        bookNew.setNumberOfPages(Integer.parseInt(binding.etEditPage.getText().toString()));
        bookNew.setDescription(binding.etEditDescription.getText().toString());

        if(binding.rdoAnother.isChecked()){
            bookNew.setCategory(binding.etEditCategory.getText().toString());
        }
        else{
            int Index = binding.spinnerCategory.getSelectedItemPosition();
            String Category_Now = categoryAdapter.getItem(Index).getNameCategory();
            bookNew.setCategory(Category_Now);
        }

        ServiceAPI.serviceApi.UpdateBookToAPI(bookNew.get_id(), bookNew).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                BookDatabase.getInstance(getContext()).bookDAO().update(bookNew);
                progressDialog.dismiss();
                Toast.makeText(getContext(), "UPDATE SUCCESS", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "UPDATE FAIL", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private List<Category> GetListCategory() {
        List<Category> data = new ArrayList<>();
        data.add(new Category("Web Development"));
        data.add(new Category("Artificial Intelligence"));
        data.add(new Category("DS and Algorithm"));
        return data;
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
            //Toast.makeText(getContext(), "URI : " + mUri, Toast.LENGTH_SHORT).show();

            Bitmap bitmapImage = null;
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.ivEditAvatarBook.setImageBitmap(bitmapImage);
        }
    }
}