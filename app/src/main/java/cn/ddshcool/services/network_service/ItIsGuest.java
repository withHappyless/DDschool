package cn.ddshcool.services.network_service;

import android.content.Context;
import android.content.Intent;

import cn.bmob.v3.BmobUser;
import cn.ddshcool.entity.BmobBean.User;
import cn.ddshcool.views.login_views.LoginWindow;

/**
 * 访客模式核心代码
 */
public class ItIsGuest {

    public static boolean CheckUser(Context context){
        //获取当前用户
        User user = (User) BmobUser.getCurrentUser(context);
        if( user == null){
            Intent intent = new Intent(context, LoginWindow.class);
            context.startActivity(intent);
            return false;
        }

        return true;
    }


}
