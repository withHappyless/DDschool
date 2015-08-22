package cn.ddshcool.services.network_service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.ddshcool.entity.BmobBean.User;
import cn.ddshcool.main.Splash;
import cn.ddshcool.services.ActivityCollection;
import cn.ddshcool.utils.BmobExceptionUtil;
import cn.ddshcool.utils.MyApplication;
import cn.ddshcool.utils.MyToast;
import cn.ddshcool.views.MainActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 登陆 注册 业务逻辑判断
 */
public class LoginService {
    //先从数据库判断是否有这个人的注册信息
    //没有 则提示注册
    //有 则从学校的教务系统进行验证
    //验证成功 读取 其用户信息并生成缓存 日期为3天
    //验证失败 返回错误信息
    //登陆方法

    /**
     * 判断登陆方式
     *  true 表示 直接跳转到主activity
     *  false 表示 跳转到登陆、注册界面
     * @param outDays
     * @return
     */
    public static void CheckUser(final int outDays){

        final Context mContext = MyApplication.getmContext();
        final BmobUser oldUser = BmobUser.getCurrentUser(mContext);
        //如果存在用户缓存
        if(oldUser != null){
            //获取当前时间
            Bmob.getServerTime(mContext, new GetServerTimeListener() {
                @Override
                public void onSuccess(long time) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String times = format.format(new Date(time*1000L));
                    Log.i("服务器时间", times);
                    //获取成功 , 判断登陆时间差值是否在允许范围内
                    try {
                        //错在了缓存的更新日期 不存在!
                        int changeDays = DayOfTwo( format.parse(times), format.parse(oldUser.getUpdatedAt()));
                        if(changeDays >= outDays || changeDays==-1){
                            //缓存过期 清空缓存数据
                            BmobUser.logOut(mContext);
                            Splash.selectWindow(false);

                        }else{
                            //缓存合法 跳转到主activity
                            Splash.selectWindow(true);

                        }
                    } catch (ParseException e) {
                        //时间转换错误  也算不成功一种 不过应该不会出问题
                        Splash.selectWindow(false);

                    } catch (NullPointerException e1){
                        //缓存出错 清空缓存数据
                        BmobUser.logOut(mContext);
                        Splash.selectWindow(false);
                    }
                }

                @Override
                public void onFailure(int code, String msg) {
                    // 获取失败 则返回设置返回值为false
                    Splash.selectWindow(false);
                }
            });

        }else{
            //不存在用户缓存数据 转入登录界面
            Splash.selectWindow(false);
        }

    }

    //登录业务逻辑
    public static void signIn(final SweetAlertDialog isLoading,final String account ,final String password){
        final Activity mActivity = ActivityCollection.getActivity(0);
        final User user = new User();
        user.setUsername(account);
        user.setPassword(password);
        user.login(mActivity, new SaveListener() {
            @Override
            public void onSuccess() {
                user.setUsername(account);
                user.update(mActivity);
                isLoading.dismiss();
                MyToast.showToast(mActivity, "登录成功", Toast.LENGTH_SHORT);
                //登陆成功,保存账号,以便下次读取
                SharedPreferences.Editor mSharedPreferences = mActivity.getSharedPreferences("user_name", Context.MODE_PRIVATE).edit();
                mSharedPreferences.putString("userName", account);
                mSharedPreferences.commit();
                //跳到主activity
                Intent mIntent = new Intent(mActivity, MainActivity.class);
                mActivity.startActivity(mIntent);
                mActivity.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                isLoading.dismiss();
                Log.e("错误代码", i + "||||||" + s);
                MyToast.showToast(mActivity, BmobExceptionUtil.StringException(i), Toast.LENGTH_SHORT);
            }
        });

    }

    //注册业务逻辑
    public static void SignUp(final SweetAlertDialog isLoading ,final String account ,final String password ,
                              Boolean userSex , String whatTime , String userSchool ,String userDept){
        final Activity mActivity = ActivityCollection.getActivity(0);
//        final BmobUserManager userManager = new BmobUserManager();
        final User newUser = new User();
        newUser.setUsername(account);
        newUser.setPassword(password);
        newUser.setUserSex(true);
        newUser.setDeviceType("android");
        newUser.setInstallId(BmobInstallation.getInstallationId(mActivity));
        //测试代码
        newUser.signUp(mActivity, new SaveListener() {
            @Override
            public void onSuccess() {
                // 将设备与username进行绑定
                BmobUserManager userManager = new BmobUserManager();
                userManager.init(mActivity);
                userManager.bindInstallationForRegister(account);

                //进行登录操作
                LoginService.signIn(isLoading,account,password);

            }

            @Override
            public void onFailure(int i, String s) {
                isLoading.dismiss();
                //注册失败
                Log.e("ERRORcode", s);
                MyToast.showToast(mActivity, BmobExceptionUtil.StringException(i), Toast.LENGTH_SHORT);
            }
        });

    }


    public static int DayOfTwo(Date dateOne , Date dateTwo){
        //dateTwo是新时间 dateOne是旧时间
        if(null == dateOne || null == dateTwo){

            return -1;

        }

        long intervalMilli = dateTwo.getTime() - dateOne.getTime();

        return (int) (intervalMilli / (24 * 60 * 60 * 1000));
    }
}
