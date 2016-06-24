package com.linsr.dumpling.zhihu.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.linsr.dumpling.app.provider.AbstractSQLiteHelper;
import com.linsr.dumpling.app.provider.BaseColumn;
import com.linsr.dumpling.app.provider.BaseContentProvider;
import com.linsr.dumpling.app.provider.ColumnMap;

import java.util.List;
import java.util.Map;

/**
 * description
 *
 * @author Linsr
 */
public class ZhihuProvider extends BaseContentProvider implements ZhihuTableNames {

    private static final String TAG = ZhihuProvider.class.getSimpleName();

    private static final String DATABASE_NAME = "zhihu.db";
    private static final int DATABASE_VERSION = 3;

    private static class DBHelper extends AbstractSQLiteHelper {

        private static volatile DBHelper mInstance;

        private DBHelper(Context context) {
            super(context, DATABASE_NAME, DATABASE_VERSION);
            mInstance = this;
        }

        public static DBHelper getInstance(Context context) {
            if (mInstance == null) {
                synchronized (DBHelper.class) {
                    if (mInstance == null) {
                        mInstance = new DBHelper(context);
                    }
                }
            }
            return mInstance;
        }

        @Override
        protected void onCreateEx(SQLiteDatabase db) {
            createTable(db, DAILY_NEWS_TABLE, createDailyNewsTable());
        }

        @Override
        protected void onUpgradeEx(SQLiteDatabase db, List<String> tables) {
            compareTable(db,tables,DAILY_NEWS_TABLE,createDailyNewsTable());
        }

        Map<String, String> createDailyNewsTable() {
            Map<String, String> map = newTableColumns();
            map.put(Zhihu.DailyNews.ID, text());
            map.put(Zhihu.DailyNews.TITLE, text());
            map.put(Zhihu.DailyNews.IMAGE_URL, text());
            map.put(Zhihu.DailyNews.DATE, integer());
            map.put(Zhihu.DailyNews.CONTENT, text());
            return map;
        }
    }

    private DBHelper mDBHelper;
    private static final UriMatcher uriMatcher;
    private static final int DAILY_NEWS = 1;
    private static final int DAILY_NEWS_ID = 2;

    private static ColumnMap dailyNewsMaps;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(Zhihu.AUTHORITY, "DailyNews", DAILY_NEWS);
        uriMatcher.addURI(Zhihu.AUTHORITY, "DailyNews/#", DAILY_NEWS_ID);

        dailyNewsMaps = new ColumnMap();
        dailyNewsMaps.put(Zhihu.DailyNews.ID, Zhihu.DailyNews.ID);
        dailyNewsMaps.put(Zhihu.DailyNews.TITLE, Zhihu.DailyNews.TITLE);
        dailyNewsMaps.put(Zhihu.DailyNews.IMAGE_URL, Zhihu.DailyNews.IMAGE_URL);
        dailyNewsMaps.put(Zhihu.DailyNews.DATE, Zhihu.DailyNews.DATE);
        dailyNewsMaps.put(Zhihu.DailyNews.CONTENT, Zhihu.DailyNews.CONTENT);
    }

    @Override
    public boolean onCreate() {
        mDBHelper = DBHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public synchronized Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (values != null) {
            values.put(BaseColumn._LAST_UPDATE_TIME, System.currentTimeMillis());
        }
        switch (uriMatcher.match(uri)) {
            case DAILY_NEWS:
                long _id = db.insert(DAILY_NEWS_TABLE, Zhihu.DailyNews.ID, values);
                if (_id > 0) {
                    Uri uri1 = ContentUris.withAppendedId(Zhihu.DailyNews.CONTENT_URI, _id);
                    notifyUriIfNeeded(uri1);
                    return uri1;
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return null;
    }

    @Override
    public synchronized int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case DAILY_NEWS:
                count = db.delete(DAILY_NEWS_TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException();
        }
        if (count > 0) {
            notifyUriIfNeeded(uri);
        }
        return count;
    }

    @Override
    public synchronized int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int count;
        if (values != null) {
            values.put(BaseColumn._LAST_UPDATE_TIME, System.currentTimeMillis());
        }
        switch (uriMatcher.match(uri)) {
            case DAILY_NEWS:
                count = db.update(DAILY_NEWS_TABLE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return count;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case DAILY_NEWS:
                sqb.setTables(DAILY_NEWS_TABLE);
                sqb.setProjectionMap(dailyNewsMaps);
                return queryDb(sqb, mDBHelper, uri, projection, selection, selectionArgs, sortOrder);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
