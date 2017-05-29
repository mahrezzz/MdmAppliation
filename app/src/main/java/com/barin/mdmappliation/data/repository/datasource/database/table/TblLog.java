package com.barin.mdmappliation.data.repository.datasource.database.table;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import timber.log.Timber;

/**
 * Created by barin on 2/9/2016.
 */
public class TblLog {

  public static final String TBL_LOG = "logs";
  public static final String LOG_TYPE = "type";
  public static final String LOG_MESSAGE = "message";
  public static final String LOG_DETAIL = "detail";
  public static final String LOG_CREATED_AT = "created_at";
  public static final String TABLE_CRATE = "create table if not exists "
      + TBL_LOG
      +
      "("
      + LOG_TYPE
      + " text, "
      + LOG_MESSAGE
      + " text, "
      + LOG_DETAIL
      + " text, "
      + LOG_CREATED_AT
      + " int)";

  public static final String GET_ALL_ORDER_BY = LOG_CREATED_AT + " DESC";

  public static void createTable(@NonNull SQLiteDatabase db) {
    db.execSQL(TABLE_CRATE);
  }

  public static void upgradeTable(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
    Timber.d(" Upgrading database from version "
        + oldVersion
        + " to "
        + newVersion
        + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TBL_LOG);
    createTable(db);
  }
}
