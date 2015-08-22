package cn.ddshcool.views.post_views;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.CheckBox;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.ddshcool.entity.BaseActivity;
import cn.ddshcool.entity.BmobBean.User;
import cn.ddshcool.entity.BmobBean.post_list;
import cn.ddshcool.main.R;
import cn.ddshcool.services.adapters.EmotionGvAdapter;
import cn.ddshcool.services.adapters.EmotionPagerAdapter;
import cn.ddshcool.services.adapters.WriteStatusGridImgsAdapter;
import cn.ddshcool.services.network_service.PostService;
import cn.ddshcool.utils.DisplayUtil;
import cn.ddshcool.utils.EmotionUtils;
import cn.ddshcool.utils.ImageUtils;
import cn.ddshcool.utils.MyToast;
import cn.ddshcool.utils.StringUtils;
import cn.ddshcool.views.weight.WrapHeightGridView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by yosemite on 15/8/15.
 */
public class NewPostActivity extends BaseActivity implements OnItemClickListener {

    private LinearLayout mLinearLayout;
    private CheckBox isVisible;
    private TextView isBack;
    private TextView title;
    private TextView isSend;
    private ImageView isFace;
    private TextView isClear;
    private EditText editInput;
    // 添加的九宫格图片
    private WrapHeightGridView gv_write_status;
    // 表情选择面板
    private LinearLayout ll_emotion_dashboard;
    private ViewPager vp_emotion_dashboard;
    //表情面板适配器
    private EmotionPagerAdapter emotionPagerGvAdapter;
    //九宫格适配器
    private WriteStatusGridImgsAdapter statusImgsAdapter;
    //图片集合
    private ArrayList<Uri> imgUris = new ArrayList<Uri>();

    private LinearLayout.LayoutParams layoutParams;
    //帖子内容
    private String postText = "";

    private boolean isFacing = false;   //当前是否显示表情面板

    private InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);

    //points
    private ArrayList<ImageView> points = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        initView();
        setListener();
//        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();

    }

    //初始化控件
    private void initView() {

        mLinearLayout = (LinearLayout) findViewById(R.id.ll_actionbar);

        // 添加的九宫格图片
        gv_write_status = (WrapHeightGridView) findViewById(R.id.gv_write_status);

        statusImgsAdapter = new WriteStatusGridImgsAdapter(this, imgUris, gv_write_status);
        gv_write_status.setAdapter(statusImgsAdapter);
        gv_write_status.setOnItemClickListener(this);

        isVisible = (CheckBox) findViewById(R.id.cb_new_post_is_anyone);
        isBack = (TextView) findViewById(R.id.tv_new_post_back);
        title = (TextView) findViewById(R.id.tv_new_post_title);
        isSend = (TextView) findViewById(R.id.tv_new_post_send);
        isFace = (ImageView) findViewById(R.id.iv_new_post_face);
        isClear = (TextView) findViewById(R.id.tv_new_post_clear);
        editInput = (EditText) findViewById(R.id.et_new_post_input);

        // 表情选择面板
        ll_emotion_dashboard = (LinearLayout) findViewById(R.id.ll_new_post_emotion_dashboard);
        vp_emotion_dashboard = (ViewPager) findViewById(R.id.vp_new_post_emotion_dashboard);

//        //动态设置imageView大小
//        layoutParams = (LinearLayout.LayoutParams) addPicture.getLayoutParams();
//        layoutParams.width=size;
//        layoutParams.height=size;

        //不让editInput获取焦点
        isFace.requestFocus();
        //初始化表情面板
        initEmotion();

    }

    //初始化监听器
    public void setListener() {
        //清空按钮监听
        isClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(editInput.getText().toString())) {

                    if (isFacing) {

                        ll_emotion_dashboard.setVisibility(View.GONE);
                        isFace.setImageResource(R.drawable.ic_th_icon_face);

                    }

                    //dialog
                    SweetAlertDialog clearDialog = new SweetAlertDialog(NewPostActivity.this, SweetAlertDialog.BUTTON_NEGATIVE);
                    clearDialog.setCancelable(false);
                    clearDialog.setTitleText("提示");
                    clearDialog.setContentText("确认清空内容?");
                    clearDialog.setCancelText("取消");
                    clearDialog.setConfirmText("确认");
                    clearDialog.showCancelButton(true);
                    clearDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            editInput.setText("");
                            sDialog.dismissWithAnimation();
                        }
                    });
                    clearDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    });
                    clearDialog.show();
                }
            }
        });

        isBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backEvent();
            }
        });
        //如果输入文本框有焦点 则隐藏表情选择框
        editInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && isFacing) {

                    ll_emotion_dashboard.setVisibility(View.GONE);
                    isFace.setImageResource(R.drawable.ic_th_icon_face);

                }
            }
        });

        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                //改变字体颜色
