package com.tiantian.yy.view.wheelsruflibrary.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.tiantian.yy.R;
import com.tiantian.yy.utils.MyUtils;
import com.tiantian.yy.view.wheelsruflibrary.listener.RotateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cretin on 2017/12/26.
 */

public class WheelSurfPanView extends View {
    private Context mContext;
    //记录视图的大小
    private int mWidth;
    //记录当前有几个分类
    private Paint mPaint;
    //文字画笔
    private Paint mTextPaint;
    //圆环图片
    private Bitmap mYuanHuan;
    //大图片
    private Bitmap mMain;

    //中心点横坐标
    private int mCenter;
    //绘制扇形的半径 减掉50是为了防止边界溢出  具体效果你自己注释掉-50自己测试
    private int mRadius;
    //每一个扇形的角度
    private float mAngle;

    private List<Bitmap> mListBitmap;

    //动画回调监听
    private RotateListener rotateListener;

    public RotateListener getRotateListener() {
        return rotateListener;
    }

    public void setRotateListener(RotateListener rotateListener) {
        this.rotateListener = rotateListener;
    }

    public WheelSurfPanView(Context context) {
        super(context);
        init(context, null);
    }

    public WheelSurfPanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WheelSurfPanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //当前类型 1 自定义模式 2 暴力模式
    private int mType;
    //最低圈数 默认值3 也就是说每次旋转都会最少转3圈
    private int mMinTimes;
    //分类数量 如果数量为负数  通过代码设置样式
    private int mTypeNum = 6;
    //每个扇形旋转的时间
    private int mVarTime = 200;
    //文字描述集合
    private String[] mDeses;
    //自定义图标集合
    private Integer[] mIcons;
    //背景颜色
    private Integer[] mColors;
    //整个旋转图的背景 只有类型为2时才需要
    private Integer mMainImgRes;
    //圆环的图片引用
    private Integer mHuanImgRes;
    //背景圆环
    private Bitmap mBgHuanImgRes;
    //文字大小
    private float mTextSize;
    //文字颜色
    private int mTextColor;

    private boolean mIsOver = false;

    public void setmType(int mType) {
        this.mType = mType;
    }

    public void setmMinTimes(int mMinTimes) {
        this.mMinTimes = mMinTimes;
    }

    public void setmIsOver(boolean mIsOver) {
        this.mIsOver = mIsOver;
    }

    public void setmVarTime(int mVarTime) {
        this.mVarTime = mVarTime;
    }

    public void setmTypeNum(int mTypeNum) {
        this.mTypeNum = mTypeNum;
    }

    public void setmDeses(String[] mDeses) {
        this.mDeses = mDeses;
    }

    public void setmIcons(List<Bitmap> mIcons) {
        List<Bitmap> icons = new ArrayList<>();
        for (Bitmap list : mIcons) {
            icons.add(centerCrop(list,MyUtils.getInstans().dp2px(mContext,30),MyUtils.getInstans().dp2px(mContext,30)));
        }
        this.mListBitmap = icons;
//        this.mListBitmap = mIcons;
    }

    public void setmColors(Integer[] mColors) {
        this.mColors = mColors;
    }

    public void setmMainImgRes(Integer mMainImgRes) {
        this.mMainImgRes = mMainImgRes;
    }

    public void setmHuanImgRes(Integer mHuanImgRes) {
        this.mHuanImgRes = mHuanImgRes;
    }

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        setBackgroundColor(Color.TRANSPARENT);

