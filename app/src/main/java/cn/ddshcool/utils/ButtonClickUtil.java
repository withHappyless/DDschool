package cn.ddshcool.utils;

/**
 * Created by yosemite on 15/8/5.
 */
public class ButtonClickUtil {
    private static long lastClickTime=0l;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 600) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
