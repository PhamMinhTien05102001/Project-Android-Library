package com.project.library.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookCallAPI {

    private String status;

    private int results;

    //@SerializedName("data")
    private List<Book> data;

    public BookCallAPI() {
    }

    public BookCallAPI(String status, int results, List<Book> books) {
        this.status = status;
        this.results = results;
        data = books;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public List<Book> getBooks() {
        return data;
    }

    public void setBooks(List<Book> books) {
        data = books;
    }
}
