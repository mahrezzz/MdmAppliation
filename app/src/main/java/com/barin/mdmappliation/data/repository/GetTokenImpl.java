package com.barin.mdmappliation.data.repository;

import android.content.Context;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.barin.mdmappliation.domain.model.TokenDomain;
import com.barin.mdmappliation.domain.repository.GetTokenRepository;
import rx.Observable;

/**
 * Created by barin on 3/25/2016.
 */

@Singleton public class GetTokenImpl implements GetTokenRepository {
  private final Context mContext;

  @Inject public GetTokenImpl(Context context) {   //the context here is the application context
    mContext = context;
  }

  @Override public Observable<TokenDomain> getToken() {
    return Observable.create(subscriber -> {
      InstanceID instanceID = InstanceID.getInstance(mContext);
      String token;
      try {
        token = instanceID.getToken("388460080928", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        subscriber.onNext(new TokenDomain(token));
      } catch (IOException e) {
        subscriber.onError(e);
      }
      subscriber.onCompleted();
    });
  }
}
