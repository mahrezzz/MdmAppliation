package com.barin.mdmappliation.data.net;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by barin on 2/22/2016.
 */
public class RegisterResponse {

  @SerializedName("rs") @Expose private String rs;
  @SerializedName("rc") @Expose private String rc;
  @SerializedName("rm") @Expose private String rm;

  @Override public String toString() {
    return "Response{" +
        "rs='" + rs + '\'' +
        ", rc='" + rc + '\'' +
        ", rm='" + rm + '\'' +
        '}';
  }
}
