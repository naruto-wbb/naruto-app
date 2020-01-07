package com.naruto.road;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.naruto.core.base.BaseApplicationKt;

public class FixLinearLayoutManager extends LinearLayoutManager {
    public FixLinearLayoutManager(Context context) {
        super(context);
    }

    public FixLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public FixLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //try catch一下
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }


    protected void setEmptyView(int layoutId, BaseQuickAdapter mAdapter, String text) {
        try {
            final View errorView = LayoutInflater.from(BaseApplicationKt.getApp()).inflate(layoutId, null);
            if (!TextUtils.isEmpty(text)) { //不为空则替换文案
                final TextView tv_empty_hint = errorView.findViewById(0);
                tv_empty_hint.setText(text);
            }
            final View iv_empty = errorView.findViewById(0);
            if (iv_empty instanceof ImageView) {
                final ImageView ivEmpty = (ImageView) iv_empty;
                ivEmpty.setImageResource(layoutId);
            }
            mAdapter.setEmptyView(errorView);
        } catch (OutOfMemoryError e) {

        } catch (Exception e) {

        }
    }
}
