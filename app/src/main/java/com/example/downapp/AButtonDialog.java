package com.example.downapp;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/3/5.
 */

public class AButtonDialog extends BaseDialog implements View.OnClickListener {

    private View view;
    private String titles, texts, confirmButtonTexts;
    private TextView tv_title, tv_button, tv_txet;
    private ClickListenerInterface clickListenerInterface;

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public interface ClickListenerInterface {
        void doConfirm();
    }

    public AButtonDialog(Context context) {
        super(context);

    }

    @Override
    protected void init() {
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_a_button, null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    @Override
    protected void initView() {
        tv_title = view.findViewById(R.id.tv_title);
        tv_txet = view.findViewById(R.id.tv_text);
        tv_button = view.findViewById(R.id.tv_button);

    }

    @Override
    protected void setData() {
        tv_button.setText(confirmButtonTexts);
        tv_title.setText(titles);
        if (!TextUtils.isEmpty(texts)) {
            tv_txet.setText(texts);
            tv_txet.setVisibility(View.VISIBLE);
        }

    }

    public void setStyle(String title, String confirmButtonText) {
        this.titles = title;
        this.confirmButtonTexts = confirmButtonText;
    }

    public void setStyle(String title, String text, String confirmButtonText) {
        this.titles = title;
        this.texts = text;
        this.confirmButtonTexts = confirmButtonText;


    }

    @Override
    protected void setListener() {
        tv_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_button:
                dismiss();
                if (clickListenerInterface != null)
                    clickListenerInterface.doConfirm();
                break;
        }

    }


}
