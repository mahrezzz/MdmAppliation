package com.barin.mdmappliation.domain.repository;

import com.barin.mdmappliation.data.net.RegisterTokenResult;
import com.barin.mdmappliation.presentation.model.GcmToken;
import rx.Observable;

/**
 * Created by barin on 3/28/2016.
 */
public interface RegisterTokenRepository {

  Observable<RegisterTokenResult> registerTokenToDatabase(GcmToken gcmToken);
}
