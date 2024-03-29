package vpn.tydic.com.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SPUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;


public class MainActivity extends BaseActivity {
    public BridgeWebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.transparentStatusBar(this);
        BarUtils.setStatusBarLightMode(this,true);

    }
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView() {
        webView=findViewById(R.id.webView);
        BarUtils.addMarginTopEqualStatusBarHeight(webView);
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.colorAccent));
    }

    @Override
    public void registerHandler() {
        webView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i("MainActivity", "得到JS传过来的数据 data =" + data);
                Toast.makeText(MainActivity.this,data,Toast.LENGTH_LONG).show();
                function.onCallBack("传递数据给JS");
            }
        });
    }

    @Override
    public void initDate() {
        webView.loadUrl("file:android_asset/demo.html");
        SPUtils.getInstance().put("app","dddd");

    }

}
