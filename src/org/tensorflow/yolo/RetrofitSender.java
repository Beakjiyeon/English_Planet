package org.tensorflow.yolo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSender {
    public static final String BASE_URL = "http://ec2-52-14-178-78.us-east-2.compute.amazonaws.com/";
    private static Retrofit retrofit = null;
    private static NetworkService networkService = null;


    public static NetworkService getNetworkService(){
        if(networkService ==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            networkService = retrofit.create(NetworkService.class);
        }
        return networkService;
    }


}
