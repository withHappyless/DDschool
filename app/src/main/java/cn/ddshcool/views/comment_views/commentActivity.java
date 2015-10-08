package cn.ddshcool.views.comment_views;

import android.graphics.drawable.AnimationDrawable;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.CheckBox;

import java.util.ArrayList;
import java.util.List;

import cn.ddshcool.entity.BaseBackActivity;
import cn.ddshcool.entity.BmobBean.Post;
import cn.ddshcool.entity.TitleBar;
import cn.ddshcool.main.R;
import cn.ddshcool.services.adapters.CommentAdapter;
import cn.ddshcool.services.adapters.EmotionGvAdapter;
import cn.ddshcool.services.adapters.EmotionPagerAdapter;
import cn.ddshcool.services.adapters.IsCommentPost;
import cn.ddshcool.utils.DisplayUtil;
import cn.ddshcool.utils.EmotionUtils;
import cn.ddshcool.utils.MyToast;
import cn.ddshcool.utils.StringUtils;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * 回复界面
 * <p/>
 * 如果是点击评论按钮进入评论界面的,让输入框获取焦点
 * 如果是点击帖子内容进入评论界面的,则让listView本身获取焦点
 * 如果是点击的评论按钮,则让输入框获取焦点
 * <p/>
 * 当输入框内没有文本的时候,发送按钮显示灰色
 * 当输入框内文本字数超出时,发送按钮显示灰色
 * 当输入框内文字字数在规定范围内,发送按钮显示为蓝色
 * <p/>
 * 点击发送按钮时,小键盘、表情面板、匿名选择按钮、评论字数提示全部隐藏
 * <p/>
 * 点击表情按钮时,如果表情面板没有显示
 * 输入框失去焦点,小键盘隐藏,匿名选择按钮、评论字数提示出现,并切换为小键盘显示
 * 如果表情面板显示
 * 输入框获取焦点,匿名选择按钮、评论字数提示出现,并切换为表情显示
 * <p/>
 * (也就是输入框失去焦点时,表情面板也没有出现,或者表情面板出现输入框也没有获得焦点)
 * <p/>
 * 当除底部操作栏的其他组件(发送按钮逻辑单独处理)获取到焦点时,匿名选择按钮、评论字数提示、
 * 表情面板以及小键盘都隐藏,且表情按钮变为笑脸
 * <p/>
 * Created by yosemite on 15/9/9.
 * TODO 没有实现侧滑返回 没有实现多界面适配 以及隐藏显示的问题 没有实现内容适配器
 */
public class commentActivity extends BaseBackActivity implements AdapterView.OnItemClickListener{


    private TitleBar mTitleBar;  //顶部操作栏

    private PullToRefreshLayout mPullToRefreshLayout;   //下拉刷新菜单
    //回复主体显示部分
    private View mHeadView_PostDetail;
    private View mHeadView_CommentCount;
    private TextView mCommentCount;
    private ListView mCommentListView;
    private CommentAdapter mCommentAdapter;
    //没有回复
    private View mFootView_noComments;
    //底部加载进度条
    private View mFootView;
    private ImageView mLodingImageView;
    private AnimationDrawable mAnimationDrawable;
    //底部评论操作栏
    private LinearLayout mCommentLayout;
    private ImageView mEmojiButton;
    private EditText mCommentInput;
    private TextView mSendComment;
    private CheckBox mIsAnyone;
    private TextView mTextCount;
    private RelativeLayout mAnyoneAndTextCount;
    // 表情选择面板
    private LinearLayout mEmojiLayout;
    private ViewPager mEmojiViewPager;
    //表情面板适配器
    private EmotionPagerAdapter mEmotionPagerGvAdapter;
    //表情面板下方的小圆点
    private ArrayList<ImageView> points = new ArrayList<>();


    private boolean isFacing = false;   //是否正在显示表情面板
    private boolean sendIsOk;

    private InputMethodManager imm;     //软键盘管理器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载布局
        setContentView(R.layout.activity_comment);
        initView();

        //// TODO: 15/9/18  没有实现 网络判断(是否直接显示error) 以及没有评论业务逻辑 
        //// TODO: 15/9/18  以及分页查询回复 下拉刷新 上拉加载
        mCommentListView.addHeaderView(new IsCommentPost(this, (Post) getIntent()
                .getSerializableExtra("postDetail")).getPostView(new View(this)));

