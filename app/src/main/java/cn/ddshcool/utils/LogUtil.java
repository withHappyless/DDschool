package cn.ddshcool.utils;

import android.util.Log;

/**
 * 日志工具类,方便打印处理
 */
public class LogUtil {
    private static final int DEBUG=1;
    private static final int ERROR=2;
    private static final int WARING=3;
    private static final int INFO=4;
    private static final int VERBOSE=5;
    private static final int NORMAL=0;
    private static final int LEVEL=DEBUG;

    public static void i(String trg,String msg){
        if(LEVEL>=INFO){
            Log.i(trg, msg);
        }
    }

    public static void d(String trg,String msg){
        if(LEVEL>=DEBUG){
            Log.d(trg, msg);
        }
    }

    public static void e(String trg,String msg){
        if(LEVEL>=ERROR){
            Log.e(trg, msg);
        }
    }

    public static void v(String trg,String msg){
        if(LEVEL>=VERBOSE){
            Log.v(trg, msg);
        }
    }

    public static void w(String trg,String msg){
        if(LEVEL>=WARING){
            Log.w(trg, msg);
        }
    }


}
