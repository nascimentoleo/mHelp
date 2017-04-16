package com.ifma.appmhelp.services;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by leo on 3/27/17.
 */

public interface FileService {

    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(
            @Part MultipartBody.Part file
    );

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String fileUrl);


}
