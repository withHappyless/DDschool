package cn.ddshcool.main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import cn.bmob.im.BmobChat;
import cn.bmob.v3.Bmob;
import cn.ddshcool.entity.BaseActivity;
import cn.ddshcool.entity.DeptAndYear;
import cn.ddshcool.services.ActivityCollection;
import cn.ddshcool.services.network_service.LoginService;
import cn.ddshcool.views.MainActivity;
import cn.ddshcool.views.login_views.LoginWindow;

public class Splash extends BaseActivity {
    //启动界面显示时间
    private static long showTime=2200;
    //缓存失效天数
    private int outDays = 3;
    //我的ApplicationID
    private static final String myApplicationId="c9825471df8a957cec4efb19eb09f5d0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //干掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //应用全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //初始化Bmob SDK
        Bmob.initialize(this, myApplicationId);
        //可设置调试模式，当为true的时候，会在logcat的BmobChat下输出一些日志，包括推送服务是否正常运行，如果服务端返回错误，也会一并打印出来。方便开发者调试，正式发布应注释此句。
        BmobChat.DEBUG_MODE = true;
        //BmobIM SDK初始化--只需要这一段代码即可完成初始化
        BmobChat.getInstance(this).init(myApplicationId);
        //初始化院系 以及 学年
        DeptAndYear.initDeptAndYear();
        //调用初始化方法
        LoginService.CheckUser(outDays);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        switch(keyCode){
            //判断用户是否按下返回键
            case android.view.KeyEvent.KEYCODE_BACK:
                //结束程序
                android.os.Process.killProcess(android.os.Process.myPid());
            break;
            default:
                break;
        }
        return true;
    }

    //选择窗口
    //有缓存合法直接进入
    //缓存不合法或没有缓存 则进入登录界面
    public static void selectWindow(final boolean isLogin){

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Splash splash = (Splash) ActivityCollection.getActivity(0);
                Intent mIntent;
                //判断是否可以直接登录
                if(!isLogin){
                    //可以直接登陆
                    mIntent= new Intent(splash,LoginWindow.class);

                }else{

                    //要注册
                    mIntent= new Intent(splash, MainActivity.class);

                }

                //选择窗口
                splash.startActivity(mIntent);
                //这里不检查更新,在登录窗口中用线程检查有无更新
                splash.finish();

            }
        }, showTime);
    }

}
