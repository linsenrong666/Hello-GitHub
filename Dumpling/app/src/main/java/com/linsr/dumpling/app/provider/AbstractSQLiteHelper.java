package com.linsr.dumpling.app.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.linsr.dumpling.log.Log;
import com.linsr.dumpling.log.LogImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 抽象SQLiteOpenHelper类，封装一些数据库升级操作，创建表的操作
 *
 * @author Linsr
 */
public abstract class AbstractSQLiteHelper extends SQLiteOpenHelper {

    protected String TAG;
    protected Log mLog;

    /**
     * 数据库创建
     *
     * @param db db
     */
    protected abstract void onCreateEx(SQLiteDatabase db);

    /**
     * 数据库升级
     *
     * @param db     db
     * @param tables 当前库中所含有的所有表名
     */
    protected abstract void onUpgradeEx(SQLiteDatabase db, List<String> tables);

    /**
     * 构造方法
     *
     * @param context   上下文
     * @param dbName    数据库名称
     * @param dbVersion 数据库版本
     */
    public AbstractSQLiteHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
        mLog = LogImpl.getInstance();
        TAG = getClass().getSimpleName();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mLog.i(TAG, "[" + TAG + "]onCreate.called");
        db.beginTransaction();

        //创建表
        onCreateEx(db);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mLog.i(TAG, "[" + TAG + "]onUpgrade.called,oldVersion = %s ,newVersion = %s ", oldVersion, newVersion);
        db.beginTransaction();

        //获取库中的表名
        List<String> tables = getTableNames(db);
        //对比字段等更新操作
        onUpgradeEx(db, tables);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 数据库类型
     *
     * @return text 类型数据
     */
    protected final String text() {
        return " TEXT";
    }

    /**
     * 数据库类型
     *
     * @param defaultValue 默认值
     * @return 带默认值的 text 类型
     */
    protected final String text(String defaultValue) {
        return " TEXT DEFAULT " + defaultValue;
    }

    /**
     * 数据库类型
     *
     * @return integer 类型数据
     */
    protected final String integer() {
        return " INTEGER";
    }

    /**
     * 数据库类型
     *
     * @param defaultValue 默认值
     * @return 带默认值的 integer 类型
     */
    protected final String integer(int defaultValue) {
        return " INTEGER DEFAULT " + defaultValue;
    }

    /**
     * 数据库类型
     *
     * @return 主键类型
     */
    protected final String primary() {
        return " INTEGER PRIMARY KEY";
    }

    /**
     * 加工表中的各个列，加入一些必须有或者公有的列，比如:"_id"
     *
     * @return 加工好的columns
     */
    protected Map<String, String> newTableColumns() {
        Map<String, String> columns = new HashMap<>();
        columns.put(BaseColumn._ID, primary());
        columns.put(BaseColumn._LAST_UPDATE_TIME, integer(0));
        return columns;
    }

    /**
     * 创建表
     *
     * @param db           db
     * @param tableName    表名
     * @param tableColumns 列
     */
    protected void createTable(SQLiteDatabase db,
                               String tableName,
                               Map<String, String> tableColumns) {
        db.execSQL("CREATE TABLE " + tableName + " (" + formatColumns(tableColumns) + ");");
    }

    /**
     * 对比新旧字段，如果有新字段则增加
     *
     * @param db         db
     * @param tables     数据库中包含的所有表名
     * @param tableName  表名
     * @param newColumns 表中包含的列
     */
    protected void compareTable(SQLiteDatabase db,
                                List<String> tables,
                                String tableName,
                                Map<String, String> newColumns) {
        //如果现有库中，包含这张表，则进行对比
        if (tables.contains(tableName)) {
            //取老的字段
            Cursor cursor = null;
            try {
                cursor = db.query(tableName, null, null, null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    List<String> oldColumns = Arrays.asList(cursor.getColumnNames());
                    //和新的字段对比
                    for (Map.Entry<String, String> entry : newColumns.entrySet()) {
                        String columnName = entry.getKey();
                        String columnType = entry.getValue();

                        if (!oldColumns.contains(columnName)) {
                            db.execSQL("ALTER TABLE " + tableName + " ADD " + columnName + " " + columnType);
                            mLog.i(TAG, "ALTER TABLE %s ,ADD COLUMN %s", tableName, columnName);
                        }
                    }
                } else {
                    db.execSQL("DROP TABLE IF EXISTS " + tableName);
                    createTable(db, tableName, newColumns);
                    mLog.i(TAG, "DROP TABLE %s", tableName);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        } else {
            createTable(db, tableName, newColumns);
        }
    }

    //格式化列，转换成创建表时所需要的String
    private String formatColumns(Map<String, String> table) {
        String columns = "";
        //spell the column string
        for (Map.Entry<String, String> entry : table.entrySet()) {
            columns = columns + entry.getKey() + " " + entry.getValue() + " ,";
        }
        //remove the last ","
        if (columns.length() > 0) {
            columns = columns.substring(0, columns.length() - 1);
        }
        return columns;
    }

    //获取数据库中所有表的表名
    private List<String> getTableNames(SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.query("sqlite_master", new String[]{"name"}, " type = ? ",
                    new String[]{"table"}, null, null, null);
            List<String> tables = new ArrayList<>();
            while (cursor.moveToNext()) {
                tables.add(cursor.getString(0));
            }
            return tables;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

//    private void updateDB(SQLiteDatabase db, String tableName,HashMap<String,String> newColumns)
//    {
//        //重命名
//        String temp = tableName + "_temp";
//        db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + temp);
//        //新建表
//        createTable(db,tableName,newColumns);
//        //复制数据
//        String sql = "INSERT INTO " + tableName +
//                " ("
//                + STUDENT[0] + " , "
//                + STUDENT[1] + " , "
//                + STUDENT[2] + " , "
//                + STUDENT[3] + " "
//                + ") "
//                + " SELECT "
//                + STUDENT[0] + " , "
//                + STUDENT[1] + " , "
//                + STUDENT[2] + " , "
//                + STUDENT[3] + " "
//                + " FROM " + temp;
//
//        db.execSQL(sql);
//        //删除临时表
//        db.execSQL(" drop table " + temp);
//    }


