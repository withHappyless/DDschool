package cn.ddshcool.views;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.ddshcool.entity.BaseActivity;
import cn.ddshcool.entity.BmobBean.Post;
import cn.ddshcool.main.R;
import cn.ddshcool.services.adapters.ImageBrowserAdapter;
import cn.ddshcool.utils.MyToast;

/**
 * 图片浏览器窗口
 */
public class ImageBrowserActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TextView imageCount;
    private ImageView backButton;
    private ImageView saveButton;

    private Post post;
    private int position;
    private ImageBrowserAdapter adapter;
    private ArrayList<String> imgUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        initView();
        initDate();
        setDate();

    }

    /**
     * 初始化数据
     */
    private void initDate(){
        //获取帖子信息 , 以便获取原图片
        post = (Post) getIntent().getSerializableExtra("post");
        //获取当前点击的图片
        position = getIntent().getIntExtra("position", 0);
        //获取图片数据集合 , 以显示已经加载玩的缩略图 —— ImageLoader会自动加载缓存中的图片
        imgUrls = post.getThumbnail_images();

    }

    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.vp_image_browser);
        imageCount = (TextView) findViewById(R.id.tv_image_browser_image_count);
        backButton = (ImageView) findViewById(R.id.iv_image_browser_back);
        saveButton = (ImageView) findViewById(R.id.iv_image_browser_save);

        //返回按钮监听事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageBrowserActivity.this.finish();
            }
        });
        //保存按钮监听事件
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = adapter.getBitmap(mViewPager.getCurrentItem());
                //TODO 这里有问题啊 ！！ ！ 我到底需不需要建一个图片管理类呢
//                boolean showOriImag = picUrl.isShowOriImag();
                boolean showOriImag = false;

                String fileName = "img-" + (showOriImag ? "ori-" : "mid-") + post.getImages().get(1).getBytes();

                String title = fileName.substring(0, fileName.lastIndexOf("."));
                String insertImage = MediaStore.Images.Media.insertImage(
                        getContentResolver(), bitmap, title, "BoreWBImage");
                if (insertImage == null) {
                    MyToast.showToast(ImageBrowserActivity.this, "图片保存失败", Toast.LENGTH_SHORT);
                } else {
                    MyToast.showToast(ImageBrowserActivity.this, "图片保存成功", Toast.LENGTH_SHORT);
                }
            }
        });

    }

    private void setDate(){

        adapter = new ImageBrowserAdapter(this, post.getImages());
        mViewPager.setAdapter(adapter);

        final int size = imgUrls.size();
        int initPosition = Integer.MAX_VALUE / 2 / size * size + position;  //取整 , 用于无线轮播

        if(size > 1){   //如果是多图
            imageCount.setVisibility(View.VISIBLE);
            //设置底部总数
            imageCount.setText((position + 1) + "/" + size);
        }else{
            //单图不显示 图片总数 textView
            imageCount.setVisibility(View.GONE);
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int index = position % size;
                imageCount.setText((index + 1) + "/" + size);
                //用来处理是否显示保存按钮的显示
//                saveButton.setVisibility();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(initPosition);

    }

}
