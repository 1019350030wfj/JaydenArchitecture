package com.wfj.jaydenarchitecture.model.dao;

import com.wfj.jaydenarchitecture.model.bean.Response;

/**
 * Created by Jayden on 2015/8/25.
 */
public interface IEntityListener<T>{
    void result(Response<T> response);
}