        if (attrs != null) {
            //获得这个控件对应的属性。
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WheelSurfPanView);
            try {
                mType = typedArray.getInteger(R.styleable.WheelSurfPanView_type, 1);
                mVarTime = typedArray.getInteger(R.styleable.WheelSurfPanView_vartime, 0);
                mMinTimes = typedArray.getInteger(R.styleable.WheelSurfPanView_minTimes, 7);
                mTypeNum = typedArray.getInteger(R.styleable.WheelSurfPanView_typenum, 12);

                if (mTypeNum == -1) {
                    //用代码去配置这些参数
                } else if (mTypeNum == 12) {
                    mMain = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.bg_lottery_wheel_center);

                    mAngle = (float) (360.0 / 12);

                    mTextPaint = new Paint();
                    //设置填充样式
                    mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    //字符间距
                    mTextPaint.setLetterSpacing(0.4f);
                    //粗体
                    mTextPaint.setFakeBoldText(true);
                    //设置抗锯齿
                    mTextPaint.setAntiAlias(false);
                    //设置边界模糊
                    mTextPaint.setDither(true);
                    //设置画笔颜色
                    mTextPaint.setColor(Color.parseColor("#FF003F"));
                    //设置字体大小
                    mTextSize = typedArray.getDimension(R.styleable.WheelSurfPanView_mTextSize, 10 * getScale());

                    mTextPaint.setTextSize(mTextSize);

                    mBgHuanImgRes = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_lottery_bg_2);

                } else {
                    if (mVarTime == 0)
                        mVarTime = 75;

                    if (mTypeNum == 0)
                        throw new RuntimeException("找不到分类数量mTypeNum");

                    //每一个扇形的角度
                    mAngle = (float) (360.0 / mTypeNum);

                    if (mType == 1) {

                        mMain = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.bg_lottery_wheel_center);

//                        mHuanImgRes = typedArray.getResourceId(R.styleable.WheelSurfPanView_huanImg, 0);
////                        if ( mHuanImgRes == 0 )
////                            mYuanHuan = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.yuanhuan);
////                        else {
////                            mYuanHuan = BitmapFactory.decodeResource(mContext.getResources(), mHuanImgRes);
////                        }
//
//                        //文字大小
                        mTextSize = typedArray.getDimension(R.styleable.WheelSurfPanView_mTextSize, 12 * getScale());
//                        //文字颜色 默认粉红色
                        mTextColor = typedArray.getColor(R.styleable.WheelSurfPanView_mTextColor, Color.parseColor("#7651F1"));
//
//                        //描述
//                        int nameArray = typedArray.getResourceId(R.styleable.WheelSurfPanView_deses, -1);
//                        if ( nameArray == -1 ) throw new RuntimeException("找不到描述");
//                        mDeses = context.getResources().getStringArray(nameArray);
//                        //图片
//                        int iconArray = typedArray.getResourceId(R.styleable.WheelSurfPanView_icons, -1);
//                        if ( iconArray == -1 ) throw new RuntimeException("找不到分类的图片资源");
//                        String[] iconStrs = context.getResources().getStringArray(iconArray);
//                        List<Integer> iconLists = new ArrayList<>();
//                        for ( int i = 0; i < iconStrs.length; i++ ) {
//                            iconLists.add(context.getResources().getIdentifier(iconStrs[i], "mipmap", context.getPackageName()));
//                        }
//                        mIcons = iconLists.toArray(new Integer[iconLists.size()]);
//                        //颜色
//                        int colorArray = typedArray.getResourceId(R.styleable.WheelSurfPanView_colors, -1);
//                        if ( colorArray == -1 ) throw new RuntimeException("找不到背景颜色");
//                        String[] colorStrs = context.getResources().getStringArray(colorArray);
//                        if ( mDeses == null || mIcons == null || colorStrs == null )
//                            throw new RuntimeException("找不到描述或图片或背景颜色资源");
//                        if ( mDeses.length != mTypeNum || mIcons.length != mTypeNum || colorStrs.length != mTypeNum )
//                            throw new RuntimeException("资源或描述或背景颜色的长度和mTypeNum不一致");
//                        mColors = new Integer[mTypeNum];
//                        //分析背景颜色
//                        for ( int i = 0; i < colorStrs.length; i++ ) {
//                            try {
//                                mColors[i] = Color.parseColor(colorStrs[i]);
//                            } catch ( Exception e ) {
//                                throw new RuntimeException("颜色值有误");
//                            }
//                        }
//                        //加载分类图片 存放图片的集合
//                        mListBitmap = new ArrayList<>();
//                        for ( int i = 0; i < mTypeNum; i++ ) {
//                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), mIcons[i]);
//                            int ww = bitmap.getWidth();
//                            int hh = bitmap.getHeight();
//                            // 定义矩阵对象
//                            Matrix matrix = new Matrix();
//                            // 缩放原图
//                            matrix.postScale(1f, 1f);
//                            // 向左旋转45度，参数为正则向右旋转
//                            matrix.postRotate(mAngle * i);
//                            //bmp.getWidth(), 500分别表示重绘后的位图宽高
//                            Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, ww, hh,
//                                    matrix, true);
//                            mListBitmap.add(dstbmp);
//                        }
//                        //文字画笔
//                        mTextPaint = new Paint();
//                        //设置填充样式
//                        mTextPaint.setStyle(Paint.Style.STROKE);
//                        //设置抗锯齿
//                        mTextPaint.setAntiAlias(true);
//                        //设置边界模糊
//                        mTextPaint.setDither(true);
//                        //设置画笔颜色
//                        mTextPaint.setColor(mTextColor);
//                        //设置字体大小
//                        mTextPaint.setTextSize(mTextSize);
//
//                        mMain = BitmapFactory.decodeResource(mContext.getResources(), mMainImgRes);

                    } else if (mType == 2) {

                        //直接大图
//                        if ( mMainImgRes == 0 )
//                            throw new RuntimeException("类型为2必须要传大图mMainImgRes");
                        mMain = BitmapFactory.decodeResource(mContext.getResources(), mMainImgRes);
                    } else {
                        throw new RuntimeException("类型type错误");
                    }
                }
            } finally { //回收这个对象
                typedArray.recycle();
            }
        }

        //其他画笔
        mPaint = new Paint();
        //设置填充样式
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置边界模糊
        mPaint.setDither(true);
    }

    public void setData(String[] strings, List<Bitmap> mListBitmap) {
        this.mDeses = strings;
        this.mListBitmap = mListBitmap;
        this.invalidate();
    }

    private Bitmap createCircleBitmap(Bitmap resource)
    {
        //获取图片的宽度
        int width = resource.getWidth();
        Paint paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);

        //创建一个与原bitmap一样宽度的正方形bitmap
        Bitmap circleBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        //以该bitmap为低创建一块画布
        Canvas canvas = new Canvas(circleBitmap);
        //以（width/2, width/2）为圆心，width/2为半径画一个圆
        canvas.drawCircle(width/2, width/2, width/2, paint);
        //设置画笔为取交集模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //裁剪图片
        canvas.drawBitmap(resource, 0, 0, paint);

        return circleBitmap;
    }

    //目前的角度
    private float currAngle = 0;
    //记录上次的位置
    private int lastPosition;

    /**
     * 开始转动
     * pos 位置 1 开始 这里的位置上是按照逆时针递增的 比如当前指的那个选项是第一个  那么他左边的那个是第二个 以此类推
     */
    public void startRotate(final int pos) {
        if (mDeses==null ||mDeses.length==0){
            return;
        }
        //最低圈数是mMinTimes圈
        int newAngle = (int) (360 * mMinTimes + (pos - 1) * mAngle + currAngle - (lastPosition == 0 ? 0 : ((lastPosition - 1) * mAngle)));
        //计算目前的角度划过的扇形份数
        int num = (int) ((newAngle - currAngle) / mAngle);
        ObjectAnimator anim = ObjectAnimator.ofFloat(WheelSurfPanView.this, "rotation", currAngle, newAngle + 360/12/2);
        currAngle = newAngle;
        lastPosition = pos;
        // 动画的持续时间，执行多久？
        anim.setDuration(num * mVarTime);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //将动画的过程态回调给调用者
                if (rotateListener != null)
                    rotateListener.rotating(animation);
            }
        });
        final float[] f = {0};
