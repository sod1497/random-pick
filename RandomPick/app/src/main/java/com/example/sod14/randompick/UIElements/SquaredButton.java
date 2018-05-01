package com.example.sod14.randompick.UIElements;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by sod14 on 11/02/2018.
 */

public class SquaredButton extends android.support.v7.widget.AppCompatButton {

    public SquaredButton(Context context) {
        super(context);
    }

    public SquaredButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquaredButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(this.getMeasuredWidth(),this.getMeasuredWidth());
    }
}
