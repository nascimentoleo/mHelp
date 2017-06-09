package com.ifma.appmhelp.lib;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created by leo on 5/24/17.
 */

public class QrCodeLib {

    public static Bitmap montaQrCode(String texto, int height, int width){

        Bitmap bmp = null;
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(texto, BarcodeFormat.QR_CODE,width,height);
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int i = 0; i < width; i ++)
                for (int j = 0; j < height; j ++)
                    bmp.setPixel(i, j, bitMatrix.get(i,j) ? Color.BLACK : Color.WHITE);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bmp;
    }
}
