package com.project.library.controller;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.library.R;
import com.project.library.model.Book;

import java.util.List;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.BookViewHolder> {
    private List<Book> listBook;
    public ClickItemBookListener clickItemBookListener;

    public void setClickItemBookListener(ClickItemBookListener clickItemBookListener) {
        this.clickItemBookListener = clickItemBookListener;
    }

    public interface ClickItemBookListener{
        void onClickItemBook(Book book);
    }

    public BookRecyclerViewAdapter(List<Book> listBook) {
        this.listBook = listBook;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_home, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = listBook.get(position);
        if(book != null){
            Glide.with(holder.itemView.getContext()).load(book.getImg()).into(holder.iv_book);

            holder.tv_NameBook.setText(Html.fromHtml("<b>" + "NAME : " +"</b>" + "<i>" + book.getBookName() + "</i>"));
            holder.tv_Author.setText(Html.fromHtml("<b>" + "AUTHOR : " +"</b>" + "<i>" + book.getAuthor() + "</i>"));
            holder.rb_star.setRating(book.getStar());
            holder.tv_show_star.setText(Html.fromHtml("<b>" + "<i>" + String.valueOf(Math.round(book.getStar() * 100) / 100.0) + "</i"+ "</b>"));

            // ==================set màu cho star
            Drawable progress = holder.rb_star.getProgressDrawable();
            DrawableCompat.setTint(progress, Color.BLUE);
            //=================================

            holder.tv_show_available.setText(Html.fromHtml("<b>" + "<i>" + book.getAvailable() + "</i>" + "</b>"));

            holder.linearLayout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItemBookListener.onClickItemBook(book);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(listBook != null){
            return listBook.size();
        }
        return 0;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_book;
        private TextView tv_NameBook;
        private TextView tv_Author;
        private RatingBar rb_star;
        private TextView tv_show_star;
        private TextView tv_show_available;
        private LinearLayout linearLayout_item;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_book = itemView.findViewById(R.id.imv_avatar_book);
            tv_NameBook = itemView.findViewById(R.id.tv_nameBook);
            tv_Author = itemView.findViewById(R.id.tv_author);
            rb_star = itemView.findViewById(R.id.rb_star_book);
            tv_show_star = itemView.findViewById(R.id.tv_star);
            tv_show_available = itemView.findViewById(R.id.tv_available);
            linearLayout_item = itemView.findViewById(R.id.item_book);
        }
    }
}
