package cn.ddshcool.utils;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.ddshcool.services.adapters.ImageOptHelper;

/**
 * 全局获取context工具类
 */
public class MyApplication extends Application{
    private static Context mContext;
    public static int post_thumbnail_modelID = 1;
    public static String AccessKey = "fef8c139e28d71bb4b575acea9efe6f0";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        initImageLoader(this);
    }

    public static Context getmContext(){
        return mContext;
    }
    // 初始化图片处理
    private void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(ImageOptHelper.getImgOptions())
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

}
