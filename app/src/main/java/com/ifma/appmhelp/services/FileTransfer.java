package com.ifma.appmhelp.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ifma.appmhelp.lib.ClientHTTP;
import com.ifma.appmhelp.lib.FileLib;
import com.ifma.appmhelp.models.Host;

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

        FileService service = FileTransfer.createService();
        File file = new File(path);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<ResponseBody> call = service.upload(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "Arquivo Enviado");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload", t.getMessage());
            }
        });
    }

    public static void downloadFile(final Context ctx, final String url, final String storagePath){
        FileService downloadService = FileTransfer.createService();

        Call<ResponseBody> call = downloadService.download(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            boolean salvou = FileLib.writeResponseBodyToDisk(response.body(), storagePath);

                            if (salvou) {
                                FileTransfer.deleteFile(ctx, storagePath);
                                Log.d("Download", "Arquivo salvo");
                            }else
                                Log.d("Download", "Arquivo não salvo");

                            return null;
                        }
                    }.execute();

                } else {
                    Log.d("Download", "Conexão ao servidor falhou");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Download", t.getMessage());
            }
        });
    }

    private static void deleteFile(Context ctx, String path) {

        FileService service = FileTransfer.createService();
        File file = new File(path);

        if (file.exists()){
            Call<ResponseBody> call = service.delete(file.getName());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {
                    Log.v("Delete", "Arquivo Deletado");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Delete", t.getMessage());
                }
            });
        }

    }


    private static FileService createService(){
        OkHttpClient client = ClientHTTP.getHTTPClient();
        Retrofit retrofit = new Retrofit.Builder()
                //.client(client)
                .baseUrl("http://" + new Host().getEndereco() + ":8000/")
                .build();

        return  retrofit.create(FileService.class);
    }

}
