package com.barin.mdmappliation.presentation.model.mapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.domain.model.TokenDomain;
import com.barin.mdmappliation.presentation.model.GcmToken;

/**
 * Created by barin on 3/25/2016.
 */
@Singleton public class GcmTokenMapper {

  @Inject public GcmTokenMapper() {

  }

  public GcmToken transform(TokenDomain token) {
    AppUtil.checkForNull(token, "token==null");
    return new GcmToken(token.getToken());
  }
}
