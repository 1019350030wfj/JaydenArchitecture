package com.wfj.jaydenarchitecture.model.dao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wfj.jaydenarchitecture.model.bean.Response;
import com.wfj.jaydenarchitecture.model.cache.DataProvider;

import fbcore.log.LogUtil;
import fbcore.task.AsyncTaskManager;
import fbcore.task.SyncTask;
import fbcore.task.TaskFailure;
import fbcore.task.TaskHandler;
import fbcore.task.toolbox.GetStringTask;


/**
 * @function 真正获取数据的操作类
 * Created by Jayden on 2015/8/25.
 */
public class Dao {

    private static final String TAG = "Dao";

    private static void getAsyncString(final String paramUrl, final TaskHandler handler) {
        AsyncTaskManager.INSTANCE.execute(new SyncTask() {
            @Override
            protected Object execute() {
                String url = UrlBuilder.getPublicParamUrl().append(paramUrl).toString();
                LogUtil.i(TAG, "url=" + url);
                return new GetStringTask(url).execute();
            }
        }, handler);
    }

    public static <T> void getEntity(String paramUrl, TypeToken<Response<T>> typeToken,
                                     IEntityListener<T> listener, boolean cache) {
        getEntity(paramUrl, typeToken, listener, cache, true);
    }

    public static <T> void getEntity(final String paramUrl, final TypeToken<Response<T>> typeToken,
                                     final IEntityListener<T> listener, final boolean cache, boolean showToast) {
        //首先检查是否有网络,无网络从缓存获取数据
        if (!checkNetwork(paramUrl, typeToken, listener, cache, showToast)) {
            return;
        }
        getAsyncString(paramUrl, new TaskHandler() {

            @Override
            public void onSuccess(Object result) {
                //网络有数据，返回网络数据，后台更新缓存
                if (result != null) {
                    LogUtil.i(TAG, result + "");
                    Response<T> o;
                    try {
                        o = new Gson().fromJson((String) result, typeToken.getType());
                        listener.result(o);
                        returnCode2Toast(o.code);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        LogUtil.e("", "接口解析数据失败");
                        return;
                    }
                    if (o.code != ReturnCode.RS_EMPTY_ERROR) {
                        //更新缓存
//                        DataProvider.getInstance().putStringToDisker((String) result,
//                                UrlFilter.getFileNameFromUrl(paramUrl));
                    }
                    return;
                }

                getDataFromCache(false, paramUrl, typeToken, listener, cache);
            }

            @Override
            public void onFail(TaskFailure taskFailure) {

            }

            @Override
            public void onProgressUpdated(Object... objects) {

            }
        });
    }

    private static <T> void getDataFromCache(final boolean noNet, final String paramUrl, final TypeToken<Response<T>> token,
                                             final IEntityListener<T> listener, final boolean cache) {
        AsyncTaskManager.INSTANCE.execute(new SyncTask() {

            @Override
            protected Object execute() {
                // 网络无数据，如果需要读缓存，返回缓存
                if (cache) {
                    String cacheJsonStr = DataProvider.getInstance().getCacheFromDisker(UrlFilter.getFileNameFromUrl(paramUrl));

                    // 缓存无数据
                    if (cacheJsonStr == null) {
                        Response<T> o = new Gson().fromJson(createEmptyJson(noNet ? ReturnCode.NO_NET
                                : ReturnCode.RS_EMPTY_ERROR), token.getType());
                        return o;
                    }

                    // 缓存有数据
                    Response<T> o = new Gson().fromJson(cacheJsonStr, token.getType());
                    return o;
                }

                // 网络和缓存均没有数据
                Response<T> o = new Gson().fromJson(createEmptyJson(noNet ? ReturnCode.NO_NET : ReturnCode.RS_EMPTY_ERROR),
                        token.getType());
                return o;
            }

        }, new TaskHandler() {

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(Object result) {
                listener.result((Response<T>) result);
            }

            @Override
            public void onProgressUpdated(Object... params) {
            }

            @Override
            public void onFail(TaskFailure failure) {
            }
        });
    }

    /**
     * @function 检查是否有网络，没有网络读取缓存数据
     * @param paramUrl
     * @param typeToken
     * @param listener
     * @param cache
     * @param showToast
     * @param <T>
     * @return
     */
    private static <T> boolean checkNetwork(String paramUrl, TypeToken<Response<T>> typeToken, IEntityListener<T> listener, boolean cache, boolean showToast) {
//        if (!AppContext.isNetworkAvailable()) {
//            if(showToast) {
//                ToastUtil.shortT(AppContext.getContext(), AppContext.getContext().getResources().getString(R.string.not_network));
//            }
//            getDataFromCache(true, paramUrl, token, listener, needCache);
//            return false;
//        }
        return true;
    }

    private static void returnCode2Toast(int returnCode) {
    }

    private static String createEmptyJson() {
        return createEmptyJson(ReturnCode.RS_EMPTY_ERROR);
    }

    private static String createEmptyJson(int rsCode) {
        return new Gson().toJson(new Response<Object>(rsCode));
    }
}
