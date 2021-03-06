package com.example.grey.widget;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PostItemDecoration extends RecyclerView.ItemDecoration{
    private int space;

    public PostItemDecoration(int space){
        this.space=space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left=space;
        outRect.right=space;
        outRect.bottom=space;

        if (parent.getChildPosition(view)==0)
            outRect.top=space;
    }
}
