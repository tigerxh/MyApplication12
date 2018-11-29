package vpn.tydic.com.myapplication.http;

import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vpn.tydic.com.myapplication.MyApplication;
import vpn.tydic.com.myapplication.base.Constants;
import vpn.tydic.com.myapplication.beans.LoginRequestBean;
import vpn.tydic.com.myapplication.security.AESHelper;
import vpn.tydic.com.myapplication.security.MD5;

/**
 * Created by Tiger on 2018/11/2.
 */

public class ServerLogic {
    //登录
    public void login(LoginRequestBean bean, LoadingResponseListener loadingResponseListener) {
        RequestParam<LoginRequestBean> requestParam = new RequestParam<>();
        requestParam.setINTF_CODE("INTF_LOGIN");

        LoginRequestBean loginBeanWithMD5 = new LoginRequestBean();
        loginBeanWithMD5.userAcct = bean.userAcct.trim();
        loginBeanWithMD5.userPwd = (MD5.md5for32( bean.userPwd));

        requestParam.setData(loginBeanWithMD5);
        Call<ResultModel> call = MyApplication.get().getServerApi().post(requestParam);
        call.enqueue(loadingResponseListener);
    }
    //登录
    public void RxLogin(LoginRequestBean bean, final LoadingResponseListener loadingResponseListener) {
        RequestParam<LoginRequestBean> requestParam = new RequestParam<>();
        requestParam.setINTF_CODE("INTF_LOGIN");

        LoginRequestBean loginBeanWithMD5 = new LoginRequestBean();
        loginBeanWithMD5.userAcct = bean.userAcct.trim();
        loginBeanWithMD5.userPwd = (MD5.md5for32( bean.userPwd));
        requestParam.setData(loginBeanWithMD5);

        Observable<ResultModel> observable = MyApplication.get().getServerApi().RxPost(requestParam);
        observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<ResultModel>() {
                            @Override
                            public void onCompleted() {
                                loadingResponseListener.closeDialog();
                            }

                            @Override
                            public void onError(Throwable e) {
                                loadingResponseListener.closeDialog();
                                if (e != null && e.getMessage() != null && e.getMessage().contains("failed to connect to")) {
                                    Toast.makeText(loadingResponseListener.context, "连接失败", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(loadingResponseListener.context, "请求失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNext(ResultModel resultModel) {
                                String dataEncrypted = resultModel.getRES_DATA();
                                String jsonData = AESHelper.decrypt(dataEncrypted, Constants.AES_PASSWORD);
                                Log.e("data", jsonData);
                                loadingResponseListener.closeDialog();
                                Toast.makeText(loadingResponseListener.context, jsonData, Toast.LENGTH_SHORT).show();
                            }
                        }

                );

    }




}
