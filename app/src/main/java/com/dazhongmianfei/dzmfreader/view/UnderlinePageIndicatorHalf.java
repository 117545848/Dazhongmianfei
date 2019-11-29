/*
 * Copyright (C) 2012 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dazhongmianfei.dzmfreader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;

/**
 * 适用于"男生" "女生"水平平分的情况，两者间隔屏幕一半宽度，比如分类、排行、包月、完本、限免里面的男生、女生
 */
public class UnderlinePageIndicatorHalf extends UnderlinePageIndicator {

    public UnderlinePageIndicatorHalf(Context context) {
        super(context);
    }

    public UnderlinePageIndicatorHalf(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnderlinePageIndicatorHalf(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mViewPager == null) {
            return;
        }
        final int count = mViewPager.getAdapter().getCount();
        if (count == 0) {
            return;
        }

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        final int paddingLeft = getPaddingLeft();
        final float pageWidth = (getWidth() - paddingLeft - getPaddingRight()) / (1f * count);
        final float left = paddingLeft + pageWidth / 2f + pageWidth * (mCurrentPage + mPositionOffset) - 30;
        final float right = left + 60;
        final float top = getPaddingTop();
        final float bottom = getHeight() - getPaddingBottom();
        if (Build.VERSION.SDK_INT >= 21) {
            canvas.drawRoundRect(left + width, top, right - width, bottom, 5, 5, mPaint);
        } else {
            canvas.drawRect(left + width, top, right - width, bottom, mPaint);

        }
        //canvas.drawRoundRect();
    }

    int width =15;
}