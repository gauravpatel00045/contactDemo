package com.contactdemo.helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.contactdemo.R;

public class CustomDialog {
    private static CustomDialog instance;
    private Dialog dialog;

    //constructor
    private CustomDialog() {
    }

    /**
     * @return (CustomDialog) instance : it return the CustomDialog instance
     */
    public static CustomDialog getInstance() {
        if (instance == null) {
            instance = new CustomDialog();
        }
        return instance;
    }

    /**
     * This method hide the current showing dialog
     */
    public void hide() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * @return (boolean) : return true or false, if the dialog is showing or not
     */
    public boolean isDialogShowing() {

        return dialog != null && dialog.isShowing();
    }

    /**
     * This method shows dialog with alert message with single button
     *
     * @param context      (Context)      : context
     * @param msg          (String)           : required message on dialog
     * @param isCancelable (boolean) : true or false, for dialog cancellation
     *                     e.g. true - dismiss dialog when user touch on screen within dialog view
     *                     false - disable outside touch within a dialog view
     * @param buttonText   (String)    : required text on button eg. submit, update, dismiss
     */
    public void showAlert(final Context context, String msg, boolean isCancelable, String buttonText) {

        dialog = new Dialog(context, R.style.DialogTheme);
        @SuppressLint("InflateParams") final View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_alert, null);

        TextView mTxtAlert = (TextView) view.findViewById(R.id.txtAlert);
        TextView mTxtYes = (TextView) view.findViewById(R.id.txtOk);


        mTxtAlert.setText(msg);
        /*mTxtYes.setTag(buttonText);*/

        dialog.setCanceledOnTouchOutside(isCancelable);
        dialog.setContentView(view);

        mTxtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        try {
            if (dialog != null) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(Context context,boolean isCancelable) {

        //dialog = new Dialog(context, R.style.DialogTheme);

//		if(context==null)
//			context= MyApplication.appInstance;

        dialog = new Dialog(context, android.R.style.Animation_Dialog);
        Drawable d = new ColorDrawable(Color.TRANSPARENT);
        //d.setAlpha(130);
        dialog.getWindow().setBackgroundDrawable(d);

        dialog.setCancelable(isCancelable);
        dialog.setContentView(R.layout.loader);

        TextView txtLoaderTitle= (TextView) dialog.findViewById(R.id.txtLoaderTitle);

        try {
            if (dialog != null) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
