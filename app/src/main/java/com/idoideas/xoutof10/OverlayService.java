package com.idoideas.xoutof10;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

/**
 * Created by Shayevitz on 17/09/2017.
 */

public class OverlayService extends Service {

    WindowManager windowManager;
    View view;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager =  (WindowManager) getSystemService(WINDOW_SERVICE);
        drawPortait();
    }

    public void drawPortait(){
        view = View.inflate(getApplicationContext(), R.layout.bump, null);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                (int)(2*getYPPI()),
                (int)(0.24*getXPPI()),
                // Allows the view to be on top of the StatusBar
                Build.VERSION.SDK_INT < 26 ? WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY : TYPE_APPLICATION_OVERLAY,
                // Keeps the button presses from going to the background window
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        // Enables the notification to recieve touch events
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                        // Draws over status bar
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        params.gravity =  Gravity.TOP| Gravity.CENTER;
        windowManager.addView(view , params);
    }

    public void drawLandscape(){
        view = View.inflate(getApplicationContext(), R.layout.bump_l, null);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                (int)(0.24*getYPPI()),
                (int)(2*getXPPI()),
                // Allows the view to be on top of the StatusBar
                Build.VERSION.SDK_INT < 26 ? WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY : TYPE_APPLICATION_OVERLAY,
                // Keeps the button presses from going to the background window
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity =  Gravity.LEFT;
        windowManager.addView(view , params);
    }

    public void removeView(){
        windowManager.removeView(view);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        removeView();
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == ORIENTATION_LANDSCAPE){
            removeView();
            drawLandscape();
        } else if (newConfig.orientation == ORIENTATION_PORTRAIT){
            removeView();
            drawPortait();
        }
    }

    public float getXPPI(){
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.xdpi;
    }

    public float getYPPI(){
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.ydpi;
    }
}
