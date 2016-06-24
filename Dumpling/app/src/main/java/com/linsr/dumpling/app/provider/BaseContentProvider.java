package com.linsr.dumpling.app.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linsr
 */
abstract public class BaseContentProvider extends ContentProvider implements ContentProviderDecorator {

    private List<Uri> mMonitoringUris = new ArrayList<>();

    public synchronized void startBatchNotification(Uri... uris) {
        if (uris != null && uris.length > 0) {
            for (Uri uri : uris)
                if (!mMonitoringUris.contains(uri)) {
                    mMonitoringUris.add(uri);
                }
        }
    }

    public synchronized void endBatchNotification() {
        if (mMonitoringUris.size() > 0) {
            for (int idx = 0; idx < mMonitoringUris.size(); idx++) {
                getContext().getContentResolver().notifyChange(mMonitoringUris.get(idx), null);
            }
            mMonitoringUris.clear();
        }
    }

    private boolean containsUri(Uri uri) {
        for (int idx = 0; idx < mMonitoringUris.size(); idx++) {
            if (uri.toString().startsWith(mMonitoringUris.get(idx).toString())) {
                return true;
            }
        }
        return false;
    }

    protected void notifyUriIfNeeded(Uri uri) {
        if (uri != null) {
            if (mMonitoringUris.size() == 0 || !containsUri(uri)) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
    }

    protected Cursor queryDb(SQLiteQueryBuilder sqb,
                             SQLiteOpenHelper helper,
                             Uri uri,
                             String[] projection,
                             String selection,
                             String[] selectionArgs,
                             String sortOrder) {

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = sqb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    /**
     * 创建默认的where语句，用于delete和update
     */
    protected String buildDefaultWhere(String _id, String id, String selection) {
        return _id + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
    }

    //根据id删除数据库中的数据
    protected int deleteDB(SQLiteDatabase db, String tableName,
                           String _id, String id, String selection,
                           String[] selectionArgs) {
        return db.delete(tableName, buildDefaultWhere(_id, id, selection), selectionArgs);
    }

    //根据id更新数据库中的数据
    protected int updateDB(SQLiteDatabase db, ContentValues values, String tableName,
                           String _id, String id, String selection,
                           String[] selectionArgs) {
        return db.update(tableName, values, buildDefaultWhere(_id, id, selection), selectionArgs);
    }

    /**
     * 更新修改时间
     *
     * @param values
     * @param columnName
     */
    protected void putUpdateTime(ContentValues values, String columnName) {
        if (values != null) {
            values.put(columnName, System.currentTimeMillis());
        }
    }

}
