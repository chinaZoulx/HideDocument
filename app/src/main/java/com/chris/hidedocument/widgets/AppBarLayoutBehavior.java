package com.chris.hidedocument.widgets;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.MotionEvent;

/**
 * Created by zoulx on 2016/3/22.
 */
public class AppBarLayoutBehavior extends AppBarLayout.Behavior{

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        return !(parent!=null||child!=null||ev!=null||super.onInterceptTouchEvent(parent, child, ev));
    }
}
