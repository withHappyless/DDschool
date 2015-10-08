package cn.ddshcool.entity;

import android.os.Bundle;

import cn.ddshcool.main.R;
import cn.ddshcool.services.ActivityCollection;
import cn.ddshcool.utils.LogUtil;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by yosemite on 15/9/25.
 */
public class BaseBackActivity extends SwipeBackActivity {
    private SwipeBackLayout mSwipeBackLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i("BaseActivity", getClass().getSimpleName());
        ActivityCollection.addActivity(this);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        mSwipeBackLayout.setScrimColor(getResources().getColor(R.color.transparent));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityCollection.removeActivity(this);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
    }
}
