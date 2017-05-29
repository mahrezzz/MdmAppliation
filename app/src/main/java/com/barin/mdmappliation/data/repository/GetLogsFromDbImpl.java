package com.barin.mdmappliation.data.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.application.util.FileUtil;
import com.barin.mdmappliation.presentation.model.AppLog;
import com.barin.mdmappliation.application.MdmApplication;
import com.barin.mdmappliation.data.repository.datasource.database.table.TblLog;
import com.barin.mdmappliation.domain.model.LogInfoDomain;
import com.barin.mdmappliation.domain.repository.LogRepository;
import rx.Observable;

/**
 * Created by barin on 3/10/2016.
 */

@Singleton public class GetLogsFromDbImpl implements LogRepository {

  private Context mContext;

  @Inject public GetLogsFromDbImpl(Context context) {
    mContext = context;
  }

  @Override public Observable<Collection<LogInfoDomain>> getLogs() {

    return Observable.create(subscriber -> {
      try {
        subscriber.onNext(getLogsFromDatabase());
      } catch (FileNotFoundException e) {
        subscriber.onError(e);
      }
      subscriber.onCompleted();
    });
  }

  private Collection<LogInfoDomain> getLogsFromDatabase() throws FileNotFoundException {

    final File dbFile = new File(Constants.DEFAULT_LOGS_PATH + Constants.APP_DATABASE_NAME);
    FileUtil.checkFileExists(dbFile, "Log database yerinde değil.");

    SQLiteDatabase db = ((MdmApplication) mContext).getDb();
    Collection<LogInfoDomain> logs = new ArrayList<>();
    Cursor cursor = null;

    try {
      if (db != null) {
        db.beginTransaction();
        cursor = db.query(false, TblLog.TBL_LOG, new String[] {
            TblLog.LOG_TYPE, TblLog.LOG_MESSAGE, TblLog.LOG_DETAIL, TblLog.LOG_CREATED_AT
        }, null, null, null, null, TblLog.GET_ALL_ORDER_BY, "");

        if (cursor.getCount() > 0) {
          LogInfoDomain appLog;
          while (cursor.moveToNext()) {
            AppLog.LogType logType =
                cursor.getString(cursor.getColumnIndex(TblLog.LOG_TYPE)).equalsIgnoreCase("E")
                    ? AppLog.LogType.ERROR : AppLog.LogType.INFO;
            appLog = new LogInfoDomain(cursor.getString(cursor.getColumnIndex(TblLog.LOG_MESSAGE)),
                cursor.getString(cursor.getColumnIndex(TblLog.LOG_DETAIL)), logType,
                cursor.getString(cursor.getColumnIndex(TblLog.LOG_CREATED_AT)));
            logs.add(appLog);
          }
        }
        db.setTransactionSuccessful();
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
      if (db != null) {
        db.endTransaction();
      }
    }

    return logs;
  }
}
