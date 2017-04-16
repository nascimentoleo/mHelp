package com.ifma.appmhelp.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.ifma.appmhelp.lib.FileLib;
import com.ifma.appmhelp.lib.ImageLib;
import com.ifma.appmhelp.services.FileTransfer;

import java.io.File;
import java.io.IOException;

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

    public String enviarArquivoDaCamera(Bitmap bitmap) throws IOException {
        File file = ImageLib.saveImageBitmap(bitmap, this.pathUpload);
        FileTransfer.uploadFile(ctx, file.getPath());

        return file.getName();
    }

    public String enviarArquivo(Uri data) throws IOException {
        String pathFile = FileLib.getPath(ctx, data);
        Bitmap myBitmap = BitmapFactory.decodeFile(pathFile);

        File file = ImageLib.saveImageBitmap(myBitmap, this.pathUpload);

        FileTransfer.uploadFile(ctx, file.getPath());

        return file.getName();
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


    /*public void exibirAnexoNaGaleria(File file){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMG_" + System.currentTimeMillis ());
        values.put(MediaStore.Images.Media.DESCRIPTION, "Imagem tirada via mHelp");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
        values.put(MediaStore.Images.Media.WIDTH, 1836);
        values.put(MediaStore.Images.Media.HEIGHT, 1836);
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
        ContentResolver cr = ctx.getContentResolver();
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    } */

}
