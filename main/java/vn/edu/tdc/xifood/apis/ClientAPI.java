package vn.edu.tdc.xifood.apis;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import vn.edu.tdc.xifood.models.SmsRequest;

public class ClientAPI {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl, String authToken) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        okhttp3.Request original = chain.request();
                        okhttp3.Request request = original.newBuilder()
                                .header("Authorization", "App " + authToken)
                                .build();
                        return chain.proceed(request);
                    })
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    //cấu hình cho retrofit
    public interface SmsApiService {
        @Headers({
                "Content-Type: application/json",
                "Accept: application/json"
        })
        @POST("/sms/2/text/advanced/") //chổ này là đường dẫn chỉ tới api của Infobip
        Call<Void> sendSms(@Body SmsRequest smsRequest);
    }

}
