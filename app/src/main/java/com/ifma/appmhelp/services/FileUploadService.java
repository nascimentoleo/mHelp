package com.ifma.appmhelp.services;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by leo on 3/27/17.
 */

public interface FileUploadService {

    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(
            @Part MultipartBody.Part file
    );
}
