package vpn.tydic.com.myapplication.http;

import android.provider.SyncStateContract;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import vpn.tydic.com.myapplication.base.Constants;
import vpn.tydic.com.myapplication.security.AESHelper;
import vpn.tydic.com.myapplication.security.Base64;
import vpn.tydic.com.myapplication.security.MD5;

/**
 * Created by Tiger on 2018/11/2.
 */

public class RequestParam<T> {
    //接口编号
    public String INTF_CODE;

    private ServiceParam SERVICE_PARAM;


    public RequestParam() {
        SERVICE_PARAM = new ServiceParam();
    }

    @Expose
    private static Gson mGson = new Gson();


    public void setINTF_CODE(String INTF_CODE) {
        this.INTF_CODE = INTF_CODE;
    }

    public ServiceParam getSERVICE_PARAM() {
        return SERVICE_PARAM;
    }

    public void setSERVICE_PARAM(String json) {
        String check = MD5.md5for32(Base64.encode(json.getBytes()) + Constants.CHECK_STR);
        SERVICE_PARAM.check = check;
    }


    public void setSign(String json) {
        String data = AESHelper.encrypt(json,
                Constants.AES_PASSWORD);
        SERVICE_PARAM.sign = data;
    }

    public void setData(T data) {
        String json = mGson.toJson(data);
        Log.e("RequestParam---data", json);
        setSERVICE_PARAM(json);
        setSign(json);
    }
}
