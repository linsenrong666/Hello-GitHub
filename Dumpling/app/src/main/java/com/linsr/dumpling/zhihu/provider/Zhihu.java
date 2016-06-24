package com.linsr.dumpling.zhihu.provider;

import android.net.Uri;

import com.linsr.dumpling.zhihu.provider.columns.DailyNewsColumn;

/**
 * description
 *
 * @author Linsr
 */
public class Zhihu {

    public static final String AUTHORITY = "com.linsr.dumpling.zhihu.provider.Zhihu";

    static class DailyNewsBase {

    }

    public static class DailyNews extends DailyNewsBase implements DailyNewsColumn {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/DailyNews");
    }

}
