package com.cw.farmer.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class TextView_HKNova_Medium extends androidx.appcompat.widget.AppCompatTextView {

    public TextView_HKNova_Medium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextView_HKNova_Medium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextView_HKNova_Medium(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/HKNova-Medium.ttf");
            setTypeface(tf);
        }
    }

}