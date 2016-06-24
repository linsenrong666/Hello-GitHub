package com.linsr.dumpling.gui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.linsr.dumpling.R;
import com.linsr.dumpling.app.Constants;
import com.linsr.dumpling.utils.ZhihuUtils;
import com.linsr.dumpling.zhihu.ZhihuManager;
import com.linsr.dumpling.zhihu.model.DailyDetailsPojo;

import rx.Subscriber;

/**
 * description
 *
 * @author Linsr
 */
public class DailyDetailsActivity extends BaseActivity {

    private ZhihuManager mZhihuManager;
    private String mDailyNewsId;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        initTitle();
        initView();

        if (mZhihuManager.readDailyNewsDetailsCache(mDailyNewsId) != null
                && !TextUtils.isEmpty(mZhihuManager.readDailyNewsDetailsCache(mDailyNewsId).getBody())) {
            String html = ZhihuUtils.processHtmlBody(mZhihuManager.readDailyNewsDetailsCache(mDailyNewsId).getBody());
            mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
        } else {
            mZhihuManager.getDailyDetails(new Subscriber<DailyDetailsPojo>() {

                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(DailyDetailsPojo dailyDetailsPojo) {
                    mLog.e(TAG, "onNext dailyDetailsPojo =%s", dailyDetailsPojo.toString());

                    String html = ZhihuUtils.processHtmlBody(dailyDetailsPojo.getBody());
                    mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
                }
            }, mDailyNewsId);
        }
    }

    private void initTitle() {

        displayBackButton();
    }

    private void initView() {


        mWebView = (WebView) findViewById(R.id.daily_details_web_view);
        WebSettings webSettings = mWebView.getSettings();
        //触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出
        mWebView.requestFocus();
        // 开启DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启database storage API功能
        webSettings.setDatabaseEnabled(true);
        // 开启Application Cache功能
        webSettings.setAppCacheEnabled(true);
        //设置缓存的模式
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置应用缓存的最大尺寸
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
    }

    private void init() {
        mDailyNewsId = getIntent().getStringExtra(Constants.DAILY_ID);
        mZhihuManager = ZhihuManager.getInstance();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();//返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
