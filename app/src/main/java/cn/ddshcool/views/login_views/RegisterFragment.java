package cn.ddshcool.views.login_views;


import android.app.Service;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ddshcool.entity.DeptAndYear;
import cn.ddshcool.main.R;
import cn.ddshcool.services.network_service.LoginService;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 用于处理注册界面逻辑
 * 在密码框点击回车 应该隐藏小键盘 (定位焦点的方法不行)
 */
public class RegisterFragment extends Fragment{

    private ImageView mOptionAccount;
    private ImageView mOptionPassword;
    private ImageView mOptionName;


    private TextView mErrorText;
    private TextView mErrorPwd;
    private TextView mYearText;
    private TextView mDeptText;
    private TextView mMan;
    private TextView mWomen;
    private TextView mReallyMan;
    private TextView mReallyWomen;

    private EditText mAccountText;
    private EditText mPasswordText;
    private EditText mNameText;

    private Button mRegisterBtn;

    private String account="";
    private String password="";
    private String name="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载布局
        View view = inflater.inflate(R.layout.fragment_new_register, null);
        //加载控件
        mOptionAccount= (ImageView)view.findViewById(R.id.register_option_ant);   //账号显示状态按钮
        mOptionPassword = (ImageView)view.findViewById(R.id.register_option_pwd);   //密码显示状态按钮
        mOptionName = (ImageView)view.findViewById(R.id.register_option_name);   //昵称显示状态按钮

        mErrorText= (TextView)view.findViewById(R.id.register_error_text);   //账号错误提示
        mErrorPwd = (TextView)view.findViewById(R.id.register_error_pwd);   //密码错误提示
        mYearText = (TextView)view.findViewById(R.id.tv_really_year);   //入学年份输入框
        mDeptText = (TextView)view.findViewById(R.id.tv_really_dept);   //院系输入框
        //初始化deptAndYear类中的textView
        new DeptAndYear().initTexts(mDeptText,mYearText);

        mMan = (TextView)view.findViewById(R.id.tv_sex_man);   //男生头像
        mWomen = (TextView)view.findViewById(R.id.tv_sex_women);   //女生头像
        mReallyMan = (TextView)view.findViewById(R.id.tv_of_man);   //男生文字
        mReallyWomen = (TextView)view.findViewById(R.id.tv_of_woman);   //女生文字

        mAccountText = (EditText)view.findViewById(R.id.register_et_account);   //账号输入框
        mPasswordText = (EditText)view.findViewById(R.id.register_et_pwd);   //密码输入框
        mNameText = (EditText)view.findViewById(R.id.et_name);   //昵称输入框

        mRegisterBtn = (Button)view.findViewById(R.id.btn_register);   //注册按钮

        //控件初始化
        mOptionAccount.setVisibility(View.GONE);
        mOptionPassword.setVisibility(View.GONE);
        mOptionName.setVisibility(View.GONE);
        mErrorText.setVisibility(View.GONE);
        mErrorPwd.setVisibility(View.GONE);
        mRegisterBtn.setEnabled(false);


        /**
         * 缺少点击按钮时检测网络状态
         */
        //注册监听器

