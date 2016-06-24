package com.linsr.dumpling.zhihu;


import android.content.Context;

import com.linsr.dumpling.log.Log;
import com.linsr.dumpling.zhihu.model.DailyDetailsPojo;
import com.linsr.dumpling.zhihu.model.DailyNewsPojo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * description
 *
 * @author Linsr
 */
public class ZhihuManager {

    private static final String TAG = "ZhihuManager";

    private ZhihuDao mZhihuDao;
    private ZhihuApi mZhihuApi;
    private Log mLog;

    private static volatile ZhihuManager mInstance;

    private ZhihuManager() {
    }

    public static ZhihuManager getInstance() {
        if (mInstance == null) {


            synchronized (ZhihuManager.class) {
                if (mInstance == null) {
                    mInstance = new ZhihuManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context, Log log) {
        mLog = log;

        mZhihuDao = new ZhihuDao(context);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        mLog.v(TAG, "Net Request Url = %s ", request.url());
                        mLog.v(TAG, "Result Json = %s ", response.body().string());
                        response = chain.proceed(request);
                        return response;
                    }
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://news-at.zhihu.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        mZhihuApi = retrofit.create(ZhihuApi.class);
    }

    public void getLatestNews(Subscriber<DailyNewsPojo> subscriber) {
        mZhihuApi.getLatestNews()
                .subscribeOn(Schedulers.io())
                .map(new Func1<DailyNewsPojo, DailyNewsPojo>() {
                    @Override
                    public DailyNewsPojo call(DailyNewsPojo dailyNewsPojo) {
                        mZhihuDao.saveDailyNewsList(dailyNewsPojo.getStories());
                        return dailyNewsPojo;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getDailyDetails(Subscriber<DailyDetailsPojo> subscriber, final String id) {

        mZhihuApi.getDailyDetails(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<DailyDetailsPojo, DailyDetailsPojo>() {
                    @Override
                    public DailyDetailsPojo call(DailyDetailsPojo dailyNewsPojo) {
                        mZhihuDao.saveDailyNewsDetails(dailyNewsPojo);
                        return dailyNewsPojo;
                    }
                })
                .subscribe(subscriber);
    }

    public void getBeforeNews(int date, Subscriber<DailyNewsPojo> subscriber) {
        mZhihuApi.getBeforeNews(date)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<DailyNewsPojo, DailyNewsPojo>() {
                    @Override
                    public DailyNewsPojo call(DailyNewsPojo dailyNewsPojo) {
                        mZhihuDao.saveDailyNewsList(dailyNewsPojo.getStories());
                        return dailyNewsPojo;
                    }
                })
                .subscribe(subscriber);
    }

    public DailyDetailsPojo readDailyNewsDetailsCache(String id) {
        return mZhihuDao.readDailyNewsDetails(id);
    }
}