package com.dazhongmianfei.dzmfreader.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2018/7/6.
 */

public class GetDialog {


    public interface GetEditTextDialogInterface {
        void getText(String text);
    }

    static PopupWindow popupWindow;

    public static void getEditTextDialog(final Activity activity, View v, String title, String hint, final GetEditTextDialogInterface getEditTextDialogInterface) {

     /*   View view = LayoutInflater.from(activity).inflate(R.layout.dialog_getedittext, null);
         popupWindow = new PopupWindow(view, ImageUtil.dp2px(activity, 300), ImageUtil.dp2px(activity, 230), true);
        Button dialog_getedittext_cancle = (Button) view.findViewById(R.id.dialog_getedittext_cancle);
        Button dialog_getedittext_ok = (Button) view.findViewById(R.id.dialog_getedittext_ok);
        TextView dialog_getedittext_title = (TextView) view.findViewById(R.id.dialog_getedittext_title);
        final EditText dialog_getedittext_edittext1 = (EditText) view.findViewById(R.id.dialog_getedittext_edittext1);
        dialog_getedittext_title.setText(title);
        dialog_getedittext_edittext1.setHint(hint);
        dialog_getedittext_cancle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        dialog_getedittext_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String string = dialog_getedittext_edittext1.getText().toString();
                if (string.length() > 0) {
                    getEditTextDialogInterface.getText(string);
                    popupWindow.dismiss();
                } else {

                    MyShow.myToash(activity, "输入不能为空");
                }


            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });


        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);*/


    }


    public interface IsOperationInterface {
        void isOperation();
    }

    public static void IsOperation(final Activity activity, String title, String suretext, final IsOperationInterface isOperationInterface) {
        if (activity == null) {
            return;
        }
        Dialog dialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(suretext)
                .setPositiveButton(LanguageUtil.getString(activity, R.string.public_sure), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isOperationInterface.isOperation();


                    }
                })
                .setNegativeButton(LanguageUtil.getString(activity, R.string.splashactivity_cancle), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }

    public interface GetTimeDialogInterface {
        void getTimeDialogInterface(int year, int monthOfYear, int dayOfMonth);
    }

    public static void getTimeDialog(Activity activity, String minTime, Calendar calendar, final GetTimeDialogInterface getTimeDialogInterface) {
        int style = AlertDialog.THEME_HOLO_LIGHT;
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, style,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        getTimeDialogInterface.getTimeDialogInterface(year, monthOfYear, dayOfMonth);
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePickerDialog.setTitle("选择查询日期");
        try {
            //设置最小日期
            datePicker.setMinDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(minTime).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        } //设置最大日期
        datePicker.setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }







        private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
        private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public static boolean isNotificationEnabled(Context context) {

            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

            ApplicationInfo appInfo = context.getApplicationInfo();

            String pkg = context.getApplicationContext().getPackageName();

            int uid = appInfo.uid;

            Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

            try {

                appOpsClass = Class.forName(AppOpsManager.class.getName());

                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int)opPostNotificationValue.get(Integer.class);

                return ((int)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }

}
