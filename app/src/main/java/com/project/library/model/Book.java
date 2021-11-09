package com.project.library.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.jar.Attributes;

@Entity
public class Book implements Serializable {
    @NonNull
    @PrimaryKey
    private String bookId;

    private String _id;

    private String bookName;

    private String author;

    private int available;

    private String category;

    private String img;

    private String description;

    private float star;

    private int numberOfPages;

    public Book(){

    }

    public Book(@NonNull String bookId, String _id, String bookName, String author, int available, String category, String img, String description, float star, int numberOfPages) {
        this.bookId = bookId;
        this._id = _id;
        this.bookName = bookName;
        this.author = author;
        this.available = available;
        this.category = category;
        this.img = img;
        this.description = description;
        this.star = star;
        this.numberOfPages = numberOfPages;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookID) {
        this.bookId = bookID;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", available=" + available +
                ", category='" + category + '\'' +
                ", img='" + img + '\'' +
                ", description='" + description + '\'' +
                ", star=" + star +
                ", numberOfPages=" + numberOfPages +
                '}';
    }
}
