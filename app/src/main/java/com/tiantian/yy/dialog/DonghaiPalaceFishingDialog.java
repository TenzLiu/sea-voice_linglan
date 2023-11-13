package com.tiantian.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tiantian.yy.R;
import com.tiantian.yy.bean.ChestOpenBean;
import com.tiantian.yy.bean.DoubleBean;
import com.tiantian.yy.bean.FishBean;
import com.tiantian.yy.bean.MicXgfjBean;
import com.tiantian.yy.bean.UserBean;
import com.tiantian.yy.netUtls.Api;
import com.tiantian.yy.netUtls.HttpManager;
import com.tiantian.yy.netUtls.MyObserver;
import com.tiantian.yy.ui.mine.WalletActivity;
import com.tiantian.yy.ui.room.dialog.DanJiangDialog;
import com.tiantian.yy.ui.room.dialog.DanJiangRealtimeDialog;
import com.tiantian.yy.ui.room.dialog.DanRankDialog;
import com.tiantian.yy.ui.room.dialog.ExplainDialog;
import com.tiantian.yy.utils.ActivityCollector;
import com.tiantian.yy.utils.Const;
import com.tiantian.yy.utils.SharedPreferenceUtils;
import com.tiantian.yy.view.FrameAnimation;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 东海龙宫弹窗(金宝箱)
 */

public class DonghaiPalaceFishingDialog extends Dialog implements FrameAnimation.AnimationListener {

    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;
    @BindView(R.id.ll_coin)
    LinearLayout ll_coin;
    @BindView(R.id.tv_gold)
    TextView tvGold;
    @BindView(R.id.rv_count1)
    TextView rvCount1;
    @BindView(R.id.rv_count10)
    TextView rvCount10;
    @BindView(R.id.rv_count100)
    TextView rvCount100;
    @BindView(R.id.iv_skip)
    ImageView iv_skip;
    @BindView(R.id.iv_treasure)
    ImageView iv_treasure;
    @BindView(R.id.iv_anim)
    ImageView iv_anim;
    @BindView(R.id.iv_effect)
    ImageView iv_effect;
    private Map<Integer, Integer> fishBeanViewMap = new HashMap<>();

    private List<FishBean> fishBeans = new ArrayList<>();
    int userId;
    Activity activity;

    String roomId;
    ChestOpenBean.DataBean data;
    private int normalReal = 0;//普通奖池实时数据
    private int deepReal = 0;//深海奖池实时数据
    private int chooseOne;//

    boolean isSkipAnima = false;

    public DonghaiPalaceFishingDialog(Activity activity, int userId, String roomId) {
        super(activity, R.style.CustomDialogStyle);
        this.activity = activity;
        this.userId = userId;
        this.roomId = roomId;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_donghai_palace_fishing);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (ScreenUtil.getScreenWidth(activity));
        lp.height = (int) (ScreenUtil.getScreenHeight(activity) * 0.6);
        getWindow().setAttributes(lp);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });

        initFishing();
        getCall();
