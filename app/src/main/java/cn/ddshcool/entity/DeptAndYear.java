package cn.ddshcool.entity;

import android.widget.TextView;

import java.util.ArrayList;

/**
 * 学年 和 院系 工具类
 */
public class DeptAndYear {

    public static ArrayList<String> itemOfYear = new ArrayList<String>();
    public static ArrayList<String> itemOfDept = new ArrayList<String>();
    public static boolean isSearch=false;
    //选中的院系
    public static String selectDept="计算机学院";
    //选中的学年
    public static String selectYear="2015年";

    private static TextView deptText;
    private static TextView yearText;

    public static void initDeptAndYear(){
        //初始化年份
        itemOfYear.add("2015年");
        itemOfYear.add("2014年");
        itemOfYear.add("2013年");
        itemOfYear.add("2012年");
        itemOfYear.add("2011年");
        itemOfYear.add("2010年");
        itemOfYear.add("2009年");
        itemOfYear.add("2008年");
        itemOfYear.add("2007年");
        itemOfYear.add("2006年");
        itemOfYear.add("2005年");
        itemOfYear.add("2004年");
        itemOfYear.add("2003年");
        itemOfYear.add("2002年");
        itemOfYear.add("2001年");
        //初始化院系
        itemOfDept.add("计算机学院");
        itemOfDept.add("传媒学院");
        itemOfDept.add("俄罗斯语言学院");
        itemOfDept.add("初等教育学院");
        itemOfDept.add("马克思主义学院");
        itemOfDept.add("音乐学院");
        itemOfDept.add("文学院");
        itemOfDept.add("法学院");
        itemOfDept.add("蒙古语言文学学院");
        itemOfDept.add("美术学院");
        itemOfDept.add("继续教育学院");
        itemOfDept.add("经济管理学院");
        itemOfDept.add("生命科学与化学学院");
        itemOfDept.add("物理与电子信息学院");
        itemOfDept.add("旅游管理与地理科学学院");
        itemOfDept.add("数学科学学院");
        itemOfDept.add("教育科学学院");
        itemOfDept.add("政治与历史学院");
        itemOfDept.add("建筑工程学院");
        itemOfDept.add("工程技术学院");
        itemOfDept.add("外国语学院");
        itemOfDept.add("体育学院");
        itemOfDept.add("其他院系");
    }

    public void initTexts(TextView deptText,TextView yearText){
        this.deptText = deptText;
        this.yearText = yearText;
    }

    public static void refreshDeptAndYear(){
        deptText.setText(DeptAndYear.selectDept);
        yearText.setText(DeptAndYear.selectYear);
    }
}
