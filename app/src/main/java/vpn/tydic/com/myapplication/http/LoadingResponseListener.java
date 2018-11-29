package vpn.tydic.com.myapplication.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vpn.tydic.com.myapplication.base.Constants;
import vpn.tydic.com.myapplication.security.AESHelper;
import vpn.tydic.com.myapplication.views.LoadingDialog;

/**
 * 创建人 胡焕
 * 创建时间 2016年12月13日  11:29
 */
public abstract class LoadingResponseListener<T extends ResultModel> implements Callback<T> {

    private LoadingDialog loadingDialog;
    Context context;


    public LoadingResponseListener(Context context) {
        this.context = context;
        if (context != null && needDialog()) {
            loadingDialog = new LoadingDialog(context);
            loadingDialog.show();
        }
    }
    public void closeDialog(){
        if(loadingDialog!=null&&loadingDialog.isShowing())
            loadingDialog.dismiss();

    }

    public LoadingResponseListener(Context context, String laodText) {
        this.context = context;
        if (context != null && needDialog()) {
            loadingDialog = new LoadingDialog(context, laodText);
            loadingDialog.show();
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (loadingDialog != null) {
            try {
                loadingDialog.dismiss();
            } catch (Exception e) {

            }
        }
        if (response.code() == 200) {
            if (response.body() != null && response.body().getRES_CODE() != null) {
                String resultCode = response.body().getRES_CODE();
                if (HttpResponseCode.SUCCESS.equals(resultCode)) {
                    String dataEncrypted = response.body().getRES_DATA();
                    String jsonData = AESHelper.decrypt(dataEncrypted, Constants.AES_PASSWORD);
                    Log.e("data", jsonData);
                    onSuccess(jsonData);
                } else {
                    onResultFailure(response.body());
                }
            }
        } else if (response.code() == 404) {
            onFailure(call, new Exception("没有找到指定资源"));
        } else {
            onFailure(call, new Exception("请求失败:" + response.code()));
        }
    }

    public abstract void onSuccess(String json);

    public void onResultFailure(ResultModel resultModel) {
        Toast.makeText(context, resultModel.getRES_DESC(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (loadingDialog != null)
            loadingDialog.dismiss();
        if (t != null && t.getMessage() != null && t.getMessage().contains("failed to connect to")) {
            Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
        }
//        Toast.makeText(context, "无法连接到服务器,请检查网络设置", Toast.LENGTH_LONG).show();
    }

    protected boolean needDialog() {
        return true;
    }
}
