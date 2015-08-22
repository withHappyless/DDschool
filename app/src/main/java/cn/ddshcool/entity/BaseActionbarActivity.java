package cn.ddshcool.entity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import cn.ddshcool.services.ActivityCollection;

/**
 * Created by yosemite on 15/8/16.
 */
public class BaseActionbarActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollection.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollection.removeActivity(this);


    }
}
