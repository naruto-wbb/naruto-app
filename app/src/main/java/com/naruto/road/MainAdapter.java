package com.naruto.road;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.naruto.road.model.bean.Main;

import java.util.List;

public class MainAdapter extends BaseMultiItemQuickAdapter<Main, BaseViewHolder> {

    public MainAdapter(List<Main> data) {
        super(data);

        addItemType(0, R.layout.view_title_bar);
        addItemType(1, R.layout.view_title_bar);
    }

    @Override
    protected void convert(BaseViewHolder helper, Main item) {
        if (helper.getItemViewType() == 0) {

        } else if (helper.getItemViewType() == 1) {

        }
    }
}