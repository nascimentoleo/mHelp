package com.ifma.appmhelp.services;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.lib.FileLib;

import okhttp3.ResponseBody;

/**
 * Created by leo on 4/16/17.
 */

public class DownloadAnexoTask extends AsyncTask <ResponseBody, Integer, Boolean> {

    private Context ctx;
    private String storagePath;

    public DownloadAnexoTask(Context ctx, String storagePath) {
        this.ctx = ctx;
        this.storagePath = storagePath;
    }

    @Override
    protected Boolean doInBackground(ResponseBody... params) {
        return FileLib.writeResponseBodyToDisk(params[0], storagePath);
    }

    @Override
    protected void onPostExecute(Boolean salvou) {

        if (salvou) {
            FileTransfer.deleteFile(ctx, storagePath);

            Intent it = new Intent(IntentType.ATUALIZAR_MENSAGENS.toString());
            LocalBroadcastManager.getInstance(ctx).sendBroadcast(it);

            Log.d("Download", "Arquivo salvo");
        }else
            Log.d("Download", "Arquivo não salvo");

    }
}
