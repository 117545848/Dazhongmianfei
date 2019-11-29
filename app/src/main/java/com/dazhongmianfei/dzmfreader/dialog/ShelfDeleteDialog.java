package com.dazhongmianfei.dzmfreader.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;

public class ShelfDeleteDialog {

    public interface ShelfDelete {
        void Option(int option);
    }

    public static void showShelfDeleteDialog(final Activity activity, ShelfDelete shelfDelete) {

        Dialog bottomDialog = new Dialog(activity, R.style.BottomDialog);


        bottomDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_shelfdelete, null);
        final TextView shelf_book_delete_all = view.findViewById(R.id.shelf_book_delete_all);
        final TextView shelf_book_delete_del = view.findViewById(R.id.shelf_book_delete_del);
        final TextView shelf_book_delete_cancle = view.findViewById(R.id.shelf_book_delete_cancle);


        shelf_book_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shelfDelete.Option(0);

            }
        });
        shelf_book_delete_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shelfDelete.Option(1);
                bottomDialog.dismiss();
            }
        });
        shelf_book_delete_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shelfDelete.Option(2);
                bottomDialog.dismiss();
            }
        });


        bottomDialog.setContentView(view);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        params.height = ImageUtil.dp2px(activity, 60);
        view.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
        bottomDialog.onWindowFocusChanged(false);
        bottomDialog.setCanceledOnTouchOutside(false);
        bottomDialog.setCancelable(false);

    }
}
