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
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * Created by Idoideas on 17/09/2017.
 */

public class OverlayService extends Service {

    static WindowManager windowManager;
    static View view;
    final double wParam = 2;
    final double hParam = 0.24;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager =  (WindowManager) getSystemService(WINDOW_SERVICE);
        drawPortait();
    }

    public void drawPortait(){
        int overlayType = 0;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            overlayType = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        } else {
            overlayType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        view = View.inflate(getApplicationContext(), R.layout.bump, null);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                (int)(wParam*getYPPI()),
                (int)(hParam*getXPPI()),
                // Allows the view to be on top of the StatusBar
                 overlayType,
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
        int overlayType = 0;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            overlayType = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        } else {
            overlayType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        view = View.inflate(getApplicationContext(), R.layout.bump_l, null);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                (int)(hParam*getYPPI()),
                (int)(wParam*getXPPI()),
                // Allows the view to be on top of the StatusBar
                overlayType,
                // Keeps the button presses from going to the background window
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity =  Gravity.START;
        windowManager.addView(view , params);
    }

    public static void removeView(){
        if(view!=null && windowManager!=null){
            windowManager.removeView(view);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
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
        float xdpi = metrics.xdpi;
        if(xdpi<320){
            return 320;
        }
        return xdpi;
    }

    public float getYPPI(){
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        float ydpi = metrics.ydpi;
        if(ydpi<320){
            return 320;
        }
        return ydpi;
    }
}
