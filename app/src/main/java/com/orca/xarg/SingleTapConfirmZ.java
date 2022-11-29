package com.orca.xarg;

import android.view.GestureDetector;
import android.view.MotionEvent;

class SingleTapConfirmZ extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
        return true;
    }
}
