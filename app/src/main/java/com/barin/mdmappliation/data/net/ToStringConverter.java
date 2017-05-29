package com.barin.mdmappliation.data.net;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import retrofit.Converter;

/**
 * Created by barin on 2/13/2016.
 */
public final class ToStringConverter implements Converter<String> {

  @Override public String fromBody(ResponseBody body) throws IOException {
    return body.toString();
  }

  @Override public RequestBody toBody(String value) {
    return RequestBody.create(MediaType.parse("text/plain"), value);
  }
}
