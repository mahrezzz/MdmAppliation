package com.barin.mdmappliation.domain.repository;

import com.barin.mdmappliation.domain.model.TokenDomain;
import rx.Observable;

/**
 * Created by barin on 3/25/2016.
 */
public interface GetTokenRepository {

  Observable<TokenDomain> getToken();


}
