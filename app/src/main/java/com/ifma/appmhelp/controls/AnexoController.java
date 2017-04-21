package com.ifma.appmhelp.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.ifma.appmhelp.enums.TipoAnexo;
import com.ifma.appmhelp.lib.FileLib;
import com.ifma.appmhelp.lib.MediaLib;
import com.ifma.appmhelp.services.FileTransfer;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;

/**
 * Created by leo on 4/12/17.
 */

public class AnexoController extends BaseController{

    private String pathUpload;
    private String pathDownload;

    public AnexoController(Context ctx) {
        super(ctx);
        pathUpload   = ctx.getFilesDir().getPath();
        pathDownload = "/download/";

    }

    public String enviarArquivo(TipoAnexo tipoAnexo, Uri data) throws IOException {
        File file;
        MediaType mediaType;

        String pathFile = FileLib.getPath(ctx, data);

        if (tipoAnexo == TipoAnexo.IMAGEM){
            Bitmap myBitmap = BitmapFactory.decodeFile(pathFile);
            file = MediaLib.saveImageBitmap(myBitmap, this.pathUpload);
            mediaType = MediaType.parse("image/*");
        }else{
            file = MediaLib.saveVideoMP4(new File(pathFile), this.pathUpload);
            mediaType = MediaType.parse("video/*");
        }

        if (file != null) {
            FileTransfer.uploadFile(file.getPath(), mediaType);
            return file.getName();
        }

        return null;
    }



    public File carregaAnexo(String nome){
        File anexo = this.getFile(nome);

        if (anexo != null)
            return anexo;

        return this.baixarAnexo(nome);

    }

    private File baixarAnexo(String nome) {
        String url         = this.pathDownload + nome;
        String storagePath = this.pathUpload + "/" + nome;

        FileTransfer.downloadFile(ctx, url, storagePath);

        return getFile(nome);
    }

    private File getFile(String nome){
        File file = new File(this.pathUpload + "/" + nome);

        if(file.exists())
            return file;

        return null;

    }
}
