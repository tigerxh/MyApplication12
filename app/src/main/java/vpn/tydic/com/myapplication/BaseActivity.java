package vpn.tydic.com.myapplication;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initView();
        registerHandler();
        initDate();
    }
    public abstract void setContentView();
    public abstract void initView();
    public abstract void registerHandler();
    public abstract void initDate();
}