//                if (length > 140) {
//                    isClear.setTextColor(getResources().getColor(R.color.error));
//                } else {
//                    isClear.setTextColor(getResources().getColor(R.color.not_sex));
//                }
                int nowLenght = 140 - length;
                //动态显示 “X” 号
                if (nowLenght >= 100) {

                    isClear.setText((140 - s.length()) + " ×");

                } else if (nowLenght <= 99 && nowLenght >= 10) {

                    isClear.setText(" " + (140 - s.length()) + "  ×");

                } else {

                    isClear.setText("  " + (140 - s.length()) + "  ×");

                }

            }
        });

        isFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFacing) {

                    ll_emotion_dashboard.setVisibility(View.GONE);
                    isFace.setImageResource(R.drawable.ic_th_icon_face);
                    //弹出软键盘
                    ((InputMethodManager) NewPostActivity.this.getSystemService(Service.INPUT_METHOD_SERVICE)).
                            hideSoftInputFromWindow(NewPostActivity.this.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.SHOW_FORCED);
                } else {

                    //干掉软键盘
                    ((InputMethodManager) NewPostActivity.this.getSystemService(Service.INPUT_METHOD_SERVICE)).
                            hideSoftInputFromWindow(NewPostActivity.this.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    ll_emotion_dashboard.setVisibility(View.VISIBLE);
                    isFace.setImageResource(R.drawable.ic_th_input_keybord);

                }
                isFacing = !isFacing;   //取相反值

            }
        });

        //ViewPager 监听器 用于改变 小圆点
        vp_emotion_dashboard.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 改变所有导航的背景图片为：未选中
                for (int i = 0; i < points.size(); i++) {

                    points.get(i).setBackgroundResource(R.drawable.shape_view_pager_points_normal);

                }

                // 改变当前背景图片为：选中
                points.get(position).setBackgroundResource(R.drawable.shape_view_pager_points_focus);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //发表按钮监听器
        isSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAccessed()){
                    if(isFacing){

                        ll_emotion_dashboard.setVisibility(View.GONE);
                        isFace.setImageResource(R.drawable.ic_th_icon_face);

                    }
                    //获取当前用户
                    User user = BmobUser.getCurrentUser(NewPostActivity.this,User.class);
                    post_list postlist = new post_list();
                    postlist.setcontent(postText);
                    //添加发帖人
                    postlist.setAuthor(user);
                    //是否匿名发表
                    postlist.setIsVisible(isVisible.isCheck());

                    PostService.whatSend(NewPostActivity.this , imgUris , postlist);

                }else{
                    //如果没内容就进行提示
                    MyToast.showToast(NewPostActivity.this, "内容不能为空", Toast.LENGTH_SHORT);
                }
            }
        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backEvent();
        }

        return true;
    }

    //判断是否有内容
    private boolean isAccessed(){
        if ("".equals(editInput.getText().toString()) == false || imgUris.size() > 0){

            postText = editInput.getText().toString();

            return true;
        }

        return false;
    }

    //返回事件
    private void backEvent() {
        //既没有文字 有没有图片 才不显示 这个dialog
        if (isAccessed()) {
            //dialog
            SweetAlertDialog backDialog = new SweetAlertDialog(NewPostActivity.this, SweetAlertDialog.WARNING_TYPE);
            backDialog.setTitleText("提示");
            backDialog.setContentText("放弃这条内容?");
            backDialog.setCancelText("放弃");
            backDialog.setConfirmText("继续编辑");
            backDialog.setCancelable(false);
            backDialog.showCancelButton(true);
            backDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                }
            });
            backDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    NewPostActivity.this.finish();
                    sDialog.cancel();
                }
            });
            backDialog.show();
        } else {
            NewPostActivity.this.finish();
        }

    }


