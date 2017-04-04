package com.ifma.appmhelp.services;

import android.content.Context;
import android.util.Log;

import com.ifma.appmhelp.lib.ClientHTTP;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by leo on 3/27/17.
 */

public class FileTransfer {

    public static void uploadFile(Context ctx, String path) {
        // create upload service client
        OkHttpClient client = ClientHTTP.getHTTPClient();
        Retrofit retrofit = new Retrofit.Builder()
                //.client(client)
                .baseUrl("http://192.168.0.8:8080/")
                .build();

        FileUploadService service = retrofit.create(FileUploadService.class);
        File file = new File(path);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // finally, execute the request
        Call<ResponseBody> call = service.upload(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "Arquivo Enviado");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

}
