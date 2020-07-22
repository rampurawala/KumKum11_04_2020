package com.expedite.apps.kumkum.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatSeekBar;

import java.util.ArrayList;

/**
 * Created by JAMEELA on 03/07/2020.
 */
public class CustomProgressBar extends AppCompatSeekBar {

    private ArrayList mProgressItemsList;

    public CustomProgressBar(Context context) {
        super(context);
        mProgressItemsList = new ArrayList();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initData(ArrayList progressItemsList) {
        this.mProgressItemsList = progressItemsList;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        if (mProgressItemsList!=null && mProgressItemsList.size() > 0) {
            int progressBarWidth = getWidth();
            int progressBarHeight = getHeight();
            int thumboffset = getThumbOffset();
            int lastProgressX = 0;
            int progressItemWidth, progressItemRight;
            for (int i = 0; i < mProgressItemsList.size(); i++) {
                ProgressItem progressItem = (ProgressItem) mProgressItemsList.get(i);
                Paint progressPaint = new Paint();
                progressPaint.setColor(getResources().getColor(
                        progressItem.color));

                progressItemWidth = (int) (progressItem.progressItemPercentage
                        * progressBarWidth / 100);

                progressItemRight = lastProgressX + progressItemWidth;

                // for last item give right to progress item to the width
                if (i == mProgressItemsList.size() - 1
                        && progressItemRight != progressBarWidth) {
                    progressItemRight = progressBarWidth;
                }
                RectF progressRect = new RectF();
                progressRect.set(lastProgressX, thumboffset / 2,
                        progressItemRight, progressBarHeight - thumboffset / 2);
                int cornersRadius = 25;
                canvas.drawRoundRect(progressRect,cornersRadius,cornersRadius, progressPaint);
                lastProgressX = progressItemRight;
            }
            super.onDraw(canvas);
        }

    }

}
