package com.example.downapp;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;



/**
 * Created by Administrator on 2018/3/9.
 */

public class ProgressBarDialog extends BaseDialog {
    private View view;
    ProgressBar pb_bar;
    TextView tv_loadingmsg;

    public ProgressBarDialog(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress_bar, null);
        setContentView(view);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    @Override
    protected void initView() {
        pb_bar = view.findViewById(R.id.pb_bar);
        tv_loadingmsg = view.findViewById(R.id.tv_loadingmsg);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void setData() {
        setProgress(0);
    }
    public  void  setProgress(int i){
        pb_bar.setProgress(i);

    }
    public  void  setMax(int i){
        pb_bar.setMax(i);

    }
}
