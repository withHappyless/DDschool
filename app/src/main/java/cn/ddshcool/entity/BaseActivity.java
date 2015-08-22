package cn.ddshcool.entity;

import android.app.Activity;
import android.os.Bundle;

import cn.ddshcool.services.ActivityCollection;
import cn.ddshcool.utils.LogUtil;

/**
 * Created by yosemite on 15/8/4.
 */
public class BaseActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i("BaseActivity",getClass().getSimpleName());
        ActivityCollection.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollection.removeActivity(this);
    }
}
