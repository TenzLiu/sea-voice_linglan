package com.tiantian.yy.ui.message.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiantian.yy.R;
import com.tiantian.yy.bean.MyattentionBean;
import com.tiantian.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class FriendListAdapter extends BaseQuickAdapter<MyattentionBean.DataEntity, BaseViewHolder> {


    public FriendListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyattentionBean.DataEntity item) {
        SimpleDraweeView iv_show_friend = helper.getView(R.id.iv_show_friend);
        TextView tv_name_friend = helper.getView(R.id.tv_name_friend);
        ImageUtils.loadUri(iv_show_friend, item.getImg());
        tv_name_friend.setText(item.getName());

        helper.addOnClickListener(R.id.iv_show_friend);
    }
}
