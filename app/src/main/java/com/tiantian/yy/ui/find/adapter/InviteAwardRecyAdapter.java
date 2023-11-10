package com.tiantian.yy.ui.find.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiantian.yy.R;
import com.tiantian.yy.bean.InviteAwardBean;
import com.tiantian.yy.utils.MyUtils;

public class InviteAwardRecyAdapter extends BaseQuickAdapter<InviteAwardBean.DataEntity.DivideListEntity, BaseViewHolder> {

    public InviteAwardRecyAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, InviteAwardBean.DataEntity.DivideListEntity item) {
        helper.setText(R.id.tv_name_sharehis, item.getName());
        helper.setText(R.id.tv_time_sharehis, item.getCreateTime());
        helper.setText(R.id.tv_money_sharehis, "+" + MyUtils.getInstans().doubleTwo(item.getMoney()) + "元");
    }
}