//        anim.setInterpolator(new TimeInterpolator() {
//            @Override
//            public float getInterpolation(float t) {
//                float f1 = (float) (Math.cos((t + 1) * Math.PI) / 2.0f) + 0.5f;
//                Log.e("HHHHHHHh", "" + t + "     " + (f[0] - f1));
//                f[0] = (float) (Math.cos((t + 1) * Math.PI) / 2.0f) + 0.5f;
//                return f[0];
//            }
//        });
        anim.setInterpolator(new FastOutSlowInInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //当旋转结束的时候回调给调用者当前所选择的内容
                if (rotateListener != null) {
                    if (mType == 1) {
                        //去空格和前后空格后输出
                        String des = mDeses[(mTypeNum - pos + 1) %
                                mTypeNum].trim().replaceAll(" ", "");
                        rotateListener.rotateEnd(pos, des);
                    } else {
                        rotateListener.rotateEnd(pos, "");
                    }
                }
            }
        });
        // 正式开始启动执行动画
        anim.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //视图是个正方形的 所以有宽就足够了 默认值是500 也就是WRAP_CONTENT的时候
        int desiredWidth = 800;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //将测得的宽度保存起来
        mWidth = width;

        mCenter = mWidth / 2;
        //绘制扇形的半径 减掉50是为了防止边界溢出  具体效果你自己注释掉-50自己测试
        mRadius = mWidth / 2 - 50;

        //MUST CALL THIS
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTypeNum == -1) {
            //先不管
        } else if (mTypeNum == 12) {
            Rect mDestRect = new Rect(0, 0, mWidth, mWidth);
            canvas.drawBitmap(mMain, null, mDestRect, mPaint);
            if (mIsOver) {
                // 计算初始角度
                // 从最上面开始绘制扇形会好看一点
                float startAngle = - mAngle / 2 - 105;

                final int paddingLeft = getPaddingLeft();
                final int paddingRight = getPaddingRight();
                final int paddingTop = getPaddingTop();
                final int paddingBottom = getPaddingBottom();
                int width = getWidth() - paddingLeft - paddingRight;
                int height = getHeight() - paddingTop - paddingBottom;

                for (int i = 0; i < mTypeNum; i++) {
//                    mTextPaint.setColor(mTextColor);
                    drawText(startAngle, mDeses[i], mRadius, mTextPaint, canvas);

                    int imgWidth = mRadius / 5;

//                    int w = (int) (Math.abs(Math.cos(Math.toRadians(Math.abs(180 - mAngle * i)))) *
//                            imgWidth + imgWidth * Math.abs(Math.sin(Math.toRadians(Math.abs(180 - mAngle * i)))));
//                    int h = (int) (Math.abs(Math.sin(Math.toRadians(Math.abs(180 - mAngle * i)))) *
//                            imgWidth + imgWidth * Math.abs(Math.cos(Math.toRadians(Math.abs(180 - mAngle * i)))));

                    int w = MyUtils.getInstans().dp2px(mContext,35);
                    int h = MyUtils.getInstans().dp2px(mContext,35);

                    int w2 = MyUtils.getInstans().dp2px(mContext,24);
                    int h2 = MyUtils.getInstans().dp2px(mContext,24);

                    float angle = (float) Math.toRadians(startAngle + mAngle / 2);

                    //确定图片在圆弧中 中心点的位置
                    float x = (float) (width / 2 + (mRadius / 2.2 + mRadius / 12) * Math.cos(angle));
                    float y = (float) (height / 2 + (mRadius /2.2 + mRadius / 12) * Math.sin(angle));
                    // 确定绘制图片的位置
                    RectF rect2 = new RectF((float) (x - w/ 1.5), (float) (y - h/1.5), (float) (x + w/1.5), (float) (y + h / 1.5));
//                    canvas.drawBitmap(mBgHuanImgRes, null, rect2, null);

                    RectF rect1 = new RectF((float)(x - w2 / 1.7) , (float) (y - h2 / 1.7) , (float) (x + w2 / 1.7), (float) (y + h2 / 1.7));
                    canvas.drawBitmap(mListBitmap.get(i), null, rect1, null);

//                    canvas.drawBitmap(mListBitmap.get(i), null, rect1, null);

//                    canvas.drawBitmap(, null, rect1, null);

                    //重置开始角度
                    startAngle = startAngle + mAngle;
                }
            }
        } else {
            if (mType == 1) {
                // 计算初始角度
                // 从最上面开始绘制扇形会好看一点
                float startAngle = -mAngle / 2 - 90;

                final int paddingLeft = getPaddingLeft();
                final int paddingRight = getPaddingRight();
                final int paddingTop = getPaddingTop();
                final int paddingBottom = getPaddingBottom();
                int width = getWidth() - paddingLeft - paddingRight;
                int height = getHeight() - paddingTop - paddingBottom;

                for (int i = 0; i < mTypeNum; i++) {
                    //设置绘制时画笔的颜色
//                    mPaint.setColor(mColors[i]);
                    //画一个扇形
//                    RectF rect = new RectF(mCenter - mRadius, mCenter - mRadius, mCenter
//                            + mRadius, mCenter + mRadius);
//                    canvas.drawArc(rect, startAngle, mAngle, true, mPaint);
                    mTextPaint.setColor(mTextColor);
                    drawText(startAngle, mDeses[i], mRadius, mTextPaint, canvas);

                    int imgWidth = mRadius / 3;

                    int w = (int) (Math.abs(Math.cos(Math.toRadians(Math.abs(180 - mAngle * i)))) *
                            imgWidth + imgWidth * Math.abs(Math.sin(Math.toRadians(Math.abs(180 - mAngle * i)))));
                    int h = (int) (Math.abs(Math.sin(Math.toRadians(Math.abs(180 - mAngle * i)))) *
                            imgWidth + imgWidth * Math.abs(Math.cos(Math.toRadians(Math.abs(180 - mAngle * i)))));

                    float angle = (float) Math.toRadians(startAngle + mAngle / 2);

                    //确定图片在圆弧中 中心点的位置
                    float x = (float) (width / 2 + (mRadius / 2 + mRadius / 12) * Math.cos(angle));
                    float y = (float) (height / 2 + (mRadius / 2 + mRadius / 12) * Math.sin(angle));
                    // 确定绘制图片的位置
                    RectF rect1 = new RectF(x - w / 2 , y - h /2, x + w/2 , y + h/2);


                    canvas.drawBitmap(mListBitmap.get(i), null, rect1, null);


                    //重置开始角度
                    startAngle = startAngle + mAngle;
                }
                //大圆盘
//                Rect mDestRect = new Rect(0, 0, mWidth, mWidth);
//                canvas.drawBitmap(mMain, null, mDestRect, mPaint);

                //最后绘制圆环
                Rect mDestRect = new Rect(0, 0, mWidth, mWidth);
//                canvas.drawBitmap(mYuanHuan, null, mDestRect, mPaint);
            } else {
                //大圆盘
                Rect mDestRect = new Rect(0, 0, mWidth, mWidth);
                canvas.drawBitmap(mMain, null, mDestRect, mPaint);
            }
        }
    }

    //绘制文字
    private void drawText(float startAngle, String string, int radius, Paint textPaint, Canvas canvas) {
        //创建绘制路径
        Path circlePath = new Path();
        //范围也是整个圆盘
        RectF rect = new RectF(mCenter - radius, mCenter - radius, mCenter
                + radius, mCenter + radius);
        //给定扇形的范围
        circlePath.addArc(rect, startAngle, mAngle);

        //圆弧的水平偏移
        float textWidth = textPaint.measureText(string);
        //圆弧的垂直偏移
        float hOffset = (float) (Math.sin(mAngle / 2 / 180 * Math.PI) * radius) - textWidth / 2;
        //绘制文字
        canvas.drawTextOnPath(string, circlePath, hOffset + 5, (float) (radius / 3.3), textPaint);
    }

    //再一次onDraw
    public void show() {
        //做最后的准备工作 检查数据是否合理
        if (mType == 1) {
            if (mHuanImgRes == null || mHuanImgRes == 0)
                mYuanHuan = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.yuanhuan);
            else {
                mYuanHuan = BitmapFactory.decodeResource(mContext.getResources(), mHuanImgRes);
            }
            //文字大小
            if (mTextSize == 0)
                mTextSize = 14 * getScale();
            //文字颜色 默认粉红色
            if (mTextColor == 0)
                mTextColor = Color.parseColor("#ff00ff");

            if (mListBitmap.size() != mDeses.length || mListBitmap.size() != mColors.length
                    || mDeses.length != mColors.length) {
                throw new RuntimeException("Icons数量和Deses和Colors三者数量必须与mTypeNum一致");
            }
        } else {
            //直接大图
            if (mMainImgRes == null || mMainImgRes == 0)
                throw new RuntimeException("类型为2必须要传大图mMainImgRes");
            mMain = BitmapFactory.decodeResource(mContext.getResources(), mMainImgRes);
        }

        if (mTextPaint == null) {
            //文字画笔
            mTextPaint = new Paint();
            //设置填充样式
            mTextPaint.setStyle(Paint.Style.STROKE);
            //设置抗锯齿
            mTextPaint.setAntiAlias(true);
            //设置边界模糊
            mTextPaint.setDither(true);
            //设置画笔颜色
            mTextPaint.setColor(mTextColor);
            //设置字体大小
            mTextPaint.setTextSize(mTextSize);
        }
        if (mTypeNum != 0)
            mAngle = (float) (360.0 / mTypeNum);
        if (mVarTime == 0)
            mVarTime = 75;

        //重绘
        invalidate();
    }

    //再一次onDraw
    public void myShow() {
        //做最后的准备工作 检查数据是否合理
//            if ( mHuanImgRes == null || mHuanImgRes == 0 )
//                mYuanHuan = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.yuanhuan);
//            else {
//                mYuanHuan = BitmapFactory.decodeResource(mContext.getResources(), mHuanImgRes);
//            }
        //文字大小
        if (mTextSize == 0)
            mTextSize = 12 * getScale();
        if (mTextColor == 0)
            mTextColor = Color.parseColor("#7651F1");

        if (mListBitmap.size() != mDeses.length) {
            throw new RuntimeException("Icons数量和Deses和Colors三者数量必须与mTypeNum一致");
        }

        if (mTextPaint == null) {
            //文字画笔
            mTextPaint = new Paint();
            //设置填充样式
            mTextPaint.setStyle(Paint.Style.STROKE);
            //设置抗锯齿
            mTextPaint.setAntiAlias(true);
            //设置边界模糊
            mTextPaint.setDither(true);
            //设置画笔颜色
            mTextPaint.setColor(mTextColor);
            //设置字体大小
            mTextPaint.setTextSize(mTextSize);
        }
        if (mTypeNum != 0)
            mAngle = (float) (360.0 / mTypeNum);
        if (mVarTime == 0)
            mVarTime = 70;

        //重绘
        invalidate();
    }

    /**
     * 按照指定的宽高比例,对源Bitmap进行裁剪
     * 注意，输出的Bitmap只是宽高比与指定宽高比相同，大小未必相同
     * @param srcBitmap 源图片对应的Bitmap
     * @param desWidth  目标图片宽度
     * @param desHeight 目标图片高度
     * @return
     */
    private Bitmap centerCrop(Bitmap srcBitmap, int desWidth, int desHeight){
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int newWidth = srcWidth;
        int newHeight = srcHeight;
        float srcRate = (float)srcWidth/srcHeight;
        float desRate = (float)desWidth/desHeight;
        int dx = 0, dy = 0;
        if(srcRate == desRate){
            return srcBitmap;
        } else if(srcRate > desRate){
            newWidth = (int)(srcHeight * desRate);
            dx = (srcWidth-newWidth)/2;
        } else {
            newHeight = (int)(srcWidth / desRate);
            dy = (srcHeight - newHeight)/2;
        }
        //创建目标Bitmap，并用选取的区域来绘制
        Bitmap desBitmap = Bitmap.createBitmap(srcBitmap, dx, dy, newWidth, newHeight);
        return desBitmap;
    }

    private float getScale() {
        TextView textView = new TextView(mContext);
        textView.setTextSize(1);
        return textView.getTextSize();
    }
}
