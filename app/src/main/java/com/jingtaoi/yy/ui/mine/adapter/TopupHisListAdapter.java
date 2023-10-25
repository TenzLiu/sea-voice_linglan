package com.jingtaoi.yy.ui.mine.adapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.TopupHisBean;

public class TopupHisListAdapter extends BaseQuickAdapter<TopupHisBean.DataEntity, BaseViewHolder> {


    public TopupHisListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopupHisBean.DataEntity item) {

        RecyclerView mRecyclerView_show = helper.getView(R.id.mRecyclerView_show);
        TopupHisItemAdapter topupHisItemAdapter = new TopupHisItemAdapter(R.layout.item_gifthis);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView_show.setLayoutManager(layoutManager);
        mRecyclerView_show.setAdapter(topupHisItemAdapter);
        topupHisItemAdapter.setNewData(item.getRecharge());
    }
}
