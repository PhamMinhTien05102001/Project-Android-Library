package com.project.library.model;

import androidx.room.Delete;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ServiceAPI {
    public static final String BASE_URL = "https://thefour123.herokuapp.com/";

    ServiceAPI serviceApi = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).
                                build().create(ServiceAPI.class);

    @GET("books")
    Call<BookCallAPI> GetAllBook();

    @Multipart
    @POST("books")
    Call<Book> SendBookToAPI(@Part("bookId") RequestBody bookId,
                             @Part("bookName") RequestBody bookName,
                             @Part("author") RequestBody author,
                             @Part("available") RequestBody available,
                             @Part("category") RequestBody category,
                             @Part MultipartBody.Part img,
                             @Part("description") RequestBody description,
                             @Part("star") RequestBody star,
                             @Part("numberOfPages") RequestBody numberOfPages);

    @PATCH("books/{id}")
    Call<Book> UpdateBookToAPI(@Path("id") String id, @Body Book book);

    @DELETE("books/{id}")
    Call<Book> DeleteBookToAPI(@Path("id") String id);

//    @Multipart
//    @POST("books")
//    Call<Book> SendBookToAPI(@Part("bookId") RequestBody bookId,
//                             @Part("bookName") RequestBody bookName,
//                             @Part("author") RequestBody author,
//                             @Part("available") byte available,
//                             @Part("category") RequestBody category,
//                             @Part("img") RequestBody img,
//                             @Part("description") RequestBody description,
//                             @Part("star") RequestBody star,
//                             @Part("numberOfPages") RequestBody numberOfPages);

    //@Multipart
//    @POST("books")
//    Call<Book> SendBookToAPI(@Part("bookId") String bookId,
//                             @Part("bookName") String bookName,
//                             @Part("author") String author,
//                             @Part("available") int available,
//                             @Part("category") String category,
//                             @Part("img") String img,
//                             @Part("description") String description,
//                             @Part("star") float star,
//                             @Part("numberOfPages") int numberOfPages);

//    @POST("books")
//    Call<Book> SendBookToAPI(@Body Book book);
}
