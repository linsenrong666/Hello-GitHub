package com.linsr.dumpling.zhihu;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.linsr.dumpling.zhihu.model.DailyDetailsPojo;
import com.linsr.dumpling.zhihu.model.StoriesPojo;
import com.linsr.dumpling.zhihu.provider.Zhihu;

import java.util.List;

import rx.Observable;

/**
 * description
 *
 * @author Linsr
 */
class ZhihuDao {

    private static final String DAILY_NEWS_WHERE_BY_ID = Zhihu.DailyNews.ID + " =? ";

    private static final String[] DAILY_DETAILS_MAPPING = {Zhihu.DailyNews._ID,//0
            Zhihu.DailyNews.ID,//1
            Zhihu.DailyNews.CONTENT,//2
    };

    private ContentResolver mResolver;

    public ZhihuDao(Context context) {
        mResolver = context.getContentResolver();
    }

    public void saveDailyNewsList(List<StoriesPojo> storiesPojos) {
        ContentValues values = new ContentValues();

        String[] args = new String[1];
        for (StoriesPojo item : storiesPojos) {
            values.clear();

            String id = item.getId();
            args[0] = item.getId();

            values.put(Zhihu.DailyNews.ID, id);
            values.put(Zhihu.DailyNews.TITLE, item.getTitle());
            values.put(Zhihu.DailyNews.IMAGE_URL, item.getImages()[0]);

            if ((mResolver.update(Zhihu.DailyNews.CONTENT_URI, values, DAILY_NEWS_WHERE_BY_ID, args) == 0)) {
                mResolver.insert(Zhihu.DailyNews.CONTENT_URI, values);
            }
        }

    }

    public void saveDailyNewsDetails(DailyDetailsPojo dailyDetailsPojo) {
        ContentValues values = new ContentValues();
        String[] args = new String[1];

        String id = dailyDetailsPojo.getId();
        String body = dailyDetailsPojo.getBody();

        values.put(Zhihu.DailyNews.ID, id);
        values.put(Zhihu.DailyNews.CONTENT, body);

        args[0] = id;
        if ((mResolver.update(Zhihu.DailyNews.CONTENT_URI, values, DAILY_NEWS_WHERE_BY_ID, args) == 0)) {
            mResolver.insert(Zhihu.DailyNews.CONTENT_URI, values);
        }

    }

    public DailyDetailsPojo readDailyNewsDetails(String id) {
        Cursor cursor = null;
        try {
            cursor = mResolver.query(Zhihu.DailyNews.CONTENT_URI, DAILY_DETAILS_MAPPING,
                    DAILY_NEWS_WHERE_BY_ID, new String[]{id}, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                DailyDetailsPojo pojo = new DailyDetailsPojo();
                pojo.setId(cursor.getString(1));
                pojo.setBody(cursor.getString(2));
                return pojo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

}
