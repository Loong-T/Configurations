package in.nerd_is.unactivated_weibo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

/**
 * Created by Zheng Xuqiang on 2014/8/5 0005.<br>
 * 负责图片的加载，从内存缓存、文件缓存或者是网络中加载图片
 */
public class ImageLoader {

    private static String TAG = "ImageLoader";
    private static ImageLoader instance;

    private Context mContext;
    private File mCacheDir;
    private LruCache<String, Bitmap> mMemCache;
    private ImageCacheTask mImageCacheTask;

    private ImageLoader(Context context) {
        mContext = context;

        int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
        mMemCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

        mCacheDir = AndroidUtils.FileIO.isExternalStorageWritable() ?
                mContext.getExternalCacheDir() : mContext.getCacheDir();

        mImageCacheTask = new ImageCacheTask(mContext);
    }

    public static ImageLoader getInstance(Context context) {
        if (instance == null) {
            instance = new ImageLoader(context);
        }
        return instance;
    }

    public void putMemCache(String key, Bitmap value) {
        if (key != null && value != null)
            mMemCache.put(key, value);
    }

    public void putDiskCache(String fileName, Bitmap bitmap) throws IOException {
        if (fileName != null && bitmap != null) {
            File file = new File(mCacheDir, fileName);
            AndroidUtils.FileIO.saveBitmap2File(file, bitmap);
        }
    }

    public void clearMemCache() {
        mMemCache.evictAll();
    }

    public void clearDiskCache() {
        if (mCacheDir.exists()) {
            for (File file : mCacheDir.listFiles()) {
                file.delete();
            }
        }
    }

    public void clearCache() {
        clearMemCache();
        clearDiskCache();
    }

    public void loadImage(String url, ImageView imgView) {
        String fileName = url.substring(url.lastIndexOf('/'));
        Bitmap bitmap = null;

        // 从内存缓存中获取
        if ((bitmap = mMemCache.get(fileName)) != null) {
            Log.d(TAG, "load img from memory cache : " + fileName);
            imgView.setImageBitmap(bitmap);
            return;
        }
        // 从文件缓存中获取
        else {
            File tmpFile = new File(mCacheDir, fileName);
            if (tmpFile.exists()) {
                Log.d(TAG, "load img from disk cache : " + tmpFile.getPath());
                bitmap = AndroidUtils.FileIO.loadBitmapFile(tmpFile);
                imgView.setImageBitmap(bitmap);
                return;
            }
        }

        // 网络中获取，并且存放到缓存中
        Log.d(TAG, "load img from network : " + url);
        ImageCacheTask imageCacheTask = new ImageCacheTask(mContext);
        imageCacheTask.setImageView(imgView);
        imageCacheTask.execute(url);
    }
}
