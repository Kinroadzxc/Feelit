package com.kinroad.feelit.Util;

public interface HttpCallbackListener {

    void onFinish(String response);
    void onError(Exception e);
}
