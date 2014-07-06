package common.android;

import android.content.Context;
import android.view.MotionEvent;
import android.webkit.WebView;

public class GWebView extends WebView {

    public GWebView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        boolean consumed = super.onTouchEvent(evt);

        if (isClickable()) {
            switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = evt.getX();
                lastTouchY = evt.getY();
                hasMoved = false;
                break;
            case MotionEvent.ACTION_MOVE:
                hasMoved = moved(evt);
                break;
            case MotionEvent.ACTION_UP:
                if (!moved(evt))
                    performClick();
                break;
            }
        }

        return consumed || isClickable();
    }

    private float lastTouchX, lastTouchY;
    private boolean hasMoved = false;

    private boolean moved(MotionEvent evt) {
        return hasMoved ||
                Math.abs(evt.getX() - lastTouchX) > 10.0 ||
                Math.abs(evt.getY() - lastTouchY) > 10.0;
    }
}
