package com.x.coin.coinx2.model;

import com.x.coin.coinx2.model.ServiceError;

/**
 * Created by ashispoddar on 10/16/15.
 */
public class AsyncResult {

    boolean mSuccess;
    Object mData;
    ServiceError mError;

    public AsyncResult(boolean success, Object data) {
        mSuccess = success;
        mData = data;
        if(!mSuccess) {
            //convert the data and service error
        }
    }

    public boolean isSuccess() {
        return mSuccess;
    }

    public void setSuccess(boolean success) {
        mSuccess = success;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object data) {
        mData = data;
    }

    public ServiceError getServerError() {
        return mError;
    }

    public void setServerError(ServiceError error) {
        this.mError = error;
    }
}