        mCommentListView.addHeaderView(mHeadView_CommentCount);
        /**
         * 业务逻辑分析
         *   需要再次请求服务器判断该帖子是否可见 如果不可见 则不加载评论 并显示相应提示
         *   如果可见则
         *      展示帖子信息
         *      分页加载回复
         *
         */
        mCommentListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }
        });


        //设置内容适配器
//        mCommentListView.setAdapter(new CommentAdapter(this,));

    }


    //控件初始化函数
    private void initView() {
        //初始化软键盘服务
        imm = (InputMethodManager) getSystemService(commentActivity.this.INPUT_METHOD_SERVICE);

        mTitleBar = new TitleBar(this);
        mTitleBar.setLeftImage(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mTitleBar.setTitleText("叨叨吐槽");
        //退出当前activity
        mTitleBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //评论列表
        mCommentListView = (ListView) findViewById(R.id.lv_comment_list);
        mCommentListView.setVisibility(View.VISIBLE);
        //评论个数headView
        mHeadView_CommentCount = View.inflate(this, R.layout.headview_comment_count, null);

        //评论个数
        mCommentCount = (TextView) findViewById(R.id.tv_comment_commentCount);

        //表情面板
        mEmojiLayout = (LinearLayout) findViewById(R.id.ll_comment_emotion_dashboard);
        mEmojiViewPager = (ViewPager) findViewById(R.id.vp_comment_emotion_dashboard);
        //初始化表情面板
        initEmotion();

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_comment_refresh);
        //初始化下拉控件
        ActionBarPullToRefresh.from(this)
                .options(Options.create()
                                .scrollDistance(.65f)
                                .build()
                )
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {

                    }

                })
                .theseChildrenArePullable(R.id.lv_comment_list)
                .setup(mPullToRefreshLayout);

        //底部输入操作栏
        mCommentLayout = (LinearLayout) findViewById(R.id.input);
        mAnyoneAndTextCount = (RelativeLayout) findViewById(R.id.rl_anyone_and_text_count);
        mEmojiButton = (ImageView) findViewById(R.id.iv_comment_emoji_button);
        mCommentInput = (EditText) findViewById(R.id.et_comment_input_comment);
        mIsAnyone = (CheckBox) findViewById(R.id.cb_comment_is_anyone);
        mTextCount = (TextView) findViewById(R.id.tv_comment_text_count);
        mSendComment = (TextView) findViewById(R.id.tv_comment_send_button);
        /**
         * 当评论输入框输入字符时
         *  字数统计textView 应动态显示字符个数
         */
        mCommentInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int mCount = 140 - s.length();
                //设置文本颜色

                if (mCount >= 0) {

                    mTextCount.setTextColor(getResources().getColor(R.color.comment_text_size_normal));
                    mTextCount.setText(mCount + "字");

                } else {

                    mTextCount.setTextColor(getResources().getColor(R.color.comment_text_size_warring));
                    mTextCount.setText("已超出" + (mCount * -1) + "字");

                }
                //根据输入字数决定状态 (还可以继续优化)
                if (mCount == 140 || mCount < 0) {

                    mSendComment.setTextColor(getResources().getColor(R.color.comment_send_enable));
                    mSendComment.setEnabled(false);
                    sendIsOk = false;

                } else if (!sendIsOk) {     //牺牲空间与判断 节省UI更新成本
                    mSendComment.setTextColor(getResources().getColor(R.color.comment_send_normal));
                    mSendComment.setEnabled(true);
                    sendIsOk = true;
                }

            }
        });

        /**
         * 当按下发送按钮时
         *  如果评论内容为空 则toast提示 否则判断是否匿名后 发送评论
         */
        mSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCommentInput.getText().toString().isEmpty()) {
                    MyToast.showToast(commentActivity.this, "发表成功", Toast.LENGTH_SHORT);
                } else {
                    MyToast.showToast(commentActivity.this, "还没有输入评论哦", Toast.LENGTH_SHORT);
                }
            }
        });

        mEmojiViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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


        //没有评论
        mFootView_noComments = View.inflate(this, R.layout.footview_no_comments, null);
        //加载更多布局
        mFootView = View.inflate(this, R.layout.footview_loading_more, null);
        mLodingImageView = (ImageView) mFootView.findViewById(R.id.iv_footview_loading_more);
        mAnimationDrawable = (AnimationDrawable) mLodingImageView.getDrawable();
        mAnimationDrawable.setOneShot(false);

        mEmojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当表情按钮被按下时
                if (!isFacing) {

                    showEmoji();

                } else {

                    hideEmoji();

                }
            }

        });

        mCommentInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (isFacing) {
                        hideEmoji();
                    }
                }
            }
        });


        // TODO 还有回复列表 读取中 进度条 没有 链接 以及 没有回复的业务逻辑处理 还有 IsAnyone 显示与否的问题!!!


        //是否让评论输入框优先获得焦点
        /**
         * TODO 是否显示匿名按钮的业务逻辑需要修改
         */
        boolean isFocus = getIntent().getBooleanExtra("focusOnInput", false);
