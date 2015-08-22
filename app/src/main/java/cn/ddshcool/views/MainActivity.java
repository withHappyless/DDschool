package cn.ddshcool.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import cn.ddshcool.entity.BaseActionbarActivity;
import cn.ddshcool.entity.DeptAndYear;
import cn.ddshcool.main.R;
import cn.ddshcool.services.adapters.DeptAndYearAdapter;
import cn.ddshcool.views.post_views.NewPostActivity;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


/**
 * Created by yosemite on 15/8/10.
 */
public class MainActivity extends BaseActionbarActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    //下拉刷新菜单
    private PullToRefreshLayout myRefershLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();     //初始化控件

        ActionBarPullToRefresh.from(this)
                .options(Options.create()
                                .scrollDistance(.65f)
                                .build()
                        )
                .allChildrenArePullable()
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                myRefershLayout.setRefreshComplete();
                            }
                        }, 3000);
                    }

                })
                .setup(myRefershLayout);

        //设置toolbar相关属性
        mToolbar.setTitle("叨叨校园");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.register_option_year, R.string.register_option_dept);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        //夜间模式

        ListView listView = (ListView) findViewById(R.id.listview1);
        DeptAndYearAdapter adapter = new DeptAndYearAdapter(this,R.layout.item_dept_year, DeptAndYear.itemOfDept);
        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundResource(R.drawable.action_btn_normal);
        fab.attachToListView(listView, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {
                Log.d("ListViewFragment", "onScrollDown()");
            }

            @Override
            public void onScrollUp() {
                Log.d("ListViewFragment", "onScrollUp()");
            }
        }, new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("ListViewFragment", "onScrollStateChanged()");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("ListViewFragment", "onScroll()");
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
                startActivity(intent);

            }
        });





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
        myRefershLayout = (PullToRefreshLayout) findViewById(R.id.ptr_main_activity_refresh);





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

    private boolean isExit = false;
    private Handler mHandler = new Handler();


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //弹出左侧菜单栏
        if(keyCode == KeyEvent.KEYCODE_BACK){

            //调用呼出菜单函数

        }else if(keyCode == KeyEvent.KEYCODE_MENU){
            //退出程序
            if(!isExit){

                isExit = true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                },2000);

            }else{

                finish();
//                ActivityCollection.finishAll();
                System.exit(0);

            }

        }

        return false;       //有点疑问？
    }
}
