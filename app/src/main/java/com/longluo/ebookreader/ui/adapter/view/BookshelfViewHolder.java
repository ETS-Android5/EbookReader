package com.longluo.ebookreader.ui.adapter.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.longluo.ebookreader.R;

public class BookshelfViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivBookCover;

    public TextView tvBookName;

//    public TextView tvBookType;

    public BookshelfViewHolder(View view) {
        super(view);
        ivBookCover = view.findViewById(R.id.iv_cover);
        tvBookName = view.findViewById(R.id.tv_name);
    }
}
