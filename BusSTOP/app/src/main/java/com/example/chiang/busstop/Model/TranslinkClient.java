package com.example.chiang.busstop.Model;

import retrofit.RestAdapter;

/**
 * Created by chiang on 12/17/2014.
 */
public class TranslinkClient {

    private static ApiService data;

    public static ApiService getBusesData() {
        if (data == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.translink.ca/rttiapi/v1")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            data = restAdapter.create(ApiService.class);
        }
        return data;
    }
}