//        getRealtimeCall();
    }

    private void initFishing() {
        fishBeanViewMap.put(88888, R.mipmap.ic_donghai_palace_fishing_01);
        fishBeanViewMap.put(199, R.mipmap.ic_donghai_palace_fishing_09);
        fishBeanViewMap.put(9999, R.mipmap.ic_donghai_palace_fishing_05);
        fishBeanViewMap.put(15555, R.mipmap.ic_donghai_palace_fishing_04);
        fishBeanViewMap.put(28888, R.mipmap.ic_donghai_palace_fishing_03);
        fishBeanViewMap.put(66, R.mipmap.ic_donghai_palace_fishing_08);
        fishBeanViewMap.put(333, R.mipmap.ic_donghai_palace_fishing_07);
        fishBeanViewMap.put(4999, R.mipmap.ic_donghai_palace_fishing_11);
        fishBeanViewMap.put(999, R.mipmap.ic_donghai_palace_fishing_12);
        fishBeanViewMap.put(66666, R.mipmap.ic_donghai_palace_fishing_02);
        fishBeanViewMap.put(1999, R.mipmap.ic_donghai_palace_fishing_13);
        fishBeanViewMap.put(10, R.mipmap.ic_donghai_palace_fishing_10);
        fishBeanViewMap.put(666, R.mipmap.ic_donghai_palace_fishing_06);
        isSkipAnima = (boolean) SharedPreferenceUtils.get(getContext(), Const.User.IS_DONG_HAI_SKIP, false);
        if (isSkipAnima) {
            iv_skip.setImageResource(R.mipmap.icon_skip_off);
        } else {
            iv_skip.setImageResource(R.mipmap.icon_skip_on);
        }
    }


    private void getCall() {
        int userToken = (int) SharedPreferenceUtils.get(getContext(), Const.User.USER_TOKEN, 0);

        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.getUserInfo, map, new MyObserver(getContext()) {
            @Override
            public void success(String responseString) {
                UserBean userBean = JSON.parseObject(responseString, UserBean.class);
                if (userBean.getCode() == 0) {
                    if (userBean.getData() != null) {
                        SharedPreferenceUtils.put(activity, Const.User.GOLD, userBean.getData().getGold());
                        tvGold.setText(userBean.getData().getGold() + "");
                    }
                }
            }
        });

    }

    /**
     * 获取实时奖池
     */
    private void getRealtimeCall() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("type", 1);
        HttpManager.getInstance().post(Api.jackpotSize, params, new MyObserver(activity) {
            @Override
            public void success(String responseString) {
                DoubleBean getOneBean = JSON.parseObject(responseString, DoubleBean.class);
                if (getOneBean.code == Api.SUCCESS) {
                    int data = (int) getOneBean.data;
                    deepReal = data;
                    String s = deepReal + "";
                } else {
                    showToast(getOneBean.msg);
                }
            }
        });
    }

    @OnClick({R.id.iv_close, R.id.tv_gold, R.id.rv_skip, R.id.ll_coin,
            R.id.iv_jackpot, R.id.iv_record, R.id.iv_method, R.id.iv_rankList, R.id.rv_count1, R.id.rv_count10, R.id.rv_count100})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_jackpot:
                showDanJiangDialog();
                break;
            case R.id.iv_method:
                showExplainDialog();
                break;
            case R.id.iv_record:
                showBottom(2);
                break;
            case R.id.iv_rankList:
                showRankDialog();
                break;
            case R.id.rv_count1:
                chooseOne = 1;
                setZaClicker();
                break;
            case R.id.rv_count10:
                chooseOne = 2;
                setZaClicker();
                break;
            case R.id.rv_count100:
                chooseOne = 3;
                setZaClicker();
                break;
            case R.id.ll_coin:
            case R.id.tv_gold:
                ActivityCollector.getActivityCollector().toOtherActivity(WalletActivity.class);
                break;
            case R.id.rv_skip:
                isSkipAnima = !isSkipAnima;
                if (isSkipAnima) {
                    iv_skip.setImageResource(R.mipmap.icon_skip_off);
                } else {
                    iv_skip.setImageResource(R.mipmap.icon_skip_on);
                }
                SharedPreferenceUtils.put(getContext(), Const.User.IS_DONG_HAI_SKIP, isSkipAnima);
                break;
        }
    }

    private void showDanJiangDialog() {
        DanJiangDialog danJiangDialog = new DanJiangDialog(activity, 1, fishBeanViewMap);
        danJiangDialog.show();
    }

    private void showDanJiangRealtimeDialog() {
        DanJiangRealtimeDialog danJiangDialog = new DanJiangRealtimeDialog(activity, 1);
        danJiangDialog.show();
    }

    private void showRankDialog() {
        DanRankDialog danRankDialog = new DanRankDialog(activity, userId, 1);
        danRankDialog.show();

    }

    private void showExplainDialog() {
        ExplainDialog explainDialog = new ExplainDialog(activity, 1);
        explainDialog.show();
    }

    public void setZaClicker() {
        setCountEnabled(false);
        getOpenCall();
    }

    MyBottomShow1Dialog myBottomShow1Dialog;

    private void showBottom(int i) {
        if (myBottomShow1Dialog != null && myBottomShow1Dialog.isShowing()) {
            myBottomShow1Dialog.dismiss();
        }
        myBottomShow1Dialog = new MyBottomShow1Dialog(activity, i, userId, 1);
        myBottomShow1Dialog.show();
    }


    public void loadResultSvga(String imgShow) {
    }


    private void getOpenCall() {
        int number = 1;
        if (chooseOne == 1) {
            number = 1;
        } else if (chooseOne == 2) {
            number = 10;
        } else if (chooseOne == 3) {
            number = 100;
        }
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("num", number);
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.GetLottery, map, new MyObserver(activity) {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String responseString) {
                ChestOpenBean chestOpenBean = JSON.parseObject(responseString, ChestOpenBean.class);
                if (chestOpenBean.getCode() == Api.SUCCESS) {
                    data = chestOpenBean.getData();//跳过动画
                    boolean isValuable = false;
                    for (ChestOpenBean.DataBean.LotteryBean bean : data.getLottery()) {
                        if (bean.getGold() >=4000) {
                            isValuable = true;
                            break;
                        }
                    }
                    //跳过动画
                    if (isSkipAnima) {
                        if(isValuable){
                            playValuableEffect(chestOpenBean);
                        }else{
                            showChestResult(chestOpenBean);
                        }
                    } else {
                        if(isValuable){
                            playValuableEffect(chestOpenBean);
                        }else{
                            playAnim(chestOpenBean);
                        }
                    }

                } else {
                    setCountEnabled(true);
                    showToast(chestOpenBean.getMsg());
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                setCountEnabled(true);
            }
        });
    }

    /**
     * 播放动画
     * @param chestOpenBean
     */
    private void playAnim(ChestOpenBean chestOpenBean){
        //webp动图
        iv_treasure.setVisibility(View.GONE);
        iv_anim.setVisibility(View.VISIBLE);
        Transformation<Bitmap> transformation = new CenterInside();
        Glide.with(activity)
                .load(R.mipmap.donghaifishing)//不是本地资源就改为url即可
                .optionalTransform(transformation)
                .optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(transformation))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        WebpDrawable webpDrawable = (WebpDrawable) resource;
                        //需要设置为循环1次才会有onAnimationEnd回调
                        webpDrawable.setLoopCount(1);
                        webpDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationStart(Drawable drawable) {
                                super.onAnimationStart(drawable);
                            }

                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                webpDrawable.unregisterAnimationCallback(this);
                                iv_treasure.setVisibility(View.VISIBLE);
                                iv_anim.setVisibility(View.GONE);
                                showChestResult(chestOpenBean);
                            }
                        });

                        return false;
                    }
                })
                .into(iv_anim);
    }

    /**
     * 播放动画
     * @param chestOpenBean
     */
    private void playValuableEffect(ChestOpenBean chestOpenBean){
        //webp动图
        iv_treasure.setVisibility(View.GONE);
        iv_effect.setVisibility(View.VISIBLE);
        Transformation<Bitmap> transformation = new CenterInside();
        Glide.with(activity)
                .load(R.mipmap.effect_gift)//不是本地资源就改为url即可
                .optionalTransform(transformation)
                .optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(transformation))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        WebpDrawable webpDrawable = (WebpDrawable) resource;
                        //需要设置为循环1次才会有onAnimationEnd回调
                        webpDrawable.setLoopCount(1);
                        webpDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationStart(Drawable drawable) {
                                super.onAnimationStart(drawable);
                            }

                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                webpDrawable.unregisterAnimationCallback(this);
                                iv_treasure.setVisibility(View.VISIBLE);
                                iv_effect.setVisibility(View.GONE);
                                showChestResult(chestOpenBean);
                            }
                        });

                        return false;
                    }
                })
                .into(iv_effect);
    }

    private void showChestResult(ChestOpenBean chestOpenBean) {
        setCountEnabled(true);
        int goldShow = chestOpenBean.getData().getUser().getGold();
        SharedPreferenceUtils.put(activity, Const.User.GOLD, goldShow);
        tvGold.setText(goldShow + "");
        if (sendChestsMsg != null) {
            showChestsShowDialog(data, chooseOne);
        }

    }

    private void setCountEnabled(boolean enabled) {
        rvCount1.setEnabled(enabled);
        rvCount10.setEnabled(enabled);
        rvCount100.setEnabled(enabled);
    }

    /**
     * 开箱探险结果页面
     *
     * @param data
     */
    private MyChestsShowDialog myChestsShowDialog;

    private void showChestsShowDialog(ChestOpenBean.DataBean data, int chooseOne) {
        if (myChestsShowDialog != null && myChestsShowDialog.isShowing()) {
            myChestsShowDialog.dismiss();
        }
        myChestsShowDialog = new MyChestsShowDialog(activity, data, chooseOne);
        myChestsShowDialog.show();
        myChestsShowDialog.setCancel(v -> {// 取消
            if (myChestsShowDialog != null && myChestsShowDialog.isShowing()) {
                myChestsShowDialog.dismiss();
            }
        });
        myChestsShowDialog.setButton(v -> {//再来一次
            if (myChestsShowDialog != null && myChestsShowDialog.isShowing()) {
                myChestsShowDialog.dismiss();
            }
            setZaClicker();
        });


        for (MicXgfjBean.DataBean.ListDataBean listDataBean : data.getMsgList()) {
            try {
//                WinPrizeGiftModel giftAllModel = new Gson().fromJson(listDataBean.getMessageChannelSend(), WinPrizeGiftModel.class);
                sendChestsMsg.onSendChestsMsg(listDataBean.getMessageChannelSend());
//                String txIMChannelMsg = listDataBean.getMessageChannelSend();
//                WinPrizeGiftModel giftAllModel = new Gson().fromJson(txIMChannelMsg, WinPrizeGiftModel.class);
//                List<String> msgRids = giftAllModel.getData().getRids();
//                if (msgRids!=null && msgRids.size()>0 && msgRids.contains(roomId)){
//                    sendChestsMsg.onSendChestsMsg(txIMChannelMsg);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onAnimationStart() {

    }

    @Override
    public void onAnimationEnd() {

    }

    @Override
    public void onAnimationRepeat() {

    }

    public interface SendChestsMsg {
        void onSendChestsMsg(String msg);
    }

    private SendChestsMsg sendChestsMsg;

    public void setSendChestsMsg(SendChestsMsg sendChestsMsg) {
        this.sendChestsMsg = sendChestsMsg;
    }

}