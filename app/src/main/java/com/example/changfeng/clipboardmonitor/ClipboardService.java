package com.example.changfeng.clipboardmonitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class ClipboardService extends Service {

    private static final String TAG = "ClipboardService";

    private static int count = 0;

    public static final String clipboardText = "clipboardText";

    private ClipboardServiceBinder binder = new ClipboardServiceBinder();

    class ClipboardServiceBinder extends Binder {
    }


    public ClipboardService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();

        count++;
        showToast(TAG + " OnCreate() " + count + " times.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final ClipboardManager cb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cb.setPrimaryClip(ClipData.newPlainText("", ""));
        cb.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {


            @Override
            public void onPrimaryClipChanged() {

                if (cb.hasPrimaryClip()) {
                    for (int i = 0; i < cb.getPrimaryClip().getItemCount(); i++) {

                        Log.d(TAG, "onPrimaryClipChanged() " + cb.getPrimaryClip().getItemAt(i).getText());
                        if (cb.getPrimaryClip().getItemAt(i).getText() != null && cb.getPrimaryClip().getItemAt(i).getText().length() > 0) {
                            if (!cb.getPrimaryClip().getItemAt(i).getText().toString().trim().isEmpty()) {
                                String clipboardItem = cb.getPrimaryClip().getItemAt(i).getText().toString().trim();
                                Intent intent = new Intent();
                                intent.setClassName("com.example.changfeng.clipboardmonitor", "com.example.changfeng.clipboardmonitor.ResultActivity");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(clipboardText, clipboardItem);
                                startActivity(intent);
                                break;
                            }
                        }
                    }
                }
//                Toast.makeText(getApplicationContext(), TAG + " " + cb.getPrimaryClip().getItemAt(0).getText(), Toast.LENGTH_SHORT).show();
//                startAPP("bingdic.android.activity");
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }


    public void startAPP(String appPackageName){
        try{
            Intent intent = this.getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
        }catch(Exception e){
            Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show();
        }
    }

    void showToast(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }
}
