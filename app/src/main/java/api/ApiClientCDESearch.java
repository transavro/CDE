package api;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientCDESearch {
    public static String BASE_URL;
    private static Retrofit retrofit = null;


    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            BASE_URL = "https://dev.cloudwalker.tv/";
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(CustomHttpClient.getHttpClient(context, BASE_URL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

//    public static Retrofit getClient(Context context) {
//
//
//
//        try {
//            BASE_URL = "https://dev.cloudwalker.tv/";
//
//            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
//                    .callTimeout(5, TimeUnit.SECONDS)
//                    .connectTimeout(5, TimeUnit.SECONDS);
//
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            clientBuilder.addInterceptor(loggingInterceptor);
//
//            if (retrofit == null) {
//                Gson gson = new GsonBuilder()
//                        .setLenient()
//                        .create();
//                retrofit = new Retrofit.Builder()
//                        .baseUrl(BASE_URL)
//                        .client(CustomHttpClient.getHttpClient(context, BASE_URL))
//                        .addConverterFactory(GsonConverterFactory.create(gson))
//                        .build();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return retrofit;
//    }


    public static class SubUrls {

        public static final String SEARCH = "search";

    }

}
