package com.chris.hidedocument.widgets;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zoulx on 2016/3/22.
 */
public class AppBarLayoutRepair extends AppBarLayout {



    public AppBarLayoutRepair(Context context, AttributeSet attrs) {
        super(context, attrs);
        ((CoordinatorLayout.LayoutParams)getLayoutParams()).setBehavior(new AppBarLayoutBehavior());
    }

    public class AppBarLayoutBehavior extends AppBarLayout.Behavior{

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
            return !(parent!=null||child!=null||ev!=null||super.onInterceptTouchEvent(parent, child, ev));
        }
    }

}
