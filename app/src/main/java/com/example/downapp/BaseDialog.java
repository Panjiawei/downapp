package com.example.downapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;


/**
 * Created by zhujieting on 2016/11/3.
 */
public abstract class BaseDialog extends Dialog {
    private static final String TAG = BaseDialog.class.getSimpleName();
    protected Context mContext;
    protected Activity mActivity;

    public BaseDialog(Context context) {
        super(context, R.style.Dialog);
        this.mContext = context;
        this.mActivity = (Activity) mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        initView();
        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setData();
    }

    @Override
    public void show() {
        //如果依附的activity正在关闭或已销毁则直接返回
        if(activityIsFinishing()) {
            return;
        }
        super.show();
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * scale
     *            （DisplayMetrics类中属性density）
     * @return
     */
    protected float dip2px(int dipValue)
    {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }

    @SuppressLint("NewApi")
    public boolean activityIsFinishing(){
        return mActivity.isFinishing() || mActivity.isDestroyed();
    }

    public Activity getmActivity() {
        return mActivity;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    protected abstract void init();
    protected abstract void initView();
    protected abstract void setListener();
    protected abstract void setData();
}
