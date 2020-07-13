package ru.nsd.proxy;


import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SdAPI {
  @GET("{'internalPhoneNumber':'{phone}','removed':false}?accessKey=00000000000-eccf-4622-a720-00000000000")
  Call<List<String>> getUserIdByInternalPhone(@Path("phone") String phone);

  @GET("{'mobilePhoneNumber':'{phone}','removed':false}?accessKey=00000000000-eccf-4622-a720-00000000000")
  Call<List<String>> getUserIdByExternalPhone(@Path("phone") String phone);
}
