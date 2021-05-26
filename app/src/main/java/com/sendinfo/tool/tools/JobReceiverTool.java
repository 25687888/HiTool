package com.sendinfo.tool.tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时广播
 */
public class JobReceiverTool extends BroadcastReceiver {

    private Map<String, JobBean> map = new ConcurrentHashMap<>();

    private Context mContext;

    private Handler handler = new Handler();

    public void register(Context context, String... actions) {
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        for (String action : actions) {
            intentFilter.addAction(action);
        }
        context.registerReceiver(this, intentFilter);
    }

    public void unregister(Context context) {
        try {
            context.unregisterReceiver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动任务,具体action需要注册
     */
    public void doJob(JobBean jobBean) {
        if (mContext == null) {
            throw new RuntimeException("unregistered JobReceiverTool,Please call the registration method first");
        }
        if (jobBean.delayTime <= 0) jobBean.delayTime = 3_000;
        if (jobBean.repeatTime < 0) jobBean.repeatTime = 0;
        map.put(jobBean.action, jobBean);
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (mgr == null) {
            doException(jobBean.action, jobBean.delayTime);
            return;
        }
        cancelJob(jobBean);
        Intent intent = new Intent();
        intent.setAction(jobBean.action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, jobBean.requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + jobBean.delayTime, pendingIntent);
            } else {
                mgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + jobBean.delayTime, pendingIntent);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            doException(jobBean.action, jobBean.delayTime);
        }
    }

    /**
     * 取消任务
     */
    public void cancelJob(JobBean jobBean) {
        if (mContext == null) return;
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (mgr == null) return;
        Intent intent = new Intent();
        intent.setAction(jobBean.action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, jobBean.requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        try {
            mgr.cancel(pendingIntent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null || TextUtils.isEmpty(action)) return;
        JobBean jobBean = map.get(action);
        if (jobBean != null) {
            doJob(jobBean);
            Runnable runnable = jobBean.runnableWeakReference.get();
            if (runnable != null) runnable.run();
        }
    }

    /**
     * 定时器异常处理函数！
     */
    private void doException(final String action, final long delay) {
        if (delay <= 0) return;
        Runnable runnable = () -> onReceive(mContext, new Intent(action));
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delay);
    }

    /**
     * 定时器任务Bean
     */
    public static class JobBean {
        String action;//定时任务Action
        long delayTime;//延时时间
        long repeatTime;//定时间隔
        int requestCode = 100;//请求码
        WeakReference<Runnable> runnableWeakReference;//定时任务

        public JobBean(@NonNull String action, long delayTime, long repeatTime, int requestCode, @NonNull Runnable runnable) {
            this.action = action;
            this.delayTime = delayTime;
            this.repeatTime = repeatTime;
            this.requestCode = requestCode;
            this.runnableWeakReference = new WeakReference<>(runnable);
        }
    }
}
