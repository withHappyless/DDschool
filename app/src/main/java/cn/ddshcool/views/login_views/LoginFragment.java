package cn.ddshcool.views.login_views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ddshcool.main.R;
import cn.ddshcool.services.network_service.LoginService;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 用于处理登陆界面逻辑
 */
public class LoginFragment extends Fragment {

    private ImageView mOpptionAccount;
    private ImageView mCleanPassword;
    private ImageView mOpptionPassword;

    private TextView mErrorText;
    private TextView mErrorPwd;

    private EditText mAccountText;
    private EditText mPasswordText;

    private static boolean seeOfPwd=false;

    private Button mLoginBtn;

    private String account="";
    private String password="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstnceState) {
        View view = inflater.inflate(R.layout.fragment_new_login, null);
        //找到控件
        mOpptionAccount=(ImageView)view.findViewById(R.id.register_option_ant);  //学号框状态显示
        mCleanPassword=(ImageView)view.findViewById(R.id.register_option_pwd);    //清除密码按钮
        mOpptionPassword=(ImageView)view.findViewById(R.id.option_pwd); //切换密码显示状态按钮
        mAccountText = (EditText) view.findViewById(R.id.et_account);      //学号输入框
        mPasswordText = (EditText) view.findViewById(R.id.register_et_pwd);      //密码输入框
        mErrorText = (TextView) view.findViewById(R.id.register_error_text);     //错误提示标签
        mLoginBtn = (Button) view.findViewById(R.id.btn_login);          //登陆按钮
        mErrorPwd = (TextView) view.findViewById(R.id.error_pwd);     //密码错误提示标签
        //初始化显示
        mOpptionAccount.setVisibility(View.GONE);
        mCleanPassword.setVisibility(View.GONE);
        mErrorText.setVisibility(View.GONE);
        mErrorPwd.setVisibility(View.GONE);

        //初始化学号输入框
        mAccountText.setText(getActivity().getSharedPreferences("user_name", Context.MODE_PRIVATE).getString("userName",""));

        //第一步读取sharedPreferences的值
//        SharedPreferences msharedPreferences= this.getString()

        //添加监听器

        //学号框焦点监听器,根据是否得到焦点切换状态显示图片
        mAccountText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //设置图片能点击
                    mOpptionAccount.setEnabled(true);
                    //显示清除按钮
                    mOpptionAccount.setImageResource(R.drawable.clean_of_text);
                    mOpptionAccount.setVisibility(View.VISIBLE);
                    //隐藏错误提示
                    mErrorText.setVisibility(View.GONE);
                } else {
                    //设置图片不能点击
                    mOpptionAccount.setEnabled(false);
                    //获取账号
                    account = mAccountText.getText().toString();
                    //如果非空则深入判断
                    if (!account.isEmpty()) {
                        if (account.length() < 12) {
                            //显示错误图片
                            mOpptionAccount.setImageResource(R.drawable.input_error);
                            //显示错误消息
                            mErrorText.setVisibility(View.VISIBLE);
                        } else {
                            //显示正确图片
                            mOpptionAccount.setImageResource(R.drawable.input_success);
                            //隐藏错误消息
                            mErrorText.setVisibility(View.GONE);
                            //判断账号和密码是否输入完毕 输入完毕则登陆按钮可以按动
                            if (password.length() >= 6) {
                                mLoginBtn.setEnabled(true);
                            } else {
                                mLoginBtn.setEnabled(false);
                            }
                        }
                    } else {
                        //没输入学号,不显示错误提示
                        mErrorText.setVisibility(View.GONE);
                        mLoginBtn.setEnabled(false);
                        mOpptionAccount.setVisibility(View.GONE);
                    }

                }
            }
        });

        //清除账号监听器
        mOpptionAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空学号输入框文本
                mAccountText.setText("");
            }
        });


        //密码输入框监听器
        mPasswordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //密码错误提示标签不可见
                    mErrorPwd.setVisibility(View.GONE);
                    //获得焦点 显示清除密码框
                    mCleanPassword.setVisibility(View.VISIBLE);
                } else {

                    //失去焦点 隐藏清除密码框
                    mCleanPassword.setVisibility(View.GONE);
                    //看看密码是否大于等于6位
                    password = mPasswordText.getText().toString();
                    if (!password.isEmpty()) {
                        if (password.length() >= 6) {
                            //隐藏密码错误提示标签
                            mErrorPwd.setVisibility(View.GONE);
                            //判断账号是否正确,正确则登陆按钮可用
                            if (account.length() == 12) {
                                mLoginBtn.setEnabled(true);
                            } else {
                                mLoginBtn.setEnabled(false);
                            }
                        } else {
                            //显示密码错误提示标签
                            mErrorPwd.setVisibility(View.VISIBLE);
                            mLoginBtn.setEnabled(false);
                        }
                    } else {
                        //没输入密码,隐藏密码错误提示标签
                        mErrorPwd.setVisibility(View.GONE);
                        //控制登陆按钮颜色
                        mLoginBtn.setEnabled(false);
                    }

                }
            }
        });

        //切换密码框显示状态按钮监听器
        mOpptionPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否显示密码
                if (!seeOfPwd) {
                    //密码明文显示
                    mPasswordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //切换密码显示状态按钮图片
                    mOpptionPassword.setImageResource(R.drawable.not_see_pwd);
                    //切换状态
                    seeOfPwd = true;
                } else {
                    //密码暗文显示
                    mPasswordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //切换密码显示状态按钮图片
                    mOpptionPassword.setImageResource(R.drawable.see_pwd);
                    //切换状态
                    seeOfPwd = false;
                }
            }
        });

        mAccountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //输入账号后判断
                //获取账号
                account = mAccountText.getText().toString();
                //如果非空则深入判断
                if (!account.isEmpty()) {
                    if (account.length() < 12) {
                        mLoginBtn.setEnabled(false);
                    } else {
                        if (password.length() >= 6) {
                            mLoginBtn.setEnabled(true);
                        } else {
                            mLoginBtn.setEnabled(false);
                        }
                    }
                } else {

                    mLoginBtn.setEnabled(false);
                }
            }
        });

        mPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                //输入文字之后判断是否满足要求
                //看看密码是否大于等于6位
                password = mPasswordText.getText().toString();
                if (!password.isEmpty()) {
                    if (password.length() >= 6) {
                        //判断账号是否正确,正确则登陆按钮可用
                        if (account.length() == 12) {
                            mLoginBtn.setEnabled(true);
                        } else {
                            mLoginBtn.setEnabled(false);
                        }
                    } else {
                        mLoginBtn.setEnabled(false);
                    }
                } else {
                    //控制登陆按钮颜色
                    mLoginBtn.setEnabled(false);
                }
            }
        });

        //清除密码监听器
        mCleanPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空密码输入框文本
                mPasswordText.setText("");
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //显示加载中...progressDialog
                SweetAlertDialog isLoading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                isLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                isLoading.setTitleText("登陆ing...");
                isLoading.setCancelable(false);
                isLoading.show();
                LoginService.signIn(isLoading, account, password);

            }
        });

        return view;
        }

    }
