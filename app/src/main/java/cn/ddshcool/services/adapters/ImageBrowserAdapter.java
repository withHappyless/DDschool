package cn.ddshcool.services.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import cn.ddshcool.main.R;
import cn.ddshcool.utils.DisplayUtil;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageBrowserAdapter extends PagerAdapter {

    private Activity context;
    private ArrayList<String> picUrls;
    private ArrayList<View> picViews;

    private ImageLoader mImageLoader;

    private PhotoViewAttacher mAttacher;

    public ImageBrowserAdapter(Activity context, ArrayList<String> picUrls) {
        this.context = context;
        this.picUrls = picUrls;
        this.mImageLoader = ImageLoader.getInstance();
        initImgs();
    }

    private void initImgs() {
        picViews = new ArrayList<>();

        for (int i = 0; i < picUrls.size(); i++) {
            // 填充显示图片的页面布局
            View view = View.inflate(context, R.layout.item_image_browser, null);
            picViews.add(view);
        }
    }

    @Override
    public int getCount() {
        if (picUrls.size() > 1) {
            return Integer.MAX_VALUE;
        }
        return picUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //模板item
    @Override
    public View instantiateItem(ViewGroup container, int position) {
        int index = position % picUrls.size();
        View view = picViews.get(index);
        final ImageView iv_image_browser_item = (ImageView) view.findViewById(R.id.iv_image_browser_item);
        /**
         * photoView 相关
         */
        mAttacher=new PhotoViewAttacher(iv_image_browser_item);
        mAttacher.update();

        String picUrl = picUrls.get(index);

        mImageLoader.loadImage(picUrl, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {


            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {


            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                float scale = (float) loadedImage.getHeight() / loadedImage.getWidth();

                int screenWidthPixels = DisplayUtil.getDisplayWidthPixels(context);
                int screenHeightPixels = DisplayUtil.getDisplayheightPixels(context);

                int height = (int) (screenWidthPixels * scale);

                if (height < screenHeightPixels) {
                    height = screenHeightPixels;
                }

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv_image_browser_item.getLayoutParams();
                params.height = height;
                params.width = screenWidthPixels;
                iv_image_browser_item.setImageBitmap(loadedImage);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {


            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public String getPic(int position) {
        return picUrls.get(position % picUrls.size());
    }

    public Bitmap getBitmap(int position) {
        Bitmap bitmap = null;
        View view = picViews.get(position % picViews.size());
        PhotoView iv_image_browser_item = (PhotoView) view.findViewById(R.id.iv_image_browser_item);
        Drawable drawable = iv_image_browser_item.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            bitmap = bd.getBitmap();
        }

        return bitmap;
    }
}
