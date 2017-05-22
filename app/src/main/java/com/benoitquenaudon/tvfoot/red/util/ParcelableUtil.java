package com.benoitquenaudon.tvfoot.red.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * see: http://stackoverflow.com/questions/18000093/how-to-marshall-and-unmarshall-a-parcelable-to-a-byte-array-with-help-of-parcel/18000094#18000094
 * Custom classes passed as extra are not "supported" in Nougat or something like that
 * so we use something Android knows, byte[]
 */
public final class ParcelableUtil {
  public static byte[] marshall(Parcelable parcelable) {
    Parcel parcel = Parcel.obtain();
    parcelable.writeToParcel(parcel, 0);
    byte[] bytes = parcel.marshall();
    parcel.recycle();
    return bytes;
  }

  public static Parcel unmarshall(byte[] bytes) {
    Parcel parcel = Parcel.obtain();
    parcel.unmarshall(bytes, 0, bytes.length);
    parcel.setDataPosition(0);
    return parcel;
  }

  public static <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
    Parcel parcel = unmarshall(bytes);
    T result = creator.createFromParcel(parcel);
    parcel.recycle();
    return result;
  }
}