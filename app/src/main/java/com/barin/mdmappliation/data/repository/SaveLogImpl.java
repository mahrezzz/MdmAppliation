package com.barin.mdmappliation.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.barin.mdmappliation.presentation.model.AppLog;
import com.barin.mdmappliation.application.MdmApplication;
import com.barin.mdmappliation.data.repository.datasource.database.table.TblLog;
import com.barin.mdmappliation.domain.repository.SaveLogRepository;
import rx.Observable;

@Singleton public class SaveLogImpl implements SaveLogRepository {

  private static SimpleDateFormat dateFormat;
  private static ContentValues contentValues;

  static {
    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    contentValues = new ContentValues();
  }

  private final Context mContext;

  @Inject public SaveLogImpl(Context context) {
    mContext = context;
  }

  @Override public Observable<Boolean> saveLog(AppLog log) {
    return Observable.create(subscriber -> {
      subscriber.onNext(logToDb(log));
      subscriber.onCompleted();
    });
  }

  private Boolean logToDb(AppLog log) {
    contentValues.clear();
    contentValues.put(TblLog.LOG_TYPE, log.getmType().type.equals("E") ? "E" : "I");
    contentValues.put(TblLog.LOG_MESSAGE, log.getMessage());
    contentValues.put(TblLog.LOG_DETAIL, log.getmDetail());
    contentValues.put(TblLog.LOG_CREATED_AT, dateFormat.format(new Date()));

    SQLiteDatabase db = ((MdmApplication) mContext).getDb();
    try {
      if (db != null) {
        db.beginTransaction();
        long row_id = db.insertWithOnConflict(TblLog.TBL_LOG, null, contentValues,
            SQLiteDatabase.CONFLICT_IGNORE);
        db.setTransactionSuccessful();
        if (row_id > -1) {
          return true;
        }
      }
    } finally {
      if (db != null) db.endTransaction();
    }
    return false;
  }
}
