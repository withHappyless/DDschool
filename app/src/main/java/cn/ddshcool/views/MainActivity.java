package cn.ddshcool.views;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import cn.ddshcool.entity.BaseActionbarActivity;
import cn.ddshcool.main.R;
import cn.ddshcool.views.post_views.PostsFragment;


/**
 * Created by yosemite on 15/8/10.
 */
public class MainActivity extends BaseActionbarActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new PostsFragment();
        fragmentTransaction.replace(R.id.fl_main,fragment);

        fragmentTransaction.commit();

        initView();     //初始化控件

        //设置toolbar相关属性
        mToolbar.setTitle("叨叨校园");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.register_option_year, R.string.register_option_dept);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);


//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.action_settings:
//                        MyToast.showToast(MainActivity.this,"action_settings", Toast.LENGTH_SHORT);
//                        break;
//                    case R.id.action_share:
//                        MyToast.showToast(MainActivity.this,"action_share",Toast.LENGTH_SHORT);
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });

    }

    public void initView(){
        mToolbar = (Toolbar) findViewById(R.id.tb_main_toolbar);
        //下拉刷新控件
//        myRefershLayout = (PullToRefreshLayout) findViewById(R.id.ptr_main_activity_refresh);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

//    private boolean isExit = false;
//    private Handler mHandler = new Handler();
//
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //弹出左侧菜单栏
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//
//            //调用呼出菜单函数
//
//        }else if(keyCode == KeyEvent.KEYCODE_MENU){
//            //退出程序
//            if(!isExit){
//
//                isExit = true;
//                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        isExit = false;
//                    }
//                },2000);
//
//            }else{
//
//                finish();
////                ActivityCollection.finishAll();
//                System.exit(0);
//
//            }
//
//        }
//
//        return true;       //有点疑问？
//    }

}
