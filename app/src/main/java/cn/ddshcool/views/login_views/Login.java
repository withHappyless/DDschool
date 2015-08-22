package cn.ddshcool.views.login_views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.ddshcool.main.R;

/**
 * 主界面
 */
public class Login extends Fragment {

    Fragment mLoginFragment,mRegisterFragment;
    ViewPager mViewPager;

    private TextView mLoginText, mRegisterText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //链接布局文件
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_login, null);

        //实例化登陆界面
        mLoginFragment = new LoginFragment();
        mRegisterFragment = new RegisterFragment();

        //实例化登录,注册标签
        mLoginText = (TextView) view.findViewById(R.id.title_Login);
        mRegisterText = (TextView) view.findViewById(R.id.title_reg);
        //实例化切换界面
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager_fragment);

        //实现切换功能
        mRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });
        mLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });

        //实现小三角的位置移动
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    mLoginText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_triangle);
                    mRegisterText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    mLoginText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    mRegisterText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_triangle);
                }
            }
        });

        //添加适配器
        mViewPager.setAdapter(new ContainerAdapter(getFragmentManager()));
        return view;
    }

    public class ContainerAdapter extends FragmentPagerAdapter {
        //viewPager适配器
        public ContainerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return mLoginFragment;
                case 1:
                    return mRegisterFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

}
