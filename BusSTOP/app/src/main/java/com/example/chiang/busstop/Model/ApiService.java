package com.example.chiang.busstop.Model;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by chiang on 12/15/2014.
 */
public interface ApiService {

    @GET("/buses")
    public Buses getBus(@Query("apikey") String api, @Query("routeNo") int busNo);
}
