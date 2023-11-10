package com.tiantian.yy.ui.room.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiantian.yy.R;
import com.tiantian.yy.bean.ReceivePacketBean;

public class ReceivePacketListAdapter extends BaseQuickAdapter<ReceivePacketBean.DataBean, BaseViewHolder> {


    public ReceivePacketListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReceivePacketBean.DataBean item) {

        helper.setText(R.id.tv_name_receive, item.getName());
        helper.setText(R.id.tv_time_receive, item.getCreateTime());

    }
}
