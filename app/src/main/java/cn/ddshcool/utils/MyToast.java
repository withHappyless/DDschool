package cn.ddshcool.utils;

import android.content.Context;
import android.widget.Toast;


/**
 * 自定义Toast类
 */
public class MyToast extends Toast {

    public static Toast mToast=null;

    public MyToast(Context context) {
        super(context);
    }

    public static void showToast(Context context ,String msg , int duration){

        //总觉得缺点什么  到时候补上


        if(mToast==null){
            //设置文本属性
            mToast=Toast.makeText(context,msg,duration);
        }else{
            //设置文字
            mToast.setText(msg);
            //设置持续时间
            mToast.setDuration(duration);
        }
        mToast.show();
    }
}
