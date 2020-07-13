package ru.nsd.proxy;

import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import java.io.IOException;
import java.util.List;

@RestController
@Log4j2
public class SdClient {
  private static final String DEFAULT_URL = "https://_____ru/";
  private static final String REDIRECT_URL = "https://______.ru/operator/#uuid:";
  private static final String SD_URL = "https://_______.ru/services/rest/search/employee$employee/";
  private final OkHttpClient okHttpClient;

  public SdClient(OkHttpClient okHttpClient) {
    this.okHttpClient = okHttpClient;
  }


  private SdAPI getRetrofitAPI() {
    return getRetrofit(SD_URL).create(SdAPI.class);
  }

  private Retrofit getRetrofit(String url) {
    return new Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(url)
        .addConverterFactory(JacksonConverterFactory.create())
        .build();
  }

  private String getUserIdByExternalPhone(String phone) {
    return execute(getRetrofitAPI().getUserIdByExternalPhone(phone));
  }

  private String getUserIdByInternalPhone(String phone) {
    return execute(getRetrofitAPI().getUserIdByInternalPhone(phone));
  }


  private String execute(Call<List<String>> call) {
    try {
      Response<List<String>> response = call.execute();
      if (response.isSuccessful() && response.body() != null && !response.body().isEmpty())
        return response.body().get(0);

      return "";
    } catch (IOException e) {
      return "";
    }
  }

  @GetMapping("/find")
  ModelAndView getReport(@RequestParam(value = "phone") String phone) {
    String userId = "";

    if (phone != null) {
      if (phone.length() == 4) {
        userId = getUserIdByInternalPhone(phone);
      } else if (phone.length() >= 10) {
        userId = getUserIdByExternalPhone(phone);
      }
    }

    if (!userId.isEmpty()) {
      return new ModelAndView("redirect:" + REDIRECT_URL + userId);
    }

    return new ModelAndView("redirect:" + DEFAULT_URL);
  }
}

