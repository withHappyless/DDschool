package cn.ddshcool.utils;

/**
 * Bmob异常处理工具类
 *      根据错误code值 返回相应错误结果
 */
public class BmobExceptionUtil {

    //返回错误的描述
    public static String StringException(int code){
        switch (code){
            case 101:
                return "学号或密码错误!";
            case 202:
                return "这号注册过啦!";
            case 205:
                return "该学号还没注册,快快注册吧!";
            case 6666:
                return "注册成功";
            case 8888:
                return "登陆成功";
            case 9010:
                return "网络繁忙,请稍后再试...";
            case 9016:
                return "无法连接到网络,请检查网络配置";
        }

        return "";
    }

    //返回错误类型 --- 暂时用不到
    public static int IntException(int code){

        return 1;
    }
}
