package com.linsr.dumpling.app.provider;

import android.net.Uri;

/**
 * Created by Linsr
 */
public interface ContentProviderDecorator {

    void startBatchNotification(Uri... uris);

    void endBatchNotification();
}
