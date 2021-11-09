package com.project.library.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Book.class}, version = 1)
public abstract class BookDatabase extends RoomDatabase{

    private static BookDatabase instance;
    public static BookDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, BookDatabase.class, "Book").allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract BookDAO bookDAO();
}
