package com.barin.mdmappliation.data.net;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by barin on 2/22/2016.
 */
public class RegisterTokenResult {

  @SerializedName("success") @Expose private String success;
  @SerializedName("response") @Expose private RegisterResponse response=null;

  public String getSuccess() {
    return success;
  }

  public void setSuccess(String success) {
    this.success = success;
  }

  public RegisterResponse getResponse() {
    return response;
  }

  public void setResponse(RegisterResponse response) {
    this.response = response;
  }

  @Override public String toString() {
    return "RegisterTokenResult{" +
        "success='" + success + '\'' +
        ", response=" + response +
        '}';
  }
}

