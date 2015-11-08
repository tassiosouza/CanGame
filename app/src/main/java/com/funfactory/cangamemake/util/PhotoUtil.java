package com.funfactory.cangamemake.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.google.inject.Singleton;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Class to handle photos functionalities.
 * 
 */
@Singleton
public class PhotoUtil {

    public static final int    CAPTURE_IMAGE     = 100;
    public static final int    RESULT_LOAD_IMAGE = 1;

    private static final float REDUCAO           = (float) 0.20;

    private static PhotoUtil   sInstance;

    public static PhotoUtil getInstance() {
        if (sInstance == null) {
            sInstance = new PhotoUtil();
        }
        return sInstance;
    }

    public String tirarFoto(Activity activity) {

        String result = null;

        if (activity != null) {

            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = Uri.fromFile(getOutputPhotoFile(activity));
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(i, CAPTURE_IMAGE);
            result = uri.toString();
        }

        return result;
    }

    public String getFotoDaGaleria(Activity activity) {

        String result = null;

        if (activity != null) {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Uri uri = Uri.fromFile(getOutputPhotoFile(activity));
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(i, RESULT_LOAD_IMAGE);
            result = uri.toString();
        }

        return result;
    }

    @SuppressLint("SimpleDateFormat")
    private File getOutputPhotoFile(Activity activity) {

        File result = null;

        if (activity != null) {

            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    activity.getPackageName());

            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    return null;
                }
            }

            String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
            File ff = new File(directory.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            result = ff;
        }

        return result;
    }

    public Bitmap decodeBitmap(String imagePath) {

        Bitmap resultado = null;

        if (imagePath != null && !imagePath.isEmpty()) {

            Bitmap bitmapEscalado = null;

            File imageFile = new File(imagePath);

            String uriString = Uri.fromFile(imageFile).toString();
            String uriDecoded = Uri.decode(uriString);

            resultado = ImageLoader.getInstance().loadImageSync(uriDecoded);

            int rotation = getRotationDegrees(imageFile);

            if (rotation != 0) {
                Matrix m = new Matrix();
                m.postRotate(rotation);

                bitmapEscalado = Bitmap.createBitmap(resultado, 0, 0, resultado.getWidth(), resultado.getHeight(), m,
                        true);

                resultado.recycle();
                resultado = bitmapEscalado;
            }
        }

        return resultado;
    }

    public Bitmap createBitmap(String uri) {

        Bitmap bitmapEscalado = null;

        if (uri != null && !uri.isEmpty()) {

            Uri photoUri = Uri.parse(uri);

            if (photoUri != null) {
                String file = photoUri.toString().substring(8);
                File imageFile = new File(file);

                if (imageFile.exists()) {

                    Bitmap bitmapOriginal = ImageLoader.getInstance().loadImageSync(Uri.fromFile(imageFile).toString());

                    bitmapEscalado = Bitmap.createScaledBitmap(bitmapOriginal,
                            (int) (bitmapOriginal.getWidth() * REDUCAO), (int) (bitmapOriginal.getHeight() * REDUCAO),
                            true);

                    int rotation = getRotationDegrees(imageFile);

                    bitmapOriginal.recycle();

                    if (rotation != 0) {
                        Matrix m = new Matrix();
                        m.postRotate(rotation);
                        bitmapOriginal = Bitmap.createBitmap(bitmapEscalado, 0, 0, bitmapEscalado.getWidth(),
                                bitmapEscalado.getHeight(), m, true);

                        bitmapEscalado.recycle();
                        bitmapEscalado = bitmapOriginal;
                    }
                }
            }
        }
        return bitmapEscalado;
    }

    private static int getRotationDegrees(File ff) {

        int result = 0;

        if (ff != null) {
            try {
                ExifInterface exif = new ExifInterface(ff.getAbsolutePath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    result = 270;
                }
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    result = 180;
                }
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    result = 90;
                }
            } catch (FileNotFoundException e) {
                result = 0;
            } catch (IOException e) {
                result = 0;
            }
        }
        return result;
    }
}
