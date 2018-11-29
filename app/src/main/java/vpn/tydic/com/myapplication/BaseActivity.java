package vpn.tydic.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import java.util.Stack;

import butterknife.ButterKnife;

/**
 * Created by Tiger on 2018/11/2.
 */

public abstract class BaseActivity extends FragmentActivity {
    protected static Stack<Activity> mActivityStack = new Stack<Activity>();
    protected String TAG = "";
    protected String MODE_KEY = "modl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityStack.add(this);
        TAG = this.getClass().getSimpleName();
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }
}
