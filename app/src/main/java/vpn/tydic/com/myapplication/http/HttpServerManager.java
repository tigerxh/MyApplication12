package vpn.tydic.com.myapplication.http;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import vpn.tydic.com.myapplication.BuildConfig;
import vpn.tydic.com.myapplication.MyApplication;
import vpn.tydic.com.myapplication.base.Constants;
import com.facebook.stetho.okhttp3.StethoInterceptor;

/**
 * Created by Tiger on 2018/11/2.
 */

public class HttpServerManager {
    private static HttpServerManager instance;
    private OkHttpClient client;
    private ServerApi serverApi;

    public static HttpServerManager getInstance() {
        if (instance == null)
            instance = new HttpServerManager();
        return instance;
    }

    private HttpServerManager() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if(BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        client = builder.addInterceptor(interceptor)

                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        String token = "";  //这里自行获取token,一般在登录成功后获取到token进行保存
                        Request request = chain.request()
                                .newBuilder()
//                        .addHeader("tooken", token)  //header由具体项目决定.这里只是写了个示例.
                                .build();
                        return chain.proceed(request);
                    }
                })
                .readTimeout(1000 * 20, TimeUnit.MILLISECONDS).writeTimeout(1000 * 50, TimeUnit
                        .MILLISECONDS).build();
        Retrofit retrofit = new Retrofit.Builder().client(client).addConverterFactory
                (GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.HTTP_SERVER_BASE_URL)
                .build();
        serverApi = retrofit.create(ServerApi.class);
    }
    public ServerApi getServerApi() {
        return serverApi;
    }

    /**
     * 获取保存的token,具体实现由项目决定.
     *
     * @author 胡焕
     * @date 2016/11/23 11:44
     */
    private String getSavedToken() {
        return "";
    }
}

