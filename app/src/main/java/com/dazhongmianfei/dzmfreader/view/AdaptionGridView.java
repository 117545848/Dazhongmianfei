package com.dazhongmianfei.dzmfreader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.GridView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.utils.Utils;

/**
 * 高度自适应，防止出现滚动条
 */
public class AdaptionGridView extends GridView {

    public AdaptionGridView(Context context) {
        this(context, null);
    }

    public AdaptionGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdaptionGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
      //  setPadding(Utils.convertDpToPx(10), 0, Utils.convertDpToPx(10), 0);
        setSelector(getResources().getDrawable(R.drawable.selector_listview_item));
        setFocusable(false);
    }

    //    不出现滚动条
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}