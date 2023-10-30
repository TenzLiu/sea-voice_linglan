package com.linglani.yy.ui.mine.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linglani.yy.R;
import com.linglani.yy.bean.ImgEntity;
import com.linglani.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.sinata.xldutils.utils.StringUtils;

public class PersonHomeImgListAdapter extends BaseQuickAdapter<ImgEntity, BaseViewHolder> {


    public PersonHomeImgListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImgEntity item) {
        SimpleDraweeView iv_show_onlines = helper.getView(R.id.iv_img_personhome);
        if (StringUtils.isEmpty(item.getUrl())) {
            ImageUtils.loadUri(iv_show_onlines,"");
            helper.getView(R.id.gp_add).setVisibility(View.VISIBLE);
        }else {
            ImageUtils.loadUri(iv_show_onlines, item.getUrl());
            helper.getView(R.id.gp_add).setVisibility(View.GONE);

        }
    }
}
