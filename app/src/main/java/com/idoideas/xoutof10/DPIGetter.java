package com.idoideas.xoutof10;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import static android.content.Context.WINDOW_SERVICE;
import static com.idoideas.xoutof10.OverlayService.windowManager;

/**
 * Created by Shayevitz on 23/09/2017.
 */

public class DPIGetter {
    public static float xdpi = 0;
    public static float ydpi = 0;
    public static WindowManager windowManager = null;

    public static void startDPIChangeDialog(final Context context){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.changedpidialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);

        final EditText xdpiEdit = promptsView
                .findViewById(R.id.xdpi);
        final EditText ydpiEdit = promptsView
                .findViewById(R.id.ydpi);
        xdpiEdit.setText(xdpi+"");
        ydpiEdit.setText(ydpi+"");

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                setXDPI(Float.parseFloat(String.valueOf(xdpiEdit.getText())));
                                setYDPI(Float.parseFloat(String.valueOf(ydpiEdit.getText())));
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setNeutralButton("Reset Default DPI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetDPI((WindowManager) context.getSystemService(WINDOW_SERVICE));
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public static void setXDPI(float input){
        xdpi = input;
    }

    public static void setYDPI(float input){
        ydpi = input;
    }

    public static float getXDPI(){
        return xdpi;
    }

    public static float getYDPI(){
        return ydpi;
    }

    public static void resetDPI(WindowManager receivedWindowManager){
        if(windowManager == null){
            windowManager = receivedWindowManager;
        }
        setYDPI(getDefaultYPPI());
        setXDPI(getDefaultXPPI());
    }

    public static float getDefaultXPPI(){
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        float xdpi = metrics.xdpi;
        if(xdpi<320){
            return 320;
        }
        return xdpi;
    }

    public static float getDefaultYPPI(){
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        float ydpi = metrics.ydpi;
        if(ydpi<320){
            return 320;
        }
        return ydpi;
    }
}
