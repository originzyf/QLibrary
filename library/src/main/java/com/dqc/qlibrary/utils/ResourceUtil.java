package com.dqc.qlibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Android 资源相关工具类
 *
 * @author DragonsQC
 */
public class ResourceUtil {
    private static DisplayMetrics sDisplayMetrics;

    /**
     * 获取Drawable下的图片资源
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Drawable getAssets4Drawable(Context context, String fileName) {
        Drawable drawable = null;
        try {
            InputStream is = context.getApplicationContext().getAssets().open(fileName);
            drawable = Drawable.createFromStream(is, null);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * 获取Assets下的图片资源
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap4Assets(Context context, String fileName) {

        Bitmap                bitmap = null;
        BitmapFactory.Options opt    = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        if (android.os.Build.VERSION.SDK_INT < 21) {
            opt.inPurgeable = true;
            opt.inInputShareable = true;
        }
        try {
            InputStream is = context.getApplicationContext().getAssets().open(fileName);
            bitmap = BitmapFactory.decodeStream(is, null, opt);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获取Res下的图片资源
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap getBitmap4Res(Context context, int resId) {

        Bitmap                bitmap = null;
        BitmapFactory.Options opt    = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        if (android.os.Build.VERSION.SDK_INT < 21) {
            opt.inPurgeable = true;
            opt.inInputShareable = true;
        }
        try {
            InputStream is = context.getApplicationContext().getResources().openRawResource(resId);
            bitmap = BitmapFactory.decodeStream(is, null, opt);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * copy assets 下的文件
     *
     * @param context
     * @param sourcePath
     * @param targetPathString
     */
    public static void copyAssetsFile(Context context, String sourcePath, String targetPathString) {
        InputStream  inputStream;
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(targetPathString);
            inputStream = context.getApplicationContext().getAssets().open(sourcePath);
            byte[] buffer = new byte[1024];
            int    length = inputStream.read(buffer);
            while (length > 0) {
                outputStream.write(buffer, 0, length);
                length = inputStream.read(buffer);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取对应Uri的图片资源
     *
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap getBitmap4Uri(Context context, Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), uri);
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * init display metrics
     *
     * @param context
     */
    private static synchronized void initDisplayMetrics(Context context) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    /**
     * get screen width
     *
     * @param context
     * @return
     */
    public static int getDisplayWidth(Context context) {
        initDisplayMetrics(context.getApplicationContext());
        return sDisplayMetrics.widthPixels;
    }

    /**
     * get screen height
     *
     * @param context
     * @return
     */
    public static int getDisplayHeight(Context context) {
        initDisplayMetrics(context.getApplicationContext());
        return sDisplayMetrics.heightPixels;
    }

    /**
     * get screen density
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        initDisplayMetrics(context.getApplicationContext());
        return sDisplayMetrics.density;
    }


    /**
     * 创建 圆形图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createRoundBitmap(Bitmap bitmap) {
        int    size   = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int   color   = 0xff424242;
        final Paint paint   = new Paint();
        final Rect  rect    = new Rect(0, 0, size, size);
        final float roundPx = size / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 创建 圆形图片 带白色边框
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createRoundBitmapWithWhiteBorder(Bitmap bitmap) {
        bitmap = createRoundBitmap(bitmap);
        return addRoundeBitmapWhiteBorder(bitmap);
    }


    /**
     * 给 圆形图 添加 白色边框
     *
     * @param bitmap
     * @return
     */
    public static Bitmap addRoundeBitmapWhiteBorder(Bitmap bitmap) {
        int    size    = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
        int    num     = 14;
        int    sizebig = size + num;
        Bitmap output  = Bitmap.createBitmap(sizebig, sizebig, Bitmap.Config.ARGB_8888);
        Canvas canvas  = new Canvas(output);

        final int   color   = Color.parseColor("#FFFFFF");
        final Paint paint   = new Paint();
        final float roundPx = sizebig / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        RadialGradient gradient = new RadialGradient(roundPx, roundPx, roundPx,
                new int[]{Color.WHITE, Color.WHITE, Color.parseColor("#AAAAAAAA")},
                new float[]{0.f, 0.97f, 1.0f}, Shader.TileMode.CLAMP);
        paint.setShader(gradient);
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        return output;
    }
}