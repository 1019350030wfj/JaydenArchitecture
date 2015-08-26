package com.wfj.jaydenarchitecture.model.manager;

public interface LoadListener {
    void onSuccess();

    void onFail(int code);
}