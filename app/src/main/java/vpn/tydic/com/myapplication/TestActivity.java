package vpn.tydic.com.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

public class TestActivity  extends BaseActivity {
    public BridgeWebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView() {
        webView=findViewById(R.id.webView);
    }

    @Override
    public void registerHandler() {
        webView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i("MainActivity", "得到JS传过来的数据 data =" + data);
                Toast.makeText(TestActivity.this,data,Toast.LENGTH_LONG).show();
                function.onCallBack("传递数据给JS");
            }
        });
    }

    @Override
    public void initDate() {
        webView.loadUrl("file:android_asset/demo.html");

    }

}
