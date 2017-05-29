package com.barin.mdmappliation.data.repository;

import android.net.wifi.WifiManager;
import com.barin.mdmappliation.application.exception.MdmException;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.data.net.GcmCommandRestClient;
import com.barin.mdmappliation.domain.model.UploadMessage;
import com.barin.mdmappliation.domain.repository.UploadMessageToDbRepository;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import timber.log.Timber;

/**
 * Created by barin on 4/16/2016.
 */
@Singleton public class UploadMessageImpl implements UploadMessageToDbRepository {

  private final WifiManager mWifiManager;

  @Inject public UploadMessageImpl(WifiManager wifiManager) {
    mWifiManager = wifiManager;
  }

  @Override public Observable<Void> uploadMessageLogToDb(UploadMessage uploadMessage) {
    AppUtil.checkForNull(uploadMessage, "uploadMessage==null");

    GcmCommandRestClient.GcmCommandApiInterface service =
        GcmCommandRestClient.getGcmCommandApiInterface(Constants.BASE_URL);
    if (service == null) {
      Timber.e("@RegisterTokenImpl", new MdmException("Assertion Error"));
      throw new AssertionError();
    }
    return service.uploadLog(mWifiManager.getConnectionInfo().getMacAddress(), Constants.REMOTE_CONTROL,
        uploadMessage.toString());
  }
}

