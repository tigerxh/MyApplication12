package vpn.tydic.com.myapplication;

import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

import java.io.File;

import vpn.tydic.com.myapplication.http.HttpServerManager;
import vpn.tydic.com.myapplication.http.ServerApi;

/**
 * Created by Tiger on 2018/11/2.
 */

public class MyApplication extends MultiDexApplication {

    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }



    }


    public static MyApplication get() {
        return application;
    }

    public String getDefRootPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/SCTobacco";
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        return path;
    }




    public ServerApi getServerApi() {
        return HttpServerManager.getInstance().getServerApi();
    }





}
