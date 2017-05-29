package com.barin.mdmappliation.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by barin on 3/25/2016.
 */
public class GcmToken implements Parcelable {

  private String token;
  private String macId;

  public void setMacId(String macId) {
    this.macId = macId;
  }

  public GcmToken(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public String getMacId() {
    return macId;
  }

  // PARCEL PART IF YOU WANT
  public GcmToken(Parcel in) {
    token = in.readString();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(token);
  }

  public static final Creator CREATOR = new Creator() {
    public GcmToken createFromParcel(Parcel in) {
      return new GcmToken(in);
    }

    public GcmToken[] newArray(int size) {
      return new GcmToken[size];
    }
  };
}
