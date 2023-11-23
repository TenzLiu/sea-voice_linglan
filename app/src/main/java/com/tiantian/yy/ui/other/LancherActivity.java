package com.tiantian.yy.ui.other;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;

import com.tiantian.yy.bean.VersionBean;
import com.tiantian.yy.dialog.MyDialog;
import com.tiantian.yy.netUtls.Api;
import com.tiantian.yy.netUtls.HttpManager;
import com.tiantian.yy.netUtls.MyObserver;
import com.tiantian.yy.utils.LogUtils;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tiantian.yy.R;
import com.tiantian.yy.base.MyBaseActivity;
import com.tiantian.yy.utils.ActivityCollector;
import com.tiantian.yy.utils.Const;
import com.tiantian.yy.utils.MyUtils;
import com.tiantian.yy.utils.SharedPreferenceUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;


import java.lang.ref.WeakReference;
import java.util.HashMap;

import cn.sinata.xldutils.utils.StringUtils;
import cn.sinata.xldutils.utils.Utils;
import io.reactivex.functions.Consumer;

/**
 * 启动页面
 * Created by Administrator on 2018/10/10.
 */

public class LancherActivity extends MyBaseActivity {
    MyHandler myHandler;

    @Override
    public void initData() {
        initClinkApiCall();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_lancher);
    }

    @Override
    public void initView() {
        showHeader(false);
        showTitle(false);
        setRxpermiss();
    }

    /**
     * 初始化clinkapicall
     */
    cn.ay.clinkapi.Api clinkApi = null;
    private void initClinkApiCall(){
        String key = "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";
        clinkApi = new cn.ay.clinkapi.Api();
        //启动(只需要调用一次，重复调用也不会出错，调用时内部会判断是否已启动过，如果已启动过直接返回第一次调用的结果。调用时因客户端网络不通返回了0，过了段时间网络正常后我们会自动重连，这时再调用会返回150，这时如果没有调用业务也可正常运行)。
        //返回150表示成功，其它的值均为失败。150：成功、0：网络不通、1：已外部停止(如调用了停止函数，该函数一般不需要调用)、2：已内部停止(如运行过程中密钥被删除等)、170：实例到期或密钥不存在
        int ret = clinkApi.start(key);
    }

    @SuppressLint("CheckResult")
    private void setRxpermiss() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            myHandler = new MyHandler(LancherActivity.this);
                            myHandler.sendEmptyMessageDelayed(0, 2000);
                        } else {
                            showMyDialog();
                        }
                    }
                });
    }

    private void showMyDialog() {
        MyDialog myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setHintText("请在应用权限页面开启相关权限");
        myDialog.setLeftButton("退出应用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
            }
        });
        myDialog.setRightButton("前往设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                MyUtils.getInstans().gotoAppDetailIntent(LancherActivity.this, 101);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
//                toNextActivity();
                getCall();
            }
        }
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("state", Const.IntShow.ONE);
        HttpManager.getInstance().post(Api.getVersions, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                VersionBean versionBean = new Gson().fromJson(responseString, VersionBean.class);
                String appVersion = Utils.getAppVersion(LancherActivity.this);
                if (versionBean.getCode() == 0) {
                    if (versionBean.getData().getVersions().equals(appVersion)) {
                        toNextActivity();
                    } else {
                        showVersionDialog(versionBean.getData());
                    }
                } else {
                    toNextActivity();
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                showVersionDialog("连接失败，请重试");
            }
        });
    }

    MyDialog myDialog;

    private void showVersionDialog(VersionBean.DataEntity data) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setCancelable(false);
        if (data.getState() == 1) {
            myDialog.setHintText("检测到有最新版本是否更新");
        } else if (data.getState() == 2) {
            myDialog.setHintText("检测到有最新版本请前往更新");
            myDialog.setLeftText("退出应用");
        }
        myDialog.setRightButton("前往更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                MyUtils.getInstans().toWebView(LancherActivity.this, data.getUrl());
            }
        });
        myDialog.setLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                if (data.getState() == 1) {
                    toNextActivity();
                } else if (data.getState() == 2) { //强制更新
                    ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
                }
            }
        });
    }

    private void showVersionDialog(String msgShow) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setHintText(msgShow);
        myDialog.setLeftButton("退出应用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
            }
        });

        myDialog.setRightButton("重新获取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                getCall();
            }
        });
    }

    private void toNextActivity() {
        String uuid = (String) SharedPreferenceUtils.get(this, Const.User.UUID, "");
        if (StringUtils.isEmpty(uuid)) {
            SharedPreferenceUtils.put(this, Const.User.UUID, MyUtils.getInstans().getUuid(this));
        }
        if (userToken <= 0) {
            toLogin();
        } else {
            String userSig = (String) SharedPreferenceUtils.get(this, Const.User.USER_SIG, "");
            onRecvUserSig(String.valueOf(userToken), userSig);
        }
    }

    private void toMain() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Const.ShowIntent.STATE, true);
        ActivityCollector.getActivityCollector().toOtherActivity(AdvertActivity.class, bundle);
        ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
    }

    private void toLogin() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Const.ShowIntent.STATE, false);
        ActivityCollector.getActivityCollector().toOtherActivity(AdvertActivity.class, bundle);
        ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
    }

    private void onRecvUserSig(String userId, String userSig) {
//        showDialog();
        TUIKit.login(userId, userSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                /**
                 * IM 登录成功后的回调操作，一般为跳转到应用的主页（这里的主页内容为下面章节的会话列表）
                 */
                LogUtils.e(LogUtils.TAG, "登录腾讯云成功");
                dismissDialog();
                toMain();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e(LogUtils.TAG, errCode + "登录腾讯云失败" + errMsg);
                dismissDialog();
                if (errCode == 6208) { //被踢
                    showToast("您已在其他设备登录，请重新登录");
                } else if (errCode == 70001) { //用户票据过期
                    showToast("登录授权过时，请重新登录");
                }
                toLogin();
            }
        });
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            setRxpermiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }
}
