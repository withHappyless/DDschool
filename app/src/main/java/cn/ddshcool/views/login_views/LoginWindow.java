package cn.ddshcool.views.login_views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import cn.ddshcool.main.R;
import cn.ddshcool.services.ActivityCollection;

/**
 * 登陆背景activity
 */
public class LoginWindow extends FragmentActivity{

    Login mLoginFragment;

    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCollection.addActivity(this);
        mLoginFragment=new Login();
        //加载布局
        mViewPager = (ViewPager) findViewById(R.id.view_pager_login);
        mViewPager.setAdapter(new ContainerAdapter(getSupportFragmentManager()));
        Intent mIntent = new Intent();
        mIntent.putExtra("string",true);

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        ActivityCollection.removeActivity(this);

    }

    public class ContainerAdapter extends FragmentPagerAdapter {
        //viewPager适配器
        public ContainerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return mLoginFragment;

        }

        @Override
        public int getCount() {
            return 1;
        }

    }
//    /**
//     * 点击返回键退出程序
//     */
//    private static Boolean isExit = false;
//    private Handler mHandler = new Handler();
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (isExit == false) {
//                isExit = true;
//                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        isExit = false;
//                    }
//                }, 2000);
//            } else {
//                finish();
//                System.exit(0);
//            }
//        }
//        return false;
//    }
}
