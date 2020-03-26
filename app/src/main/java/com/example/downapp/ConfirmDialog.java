package com.example.downapp;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/7/8.
 */
public class ConfirmDialog extends BaseDialog {
    private View view;
    private String title,txetOne, textTwo,confirmButtonText, cacelButtonText;
    private ClickListenerInterface clickListenerInterface;
    private OnCancelClickListener onCancelClickListener;

    private TextView tv_title, tvConfirm, tvCancel, tv_txet_two,tv_txet_one;


    public interface ClickListenerInterface {
        void doConfirm();
    }

    public interface OnCancelClickListener{
        void onCancelClick();
    }
    public ConfirmDialog(Context context) {
        super(context);
    }

    public ConfirmDialog(Context context, String title, String confirmButtonText, String cacelButtonText) {
        super(context);
        this.title = title;
        this.confirmButtonText = confirmButtonText;
        this.cacelButtonText = cacelButtonText;
    }

    protected void init() {
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_normal_layou, null);
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
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tvCancel = (TextView) view.findViewById(R.id.left_button);
        tvConfirm = (TextView) view.findViewById(R.id.right_button);
        tv_txet_one = (TextView) view.findViewById(R.id.tv_txet_one);
        tv_txet_two = (TextView) view.findViewById(R.id.tv_txet_two);


    }

    @Override
    protected void setListener() {
            ClickListener listener = new ClickListener();
            tvConfirm.setOnClickListener(listener);
            tvCancel.setOnClickListener(listener);
    }

    @Override
    protected void setData() {
        tv_title.setText(title);
        tvConfirm.setText(confirmButtonText);
        tvCancel.setText(cacelButtonText);
        if(!android.text.TextUtils.isEmpty(txetOne)){
            tv_txet_one.setText(txetOne);
            tv_txet_one.setVisibility(View.VISIBLE);
        }
        if(!android.text.TextUtils.isEmpty(textTwo)){
            tv_txet_two.setText(textTwo);
            tv_txet_two.setVisibility(View.VISIBLE);
        }




    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener){
        this.onCancelClickListener = onCancelClickListener;
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.left_button:
                    dismiss();
                    if(onCancelClickListener != null)
                        onCancelClickListener.onCancelClick();
                    break;
                case R.id.right_button:
                    dismiss();
                    if(clickListenerInterface != null)
                        clickListenerInterface.doConfirm();
                    break;
            }
        }

    }

    public void setStyle(String title, String confirmButtonText, String cacelButtonText){
        this.title = title;
        this.confirmButtonText = confirmButtonText;
        this.cacelButtonText = cacelButtonText;
    }
    public void setStyle(String title, String textOne, String confirmButtonText, String cacelButtonText){
        this.title = title;
        this.txetOne = textOne;
        this.confirmButtonText = confirmButtonText;
        this.cacelButtonText = cacelButtonText;
    }
    public void setStyle(String title, String textOne, String textTwo, String confirmButtonText, String cacelButtonText){
        this.title = title;
        this.txetOne = textOne;
        this.textTwo = textTwo;
        this.confirmButtonText = confirmButtonText;
        this.cacelButtonText = cacelButtonText;
    }




}
