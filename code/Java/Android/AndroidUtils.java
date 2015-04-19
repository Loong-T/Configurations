package in.nerd_is.unactivated_weibo.util;

import android.os.Build;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Zheng Xuqiang on 2014/7/30 0030.
 * Android常用方法
 */
public class AndroidUtils {

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * 根据是否有外置存储，返回应用的Cache缓存文件夹
     *
     * @param context Context
     * @return 缓存文件夹
     */
    public static File getCacheDir(Context context) {
        return isExternalStorageWritable() ?
                context.getExternalCacheDir() : context.getCacheDir();
    }

    /**
     * 根据是否有外置存储，返回应用的下载文件夹
     *
     * @param context Context
     * @return 下载文件夹
     */
    public static File getDownloadDir(Context context) {
        return isExternalStorageWritable() ? context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) : context.getFilesDir();
    }

    /**
     * 清除缓存
     *
     * @param context Context
     */
    public static void clearCache(Context context) {
        File cacheDir = getCacheDir(context);
        if (cacheDir.exists()) {
            for (File file : cacheDir.listFiles()) {
                deleteFile(file);
            }
        }
    }

    private static void deleteFile(File root) {
        if (root.exists()) {
            if (root.isDirectory()) {
                for (File file : root.listFiles())
                    deleteFile(file);
            } else {
                root.delete();
            }
        }
    }

    public static String getLastEncodedPathSegment(Uri uri) {
        String encodedPath = uri.getEncodedPath();
        return encodedPath.substring(encodedPath.lastIndexOf('/'));
    }

    public static void hideSoftKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    public static float sp2Px(float sp, DisplayMetrics metrics) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    public static float dp2Px(float dp, DisplayMetrics metrics) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, metrics);
    }

    public static float px2dp(float px, DisplayMetrics metrics) {
        final float scale = metrics.density;
        return px / scale;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static float px2sp(float px, DisplayMetrics metrics) {
        final float fontScale = metrics.scaledDensity;
        return px / fontScale;
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasIceCreamSandwichMr1(){
        return Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

    public static class Net {

        /**
         * 网络是否可用
         * @param context
         * @return
         */
        public static boolean isNetworkConnected(Context context) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        /**
         * Avoiding Bugs In Earlier Releases<br>
         * Prior to Android 2.2 (Froyo, API level 8), this class had some frustrating bugs. <br>
         * In particular, calling close() on a readable InputStream could poison the connection pool.
         */
        public static void disableConnectionReuseIfNecessary() {
            // HTTP connection reuse which was buggy pre-froyo
            if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
                System.setProperty("http.keepAlive", "false");
            }
        } // end static method disableConnectionReuseIfNecessary

    } // end static class Net

    public static class FileIO {

        /**
         * 将Bitmap保存为文件<br>
         * 参见http://my.oschina.net/ryanhoo/blog/93406
         */
        public static boolean saveBitmap2File(File file, Bitmap bitmap) throws IOException {
            if(file == null || bitmap == null)
                return false;

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            boolean result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return result;
        }

        public static Bitmap loadBitmapFile(File file){
            return BitmapFactory.decodeFile(file.getPath());
        }

        /* Checks if external storage is available for read and write */
        public static boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state);
        }

        /* Checks if external storage is available to at least read */
        public static boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
        }

    }
}
