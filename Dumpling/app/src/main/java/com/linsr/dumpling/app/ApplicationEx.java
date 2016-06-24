package com.linsr.dumpling.app;

import android.app.Application;

import com.linsr.dumpling.log.Log;
import com.linsr.dumpling.log.LogImpl;
import com.linsr.dumpling.zhihu.ZhihuManager;

/**
 * description
 *
 * @author Linsr
 */
public class ApplicationEx extends Application {

    private Log mLog;

    @Override
    public void onCreate() {
        super.onCreate();

        mLog = LogImpl.getInstance();
        mLog.setLogToLogCat(true);

        ZhihuManager.getInstance().init(this,mLog);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
