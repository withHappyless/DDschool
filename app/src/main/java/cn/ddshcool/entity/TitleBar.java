package cn.ddshcool.entity;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ddshcool.main.R;

/**
 * 顶部操作栏
 */
public class TitleBar{

    private ImageView leftImage;
    private TextView leftText;
    private TextView title;
    private TextView rightText;
    private ImageView rightImage;
    private View view;

    public TitleBar(Activity content){
        view = content.findViewById(R.id.rl_titleBar);
        //加载视图
        leftImage = (ImageView)content.findViewById(R.id.iv_titlebar_left_image);
        leftText =(TextView)content.findViewById(R.id.tv_titlebar_left_text);
        title =(TextView)content.findViewById(R.id.tv_titlebar_title);
        rightText =(TextView)content.findViewById(R.id.tv_titlebar_right_text);
        rightImage = (ImageView)content.findViewById(R.id.iv_titlebar_right_image);
    }
    public TitleBar(View content){
        view = content.findViewById(R.id.rl_titleBar);
        //加载视图
        leftImage = (ImageView)content.findViewById(R.id.iv_titlebar_left_image);
        leftText =(TextView)content.findViewById(R.id.tv_titlebar_left_text);
        title =(TextView)content.findViewById(R.id.tv_titlebar_title);
        rightText =(TextView)content.findViewById(R.id.tv_titlebar_right_text);
        rightImage = (ImageView)content.findViewById(R.id.iv_titlebar_right_image);
    }

    // title
    //titlebar背景颜色
    public TitleBar setTitleBgRes(int resid) {
        view.setBackgroundResource(resid);
        return this;
    }

    public TitleBar setTitleText(String text) {
        title.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        title.setText(text);
        return this;
    }

    // left

    public TitleBar setLeftImage(int resId) {
        leftImage.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        leftImage.setImageResource(resId);
        return this;
    }

    public TitleBar setLeftText(String text) {
        leftText.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        leftText.setText(text);
        return this;
    }

    public TitleBar setLeftOnClickListener(View.OnClickListener listener) {
        if (leftImage.getVisibility() == View.VISIBLE) {
            leftImage.setOnClickListener(listener);
        } else if (leftText.getVisibility() == View.VISIBLE) {
            leftText.setOnClickListener(listener);
        }
        return this;
    }

    // right

    public TitleBar setRightImage(int resId) {
        rightImage.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        rightImage.setImageResource(resId);
        return this;
    }

    public TitleBar setRightText(String text) {
        rightText.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        rightText.setText(text);
        return this;
    }

    public TitleBar setRightOnClickListener(View.OnClickListener listener) {
        if (rightImage.getVisibility() == View.VISIBLE) {
            rightImage.setOnClickListener(listener);
        } else if (rightText.getVisibility() == View.VISIBLE) {
            rightText.setOnClickListener(listener);
        }
        return this;
    }

    public View build() {
        return view;
    }

}
