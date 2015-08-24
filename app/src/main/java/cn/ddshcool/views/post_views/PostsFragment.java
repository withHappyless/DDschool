package cn.ddshcool.views.post_views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.ddshcool.entity.BmobBean.Post;
import cn.ddshcool.main.R;
import cn.ddshcool.services.adapters.PostsAdapter;
import cn.ddshcool.utils.MyApplication;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * 吐槽板块的fragment
 */
public class PostsFragment extends Fragment {

    private View view;  //用于加载布局的view
    private PullToRefreshListView lv_post_list;//展示帖子listview
    private View footView;  //底部加载更多view
    //下拉刷新菜单
    private PullToRefreshLayout myRefershLayout;
    //发帖按钮
    private FloatingActionButton floatingActionButton;

    private PostsAdapter postsAdapter;
    private List<Post> postList = new ArrayList<Post>();
    private BmobQuery<Post> queryPost = new BmobQuery<Post>();

    private int curPage = 1;    //当前页数
    private int pagePostCount = 10; //每页帖子个数

    private PostsAdapter adapter;

    private boolean isFirst;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        isFirst=true;
        queryPost.setLimit(pagePostCount); // 限制每页多少数据
        initView();
        loadData(1);    //载入数据

        return view;

    }

    //初始化控件
    private void initView(){
        //加载布局
        view = View.inflate(MyApplication.getmContext(), R.layout.fragment_posts,null);

        myRefershLayout = (PullToRefreshLayout) view.findViewById(R.id.ptr_post_refresh);
        //初始化下拉控件
        ActionBarPullToRefresh.from(getActivity())
                .options(Options.create()
                                .scrollDistance(.65f)
                                .build()
                )
                .allChildrenArePullable()
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        //获取新数据
                        loadData(1);
                    }

                })
                .setup(myRefershLayout);

        lv_post_list = (PullToRefreshListView) view.findViewById(R.id.lv_post_list);

        adapter = new PostsAdapter(getActivity(),postList);
        lv_post_list.setAdapter(adapter);   //添加适配器

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_add_button);
        floatingActionButton.setBackgroundResource(R.drawable.action_btn_normal);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                startActivity(intent);

            }
        });

        lv_post_list.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                loadData(curPage + 1);
            }

        });

        footView = View.inflate(getActivity(), R.layout.footview_loading_more, null);   //加载更多布局

    }

    public void loadData(final int page){

//        if(isFirst){
//            //先从缓存获取 缓存中没有 再从网络获取
//            queryPost.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
//            isFirst = false;
//        }else{
//            //先从网络获取 失败后 从缓存中获取
//            queryPost.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
//        }


        queryPost.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
        //没有调用刷新动画
        queryPost.include("author");    //同时查询发帖人信息
        queryPost.order("createdAt");   //按照发帖时间排序
        myRefershLayout.setRefreshing(true);
        queryPost.setSkip(page-1 * 10);  //取出当前页数中数据
        queryPost.findObjects(getActivity(), new FindListener<Post>() {
            @Override
            public void onSuccess(List<Post> list) {
                Log.e("postListCount", list.size()+"");
                //如果是第一页就清除缓存 (说明是下拉刷新)
                if (page == 1) {
                    postList.clear();
                }
                curPage = page;
                addDatas(list);
                myRefershLayout.setRefreshComplete();

            }

            @Override
            public void onError(int i, String s) {
                //结束缓存动画
                myRefershLayout.setRefreshComplete();

            }
        });


    }

    private void addDatas(List<Post> posts){

        for(Post post : posts){
            //判断是否已有该帖子
            if(!postList.contains(post)){
                postList.add(post);
            }
        }
        adapter.notifyDataSetChanged();     //刷新listView

        if(!posts.isEmpty()){
            addFootView(lv_post_list,footView);
        }else{
            removeFootView(lv_post_list, footView);
        }

    }

    private void addFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if(lv.getFooterViewsCount() == 1) {
            lv.addFooterView(footView);
        }
    }

    private void removeFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if(lv.getFooterViewsCount() > 1) {
            lv.removeFooterView(footView);
        }
    }


}
