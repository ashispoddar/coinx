package com.x.coin.coinx2.model;

/**
 * Created by ashispoddar on 10/16/15.
 */
public class ServiceError {
    int errNum;
    String errMsg;
    String correlationID;

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }
}
