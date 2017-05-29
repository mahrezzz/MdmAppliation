package com.barin.mdmappliation.data.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.barin.mdmappliation.application.MdmApplication;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.domain.repository.DeleteRecordsRepository;
import rx.Observable;

/**
 * Created by barin on 4/6/2016.
 */
@Singleton public class DeleteTableRecordsImpl implements DeleteRecordsRepository {

  //TODO: later get inputs  as delete design with where selection clauses to create more flexible creation environment

  private final Context mContext;

  @Inject public DeleteTableRecordsImpl(Context context) {
    mContext = context;
  }

  @Override public Observable<Integer> deleteLogs(String tableName) {
    AppUtil.checkForNull(tableName, "table name==null");

    return Observable.create(subscriber -> {
      subscriber.onNext(deleteLogsInternally(tableName));
      subscriber.onCompleted();
    });
  }

  private int deleteLogsInternally(@NonNull String tableName) {

    int count = 0;
    SQLiteDatabase db = ((MdmApplication) mContext).getDb();
    try {
      if (db != null) {
        db.beginTransaction();
        count = db.delete(tableName, null, null);
        db.setTransactionSuccessful();
      }
    } finally {
      if (db != null) db.endTransaction();
    }

    return count;
  }
}
