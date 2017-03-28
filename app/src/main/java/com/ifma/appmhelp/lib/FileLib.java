package com.ifma.appmhelp.lib;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by leo on 3/27/17.
 */

public class FileLib {

    public static String getPath(Context ctx, Uri uri) {
        String[]  data = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(ctx, uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
