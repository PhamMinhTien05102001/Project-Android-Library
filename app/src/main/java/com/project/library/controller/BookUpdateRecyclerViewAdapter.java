package com.project.library.controller;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.library.R;
import com.project.library.model.Book;
import com.ramotion.foldingcell.FoldingCell;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

public class BookUpdateRecyclerViewAdapter extends RecyclerView.Adapter<BookUpdateRecyclerViewAdapter.BookViewHolder>{
    public List<Book> listBook;
    public List<Boolean> Show_Expand = new ArrayList<>();
    public OnClickButtonEditListener onClickButtonEditListener;

    public interface OnClickButtonEditListener{
        void EditBook(Book book);
    }

    public void setOnClickButtonEditListener(OnClickButtonEditListener onClickButtonEditListener) {
        this.onClickButtonEditListener = onClickButtonEditListener;
    }

    public BookUpdateRecyclerViewAdapter(List<Book> listBook) {
        this.listBook = listBook;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_update, parent, false);

        for(int i = 0; i <= getItemCount()+5; i++){
            Show_Expand.add(false);
        }

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // ==================set màu cho star
        Drawable progress = holder.rb_star.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.BLUE);
        //=================================

        Book book = listBook.get(position);

        holder.rb_star.setRating(book.getStar());
        Glide.with(holder.itemView.getContext()).load(book.getImg()).into(holder.iv_avatarBook);
        holder.tv_name.setText(Html.fromHtml("<b>" + "NAME : " +"</b>" + "<i>" + book.getBookName() + "</i>"));
        holder.tv_author.setText(Html.fromHtml("<b>" + "AUTHOR : " +"</b>" + "<i>" + book.getAuthor() + "</i>"));
        holder.tv_bookID.setText(Html.fromHtml("<b>" + "BookID : " +"</b>" + "<i>" + book.getBookId() + "</i>"));
        holder.tv_category.setText(Html.fromHtml("<b>" + "Category : " +"</b>" + "<i>" + book.getCategory() + "</i>"));
        holder.tv_available.setText(Html.fromHtml("<b>" + "Available : " +"</b>" + "<i>" + book.getAvailable() + "</i>"));
        holder.tv_page.setText(Html.fromHtml("<b>" + "Page : " +"</b>" + "<i>" + book.getNumberOfPages() + "</i>"));
        holder.tv_description.setText(Html.fromHtml("<b>" + "Description : " +"</b>" + "<i>" + book.getDescription() + "</i>"));

        holder.ll_expand.setVisibility(Show_Expand.get(position) ? View.VISIBLE : View.GONE);

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButtonEditListener.EditBook(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listBook != null)    return listBook.size();
        return 0;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name, tv_author, tv_category, tv_description, tv_available, tv_page, tv_bookID;
        private RatingBar rb_star;
        private ImageView iv_avatarBook;
        private LinearLayout ll_all, ll_expand;
        private ImageButton btn_edit;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_bookID = itemView.findViewById(R.id.tv_update_bookID);
            tv_name = itemView.findViewById(R.id.tv_update_nameBook);
            tv_author = itemView.findViewById(R.id.tv_update_author);
            tv_category = itemView.findViewById(R.id.tv_update_category);
            tv_description = itemView.findViewById(R.id.tv_update_description);
            tv_available = itemView.findViewById(R.id.tv_update_available);
            tv_page = itemView.findViewById(R.id.tv_update_page);
            rb_star = itemView.findViewById(R.id.rb_update_star);
            iv_avatarBook = itemView.findViewById(R.id.iv_update_avatar_book);
            ll_all = itemView.findViewById(R.id.linearlayout_all);
            ll_expand = itemView.findViewById(R.id.linearlayout_expandable);
            btn_edit = itemView.findViewById(R.id.btn_update_edit);

            ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Show_Expand.set(getAdapterPosition(), !Show_Expand.get(getAdapterPosition()));
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}
