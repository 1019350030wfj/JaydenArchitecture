package com.wfj.jaydenarchitecture.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseListData<E>{
    
    @SerializedName("count")
    public int count;

    @SerializedName("items")
    public List<E> items;
}