//        boolean isFocus = false;
        if (isFocus) {

            showTip(false);
            mCommentInput.requestFocus();
            //显示软键盘
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);

        }


    }


    //显示加载进度条
    private void addFootView(ListView plv, View footView) {
        //如果listView没有footView则进行添加
        if (plv.getFooterViewsCount() == 1) {

            plv.addFooterView(footView);
            //启动加载动画
            mAnimationDrawable.start();

        }
    }

    //隐藏加载进度条
    private void removeFootView(ListView plv, View footView) {
        //如果listView有footView则进行移除
        if (plv.getFooterViewsCount() > 1) {

            plv.removeFooterView(footView);
            //停止加载动画
            mAnimationDrawable.stop();

        }
    }

    /**
     * 表情面板显示与隐藏
     */
    private void showEmoji() {

        showTip(true);

        //如果当前没有显示表情面板 则显示表情面板 并将表情按钮转换为输入按钮
//        imm.hideSoftInputFromWindow(mCommentInput.getWindowToken(), 0);
//        imm.hideSoftInputFromWindow(mCommentInput.getWindowToken(), 0);
        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,InputMethodManager.SHOW_IMPLICIT);
        mEmojiLayout.setVisibility(View.VISIBLE);   //显示表情面板
        mEmojiButton.setImageResource(R.drawable.selector_comment_keyboard);   //转换为输入按钮

        isFacing = true;

    }

    private void hideEmoji() {

        showTip(true);

        //如果当前显示的是表情面板 则隐藏表情面板 并将焦点转到评论输入框
        mEmojiLayout.setVisibility(View.GONE);   //隐藏表情面板
        mEmojiButton.setImageResource(R.drawable.selector_comment_emoji);  //转换为表情按钮

        mCommentInput.setSelection(mCommentInput.getText().length());
        mCommentInput.requestFocus();
        mCommentInput.setFocusable(true);
        //显示软键盘
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        isFacing = false;

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

        List<GridView> gvs = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>();
        for (String emojiName : EmotionUtils.emojiMap.keySet()) {
            emotionNames.add(emojiName);

            if (emotionNames.size() == 20) {

                GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
                gvs.add(gv);

                emotionNames = new ArrayList<>();

                pointNum++; //动态设置圆点个数

            }
        }

        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
            gv.setBackgroundColor(getResources().getColor(R.color.white));
            gvs.add(gv);
        }

        mEmotionPagerGvAdapter = new EmotionPagerAdapter(gvs);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
        mEmojiViewPager.setLayoutParams(params);
        mEmojiViewPager.setAdapter(mEmotionPagerGvAdapter);

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
        View v = findViewById(R.id.ll_comment_emoji_points);// 线性水平布局，负责动态调整导航图标

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAdapter = parent.getAdapter();

        if (itemAdapter instanceof EmotionGvAdapter) {
            //如果是点击的表情图片 则显示表情菜单
            EmotionGvAdapter emotionAdapter = (EmotionGvAdapter) itemAdapter;

            if (position == emotionAdapter.getCount() - 1) {
                mCommentInput.dispatchKeyEvent(
                        new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            } else {
                String emotionName = emotionAdapter.getItem(position);

                int curPosition = mCommentInput.getSelectionStart();
                StringBuilder sb = new StringBuilder(mCommentInput.getText().toString());
                sb.insert(curPosition, emotionName);

                SpannableString weiboContent = StringUtils.getPostContent(
                        this, mCommentInput, sb.toString());
                mCommentInput.setText(weiboContent);

                mCommentInput.setSelection(curPosition + emotionName.length());
            }
        }

    }

    private void showTip(boolean isShow) {

        if (isShow) {
            if (mAnyoneAndTextCount.getVisibility() == View.GONE) {

                mAnyoneAndTextCount.setVisibility(View.VISIBLE);
            }
        } else {
            if (mAnyoneAndTextCount.getVisibility() == View.VISIBLE) {

                mAnyoneAndTextCount.setVisibility(View.GONE);
            }
        }

    }


}
