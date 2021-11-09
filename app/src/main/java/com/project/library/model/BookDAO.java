package com.project.library.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDAO {
    @Query("SELECT * FROM Book ORDER BY bookId ASC")
    List<Book> GetListAllBook();

    @Query("SELECT * FROM Book WHERE bookId= :bookid ORDER BY bookId ASC")
    List<Book> GetListAllBookByBookId(String bookid);

    @Query("SELECT * FROM Book WHERE category= :category ORDER BY bookId ASC")
    List<Book> GetListAllBookByCategory(String category);

    //onConflict = OnConflictStrategy.REPLACE
    @Insert()
    void insert(Book... book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("DELETE FROM Book")
    void DeleteAll();
}
