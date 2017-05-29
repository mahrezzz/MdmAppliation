package com.barin.mdmappliation.presentation.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

/**
 * Created by barin on 3/17/2016.
 */

@Scope @Retention(RetentionPolicy.RUNTIME) public @interface PerService {
}
