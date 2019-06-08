package com.sourcey.materiallogindemo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ListTask {
    @POST("/tasks")
    Call<ParamsBody> createTask(@Body ParamsBody params);
}
