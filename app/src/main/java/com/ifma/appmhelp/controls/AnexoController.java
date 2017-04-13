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

    private String path;

    public AnexoController(Context ctx) {
        super(ctx);
        path = ctx.getFilesDir().getPath();

    }

    public String enviarArquivoDaCamera(Bitmap bitmap) throws IOException {
        File file = ImageLib.saveImageBitmap(bitmap, this.path);
        FileTransfer.uploadFile(ctx, file.getPath());

        return file.getName();
    }

    public String enviarArquivo(Uri data) throws IOException {
        String pathFile = FileLib.getPath(ctx, data);
        Bitmap myBitmap = BitmapFactory.decodeFile(pathFile);

        File file = ImageLib.saveImageBitmap(myBitmap, this.path);

        FileTransfer.uploadFile(ctx, file.getPath());

        return file.getName();
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
