package cn.ddshcool.services.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.ddshcool.entity.DeptAndYear;
import cn.ddshcool.main.R;

/**
 * Created by yosemite on 15/8/8.
 */
public class DeptAndYearAdapter extends ArrayAdapter<String> {

    private int resourceId;

    public DeptAndYearAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //加载布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        String item = getItem(position);
        //查找控件
        TextView itemName = (TextView) view.findViewById(R.id.tv_item_name);
        //设置文本
        itemName.setText(item);
        //设置选中项高亮显示
        if(DeptAndYear.isSearch){
            if(DeptAndYear.selectDept.equals(item)){
                //高亮显示      之前错在了重复加载
                itemName.setTextColor(getContext().getResources().getColor(R.color.btn_login_pressed));
                TextView isSelected = (TextView) view.findViewById(R.id.tv_is_selected);
                isSelected.setVisibility(View.VISIBLE);
            }
        }else{
            if(DeptAndYear.selectYear.equals(item)){
                itemName.setTextColor(getContext().getResources().getColor(R.color.btn_login_pressed));
                TextView isSelected = (TextView) view.findViewById(R.id.tv_is_selected);
                isSelected.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

}
