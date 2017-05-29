package com.barin.mdmappliation.data.net;

import android.support.annotation.Nullable;
import com.squareup.okhttp.OkHttpClient;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

public class GcmCommandRestClient {

  public interface GcmCommandApiInterface {
    @POST("/Notifications/rest/register") Observable<RegisterTokenResult> registerToken(@Query("mi") String mac,
        @Query("tk") String token, @Query("st") String type);

    @POST("/Notifications/rest/log") Observable<Void> uploadLog(@Query("mi") String mac, @Query("st") String sourceType,
        @Query("em") String message);
  }

  @Nullable public static GcmCommandApiInterface getGcmCommandApiInterface(String baseUrl) {

    OkHttpClient okHttpClient = UnSafeOkHttpClient.getUnsafeOkHttpClient();
    okHttpClient.setHostnameVerifier((hostname, session) -> true);
    okHttpClient.interceptors().add(chain -> chain.proceed(chain.request()));

    Retrofit client = new Retrofit.Builder().baseUrl(baseUrl)
        .addConverter(String.class, new ToStringConverter())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return client.create(GcmCommandApiInterface.class);
  }
}
