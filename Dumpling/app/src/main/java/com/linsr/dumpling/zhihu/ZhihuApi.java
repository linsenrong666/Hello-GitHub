package com.linsr.dumpling.zhihu;


import com.linsr.dumpling.zhihu.model.DailyDetailsPojo;
import com.linsr.dumpling.zhihu.model.DailyNewsPojo;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * description
 *
 * @author Linsr
 */
public interface ZhihuApi {

    @GET("4/news/latest")
    Observable<DailyNewsPojo> getLatestNews();

    @GET("4/news/{dailyId}")
    Observable<DailyDetailsPojo> getDailyDetails(@Path("dailyId") String dailyId);

    @GET("4/news/before/{date}")
    Observable<DailyNewsPojo> getBeforeNews(@Path("date") int date);

}
