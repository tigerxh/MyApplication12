package vpn.tydic.com.myapplication.http;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Tiger on 2018/11/2.
 */

public interface ServerApi {
    //获取注册校验码
    @POST("main/service.json")
    Call<ResultModel> post(@Body RequestParam requestParam);
    @GET("main/service.json")
    Call<ResultModel> get(@Query("id") String id);

    @POST("main/service.json")
    Observable<ResultModel> RxPost(@Body RequestParam requestParam);
}