        //清除账号
        mOptionAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccountText.setText("");
            }
        });

        //清除密码
        mOptionPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPasswordText.setText("");
            }
        });

        //清除昵称
        mOptionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNameText.setText("");
            }
        });

        //弹出入学年份选择框
        mYearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置为不显示搜索框
                DeptAndYear.isSearch=false;
                //设置选中项
                DeptAndYear.selectYear=mYearText.getText().toString();
                //弹出学年选择fragment
                android.support.v4.app.FragmentManager fragmentManager = RegisterFragment.this.getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_main, new ItemFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                //判断注册按钮是否可用
                BtnRegisterIsEnable(account, password, name);
            }
        });

        //弹出院系选择框
        mDeptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置为显示搜索框
                DeptAndYear.isSearch = true;
                //设置选中项
                DeptAndYear.selectDept = mDeptText.getText().toString();
                //弹出院系选择fragment    (其实是一个fragment)
                android.support.v4.app.FragmentManager fragmentManager = RegisterFragment.this.getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_main, new ItemFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                //判断注册按钮是否可用
                BtnRegisterIsEnable(account, password, name);
            }
        });

        //男女切换效果
        //点击男生头像
        mMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSex(false);
            }
        });

        //点击男生文字
        mReallyMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSex(false);
            }
        });

        //点击女生头像
        mWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSex(true);
            }
        });

        //点击女生文字
        mWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSex(true);
            }
        });

        //账号状态判断
        mAccountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                account = mAccountText.getText().toString();
                BtnRegisterIsEnable(account, password, name);
            }
        });

        //密码状态判断
        mPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password=mPasswordText.getText().toString();
                BtnRegisterIsEnable(account,password,name);
            }
        });

        //姓名状态判断,解决了跳转问题
        mNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                name=mNameText.getText().toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().equals("\n")){
                    mNameText.setText("");
                    mMan.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus && mNameText.getText().equals("")){

                    mOptionName.setVisibility(View.GONE);

                }

            }
        });

        mNameText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    ((InputMethodManager)getActivity().getSystemService(Service.INPUT_METHOD_SERVICE)).
                            hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    if (name.length() != 0){
                        mMan.requestFocus();
                        mNameText.setText(name);
                        BtnRegisterIsEnable(account, password, name);
                    }

                }
                return false;
            }
        });

        mAccountText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //切换控件显示状态
                    mErrorText.setVisibility(View.GONE);
                    mOptionAccount.setEnabled(true);
                    mOptionAccount.setImageResource(R.drawable.clean_of_text);
                    mOptionAccount.setEnabled(true);
                    mOptionAccount.setVisibility(View.VISIBLE);
                } else {
                    name=mAccountText.getText().toString();
                    //切换控件显示状态
                    mOptionPassword.setEnabled(false);
                    if (!name.isEmpty()){
                        if(name.length()==12){
                            mErrorText.setVisibility(View.GONE);
                            mOptionAccount.setImageResource(R.drawable.input_success);
                        }else{
                            mErrorText.setVisibility(View.VISIBLE);
                            mOptionAccount.setImageResource(R.drawable.input_error);
                        }
                    }else{
                        mErrorText.setVisibility(View.GONE);
                        mOptionAccount.setVisibility(View.GONE);
                    }
                }
            }
        });

        mPasswordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //切换控件显示状态
                    mErrorPwd.setVisibility(View.GONE);
                    mOptionPassword.setImageResource(R.drawable.clean_of_text);
                    mOptionPassword.setEnabled(true);
                    mOptionPassword.setVisibility(View.VISIBLE);
                }else{
                    password=mPasswordText.getText().toString();
                    //切换控件显示状态
                    mOptionPassword.setEnabled(false);
                    if(!password.isEmpty()){
                        if(password.length()>=6){
                            mErrorPwd.setVisibility(View.GONE);
                            mOptionPassword.setImageResource(R.drawable.input_success);
                        }else{
                            mErrorPwd.setVisibility(View.VISIBLE);
                            mOptionPassword.setImageResource(R.drawable.input_error);
                        }
                    }else{
                        mErrorPwd.setVisibility(View.GONE);
                        mOptionPassword.setVisibility(View.GONE);
                    }

                }
            }
        });

        mNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //切换控件显示状态
                    mOptionName.setImageResource(R.drawable.clean_of_text);
                    mOptionName.setEnabled(true);
                    mOptionName.setVisibility(View.VISIBLE);
                }else{
                    name=mNameText.getText().toString();
                    mOptionName.setEnabled(false);
                    //切换控件显示状态
                    if(!name.isEmpty()){
                        mOptionName.setImageResource(R.drawable.input_success);
                    }
                }
            }
        });

        //注册按钮->调用service处理注册业务逻辑
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //显示加载中...progressDialog
                SweetAlertDialog isLoading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                isLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                isLoading.setTitleText("注册ing...");
                isLoading.setCancelable(false);
                isLoading.show();

                LoginService.SignUp(isLoading,account,password,true,null,null,null);

            }
        });

        return view;
    }

    //变更性别
    private void changeSex(boolean isWomen){
        if(isWomen){
            //切换图片
            mMan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.register_not_man,0 , 0, 0);
            mWomen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.register_women, 0, 0, 0);
            //切换文字颜色
            mReallyMan.setTextColor(getResources().getColor(R.color.not_sex));
            mReallyWomen.setTextColor(getResources().getColor(R.color.error));
        }else{
            //切换文字颜色
            mReallyMan.setTextColor(getResources().getColor(R.color.blue));
            mReallyWomen.setTextColor(getResources().getColor(R.color.not_sex));
            //切换图片
            mMan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.register_man, 0, 0, 0);
            mWomen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.register_not_women, 0, 0, 0);
        }
    }

    //判断注册按钮是否可用
    private void BtnRegisterIsEnable(String account,String password,String name){
        if(mNameText.getText().length()>=1 && account.length()==12 && password.length()>=6){
            mRegisterBtn.setEnabled(true);
        }else{
            mRegisterBtn.setEnabled(false);
        }
    }

}
