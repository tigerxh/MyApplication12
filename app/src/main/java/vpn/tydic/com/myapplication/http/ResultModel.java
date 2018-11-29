package vpn.tydic.com.myapplication.http;

import java.io.Serializable;

/**
 * 创建人 胡焕
 * 创建时间 2016年12月13日  15:19
 * 用途
 */

public class ResultModel implements Serializable {
    private String RES_DATA;
    private String RES_CODE;
    private String RES_DESC;


    public String getRES_DATA() {
        return RES_DATA;
    }

    public void setRES_DATA(String RES_DATA) {
        this.RES_DATA = RES_DATA;
    }

    public String getRES_CODE() {
        return RES_CODE;
    }

    public void setRES_CODE(String RES_CODE) {
        this.RES_CODE = RES_CODE;
    }

    public String getRES_DESC() {
        return RES_DESC;
    }

    public void setRES_DESC(String RES_DESC) {
        this.RES_DESC = RES_DESC;
    }


}
