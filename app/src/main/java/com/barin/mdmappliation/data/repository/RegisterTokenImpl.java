package com.barin.mdmappliation.data.repository;

import com.barin.mdmappliation.application.exception.MdmException;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.data.net.GcmCommandRestClient;
import com.barin.mdmappliation.data.net.RegisterTokenResult;
import com.barin.mdmappliation.domain.repository.RegisterTokenRepository;
import com.barin.mdmappliation.presentation.model.GcmToken;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import timber.log.Timber;

/**
 * Created by barin on 3/28/2016.
 */
@Singleton public class RegisterTokenImpl implements RegisterTokenRepository {

  @Inject public RegisterTokenImpl() {

  }

  @Override public Observable<RegisterTokenResult> registerTokenToDatabase(GcmToken gcmToken) {
    AppUtil.checkForNull(gcmToken, "gcmToken==null");
    GcmCommandRestClient.GcmCommandApiInterface service =
        GcmCommandRestClient.getGcmCommandApiInterface(Constants.BASE_URL);

    if (service == null) {
      Timber.e("@RegisterTokenImpl",new MdmException("Assertion Error"));
      throw new AssertionError();
    }

    return service.registerToken(gcmToken.getMacId(), gcmToken.getToken(),
        Constants.REMOTE_CONTROL);
  }
}
