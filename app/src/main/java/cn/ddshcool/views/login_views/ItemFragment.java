package cn.ddshcool.views.login_views;


import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import cn.ddshcool.entity.DeptAndYear;
import cn.ddshcool.services.adapters.DeptAndYearAdapter;
import cn.ddshcool.main.R;

/**
 * 院系 入学年份 界面
 */
public class ItemFragment extends Fragment {

    private ArrayList<String> mItems;
    private ArrayList<String> newItems;

    private EditText eSearch;

    private ImageView mBackButton;

    private ListView mList;

    private LinearLayout mSearchBackground;

    private DeptAndYearAdapter adapter;

    Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_select_dept,null);
        //初始化搜索框
        mSearchBackground = (LinearLayout) view.findViewById(R.id.ll_search);
        if(DeptAndYear.isSearch){
            //初始化院系
            mItems = DeptAndYear.itemOfDept;
            mSearchBackground.setVisibility(View.VISIBLE);
        }else{
            //初始 年级
            mItems = DeptAndYear.itemOfYear;
            mSearchBackground.setVisibility(View.GONE);
        }

        newItems = new ArrayList<String>();
        for (String addItem : mItems){
            newItems.add(addItem);
        }

        //初始化控件

        mBackButton = (ImageView) view.findViewById(R.id.iv_back);
        mList = (ListView) view.findViewById(R.id.lv_dept);
        adapter = new DeptAndYearAdapter(getActivity(),R.layout.item_dept_year,newItems);
        mList.setAdapter(adapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(DeptAndYear.isSearch){
                    //回传值
                    DeptAndYear.selectDept = newItems.get(position);
                }else{
                    //回传值
                    DeptAndYear.selectYear = newItems.get(position);
                }
                //刷新注册界面信息
                DeptAndYear.refreshDeptAndYear();
                ((InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                //返回上一级fragment
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                //返回上一级fragment
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        //根据搜索框中文本的改变 动态展示数据
        eSearch = (EditText) view.findViewById(R.id.et_search);
        eSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //动态改变数据
                mHandler.post(eChanged);
            }
        });

        return view;
    }

    Runnable eChanged = new Runnable() {

        @Override
        public void run() {
            String data = eSearch.getText().toString();

            newItems.clear();

            getDataSub(data);   //获取更新数据

            adapter.notifyDataSetChanged();//刷新界面

        }

    };

    private void getDataSub(String data){

        //问题就出在这  出在连表上 因为arrayList指向的是字符串常量池中的数据！
        Log.i("看看搜索问题", DeptAndYear.itemOfDept.size()+"");
        for (int i = 0; i< DeptAndYear.itemOfDept.size() ; i++){
            if (DeptAndYear.itemOfDept.get(i).contains(data)){
                newItems.add(DeptAndYear.itemOfDept.get(i));
                newItems.add("其他院系");
                return;
            }
        }
        newItems.add("其他院系");
    }





}
