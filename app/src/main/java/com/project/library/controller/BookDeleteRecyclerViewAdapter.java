package com.project.library.controller;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.project.library.R;
import com.project.library.model.Book;
import com.project.library.model.BookDatabase;
import com.project.library.model.ServiceAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDeleteRecyclerViewAdapter extends RecyclerView.Adapter<BookDeleteRecyclerViewAdapter.BookViewHolder> {
    private List<Book> listBook;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    public OnClickButtonDeleteListener onClickButtonDeleteListener;
    public OnClickButtonInfoListener onClickButtonInfoListener;

    public interface OnClickButtonDeleteListener{
        void DeleteBook(Book book, int position);
    }

    public interface OnClickButtonInfoListener{
        void InfoBook(Book book);
    }

    public void setOnClickButtonDeleteListener(OnClickButtonDeleteListener onClickButtonDeleteListener) {
        this.onClickButtonDeleteListener = onClickButtonDeleteListener;
    }

    public void setOnClickButtonInfoListener(OnClickButtonInfoListener onClickButtonInfoListener) {
        this.onClickButtonInfoListener = onClickButtonInfoListener;
    }

    public BookDeleteRecyclerViewAdapter(List<Book> listBook) {
        this.listBook = listBook;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_delete, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = listBook.get(position);

        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(book.getBookId()));
        holder.tv_BookID.setText(Html.fromHtml("<b>" + "BOOKID : " +"</b>" + "<i>" + book.getBookId() + "</i>"));
        holder.tv_Name.setText(Html.fromHtml("<b>" + "NAME : " +"</b>" + "<i>" + book.getBookName() + "</i>"));
        holder.tv_Author.setText(Html.fromHtml("<b>" + "AUTHOR : " +"</b>" + "<i>" + book.getAuthor() + "</i>"));
        Glide.with(holder.itemView.getContext()).load(book.getImg()).into(holder.iv_avatar_book);

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButtonDeleteListener.DeleteBook(book, holder.getAdapterPosition());
            }
        });

        holder.btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButtonInfoListener.InfoBook(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listBook != null){
            return listBook.size();
        }
        return 0;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder{
        private SwipeRevealLayout swipeRevealLayout;
        private ImageButton btn_delete, btn_info;
        private TextView tv_Name, tv_Author, tv_BookID;
        private ImageView iv_avatar_book;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.swipereveallayout);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_info = itemView.findViewById(R.id.btn_info);
            tv_BookID = itemView.findViewById(R.id.tv_delete_bookID);
            tv_Name = itemView.findViewById(R.id.tv_delete_nameBook);
            tv_Author = itemView.findViewById(R.id.tv_delete_author);
            iv_avatar_book = itemView.findViewById(R.id.iv_delete_avatar_book);
        }
    }
}
