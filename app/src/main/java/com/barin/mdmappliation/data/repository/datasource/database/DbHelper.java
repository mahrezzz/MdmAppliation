package com.barin.mdmappliation.data.repository.datasource.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.data.repository.datasource.database.table.TblLog;

/**
 * Created by barin on 2/9/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

  public DbHelper(Context context) {

    super(context, Constants.DEFAULT_LOGS_PATH + Constants.APP_DATABASE_NAME, null,
        Constants.DATABASE_VERSION);
  }

  @Override public void onOpen(SQLiteDatabase db) {

    db.execSQL("pragma foreign_keys=true");
  }

  @Override public void onCreate(SQLiteDatabase db) {
    AppUtil.checkForNull(db, "SQLite Database cannot be null!!!");
    TblLog.createTable(db);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    TblLog.upgradeTable(db, oldVersion, newVersion);
  }
}