//    /**
//     * 更新图片显示
//     */
//    private void updateImgs() {
//        if(imgUris.size() > 0 ) {
//            gv_write_status.setVisibility(View.VISIBLE);
//            statusImgsAdapter.notifyDataSetChanged();
//        } else {
//            gv_write_status.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAdapter = parent.getAdapter();
        //如果点击的是添加图片中的项目  则显示添加图片内容
        if (itemAdapter instanceof WriteStatusGridImgsAdapter) {
            if(isFacing){
                //干掉表情输入框
                ll_emotion_dashboard.setVisibility(View.GONE);
                isFace.setImageResource(R.drawable.ic_th_icon_face);
                editInput.requestFocus();   //重新获得焦点

            }

            if (position == statusImgsAdapter.getCount() - 1) {
                ImageUtils.showImagePickDialog(this);
            }


        } else if (itemAdapter instanceof EmotionGvAdapter) {
            //如果是点击的表情图片 则显示表情菜单
            EmotionGvAdapter emotionAdapter = (EmotionGvAdapter) itemAdapter;

            if (position == emotionAdapter.getCount() - 1) {
                editInput.dispatchKeyEvent(
                        new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            } else {
                String emotionName = emotionAdapter.getItem(position);

                int curPosition = editInput.getSelectionStart();
                StringBuilder sb = new StringBuilder(editInput.getText().toString());
                sb.insert(curPosition, emotionName);

                SpannableString weiboContent = StringUtils.getPostContent(
                        this, editInput, sb.toString());
                editInput.setText(weiboContent);

                editInput.setSelection(curPosition + emotionName.length());
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_FROM_ALBUM:
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                Uri imageUri = data.getData();

                imgUris.add(imageUri);
                statusImgsAdapter.notifyDataSetChanged();
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                if (resultCode == RESULT_CANCELED) {

                    ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);

                } else {

                    Uri imageUriCamera = ImageUtils.imageUriFromCamera;

                    imgUris.add(imageUriCamera);
                    statusImgsAdapter.notifyDataSetChanged();

                }
                break;

            default:
                break;
        }
    }

    /**
     * 初始化表情面板内容
     */
    private void initEmotion() {

        int screenWidth = DisplayUtil.getDisplayWidthPixels(this);
        int spacing = DisplayUtil.dip2px(this, 8);

        int itemWidth = (screenWidth - spacing * 8) / 7;
        int gvHeight = itemWidth * 3 + spacing * 4;

        int pointNum = 0;

        List<GridView> gvs = new ArrayList<GridView>();
        List<String> emotionNames = new ArrayList<String>();
        for (String emojiName : EmotionUtils.emojiMap.keySet()) {
            emotionNames.add(emojiName);

            if (emotionNames.size() == 20) {

                GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
                gvs.add(gv);

                emotionNames = new ArrayList<String>();

                pointNum++; //动态设置圆点个数

            }
        }

        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
            gv.setBackgroundColor(getResources().getColor(R.color.white));
            gvs.add(gv);
        }

        emotionPagerGvAdapter = new EmotionPagerAdapter(gvs);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
        vp_emotion_dashboard.setLayoutParams(params);
        vp_emotion_dashboard.setAdapter(emotionPagerGvAdapter);

        //初始化圆点
        initIndicator(pointNum);

    }

    /**
     * 创建显示表情的GridView  可以在创建的时候动态添加小圆点
     */
    private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
        GridView gv = new GridView(this);

        gv.setBackgroundResource(R.color.white);    //设置背景为白色

        gv.setSelector(R.color.transparent);
        gv.setNumColumns(7);
        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = gvWidth;
        params.height = gvHeight;
        gv.setLayoutParams(params);

        EmotionGvAdapter adapter = new EmotionGvAdapter(this, emotionNames, itemWidth);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);

        return gv;
    }

    /**
     * 初始化引导图标
     * 动态创建多个小圆点，然后组装到线性布局里
     */
    private void initIndicator(int pointNum) {

        ImageView imgView;
        View v = findViewById(R.id.ll_new_post_points);// 线性水平布局，负责动态调整导航图标

        for (int i = 0; i <= pointNum; i++) {

            imgView = new ImageView(this);
            LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(10, 10);
            params_linear.setMargins(8, 10, 8, 10);
            imgView.setLayoutParams(params_linear);
            points.add(imgView);

            if (i == 0) { // 初始化第一个为选中状态

                points.get(i).setBackgroundResource(R.drawable.shape_view_pager_points_focus);

            } else {

                points.get(i).setBackgroundResource(R.drawable.shape_view_pager_points_normal);

            }

            ((ViewGroup) v).addView(points.get(i));

        }
    }

}