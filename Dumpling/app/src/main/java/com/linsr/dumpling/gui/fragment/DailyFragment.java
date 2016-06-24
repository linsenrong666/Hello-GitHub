package com.linsr.dumpling.gui.fragment;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linsr.dumpling.R;
import com.linsr.dumpling.app.Constants;
import com.linsr.dumpling.gui.activity.DailyDetailsActivity;
import com.linsr.dumpling.gui.activity.MainActivity;
import com.linsr.dumpling.gui.adapters.DailyNewsAdapter;
import com.linsr.dumpling.gui.adapters.ZhihuDailyAdapter;
import com.linsr.dumpling.gui.adapters.absAdapter.OnItemClickListener;
import com.linsr.dumpling.gui.widgets.RefreshLayout;
import com.linsr.dumpling.zhihu.ZhihuManager;
import com.linsr.dumpling.zhihu.model.DailyNewsPojo;
import com.linsr.dumpling.zhihu.model.StoriesPojo;
import com.linsr.dumpling.zhihu.provider.Zhihu;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * description
 *
 * @author Linsr
 */
public class DailyFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        OnItemClickListener, RefreshLayout.OnPullRefreshListener, RefreshLayout.OnPushLoadMoreListener {

    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
//    private DailyNewsAdapter mDailyNewsAdapter;
    private ZhihuDailyAdapter mZhihuDailyAdapter;

    private ZhihuManager mZhihuManager;
    private List<StoriesPojo> mStoriesPojoList;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mZhihuManager = ZhihuManager.getInstance();
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhihu, null);

        setTitleText(R.string.zhihu_daily);
        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.zhihu_refresh_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.zhihu_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

//        mDailyNewsAdapter = new DailyNewsAdapter(mActivity,
//                mActivity.getContentResolver().query(Zhihu.DailyNews.CONTENT_URI,
//                        DailyNewsAdapter.MAPPING,
//                        DailyNewsAdapter.DAILY_NEWS_WHERE,
//                        new String[]{String.valueOf(date)},
//                        DailyNewsAdapter.DAILY_NEWS_SORT));
//        mDailyNewsAdapter.setOnItemClickListener(this);
//        mRecyclerView.setAdapter(mDailyNewsAdapter);

        mStoriesPojoList = new ArrayList<>();
        mZhihuDailyAdapter = new ZhihuDailyAdapter(mContext, mStoriesPojoList);
        mZhihuDailyAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mZhihuDailyAdapter);

        mRefreshLayout.setOnPullRefreshListener(this);
        mRefreshLayout.setOnPushLoadMoreListener(this);

//        getLoaderManager().initLoader(0, null, this);

        getLatestNews();

        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).setFloatingActionButtonClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecyclerView.smoothScrollToPosition(0);
                }
            });
        }

        return view;
    }

    int date;

    private void getLatestNews() {
        mZhihuManager.getLatestNews(new Subscriber<DailyNewsPojo>() {

            @Override
            public void onNext(DailyNewsPojo dailyNewsPojo) {
                date = dailyNewsPojo.getDate();
                mLog.i(TAG, "onRefresh Date = %s ", date);
                mStoriesPojoList.addAll(dailyNewsPojo.getStories());
                mZhihuDailyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCompleted() {
                mLog.i(TAG, "onRefresh onCompleted ");
            }

            @Override
            public void onError(Throwable e) {
                mLog.i(TAG, "onRefresh onError");
            }

        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mActivity, Zhihu.DailyNews.CONTENT_URI, DailyNewsAdapter.MAPPING, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        mDailyNewsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        mDailyNewsAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(View view) {
        String id = (String) view.getTag(R.id.daily_id);
        Intent intent = new Intent(mActivity, DailyDetailsActivity.class);
        intent.putExtra(Constants.DAILY_ID, id);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mStoriesPojoList.clear();
        mRefreshLayout.setRefreshing(false);
        getLatestNews();
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

    @Override
    public void onLoadMore() {
        mRefreshLayout.setLoadMore(false);
        mZhihuManager.getBeforeNews(date, new Subscriber<DailyNewsPojo>() {
            @Override
            public void onCompleted() {
                mLog.i(TAG, "onLoadMore onCompleted ");
            }

            @Override
            public void onError(Throwable e) {
                mLog.i(TAG, "onLoadMore onError");
            }

            @Override
            public void onNext(DailyNewsPojo dailyNewsPojo) {
                date = dailyNewsPojo.getDate();
                mLog.i(TAG, "onLoadMore Date = %s ", date);
                mStoriesPojoList.addAll(dailyNewsPojo.getStories());
                mZhihuDailyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onPushDistance(int distance) {

    }

    @Override
    public void onPushEnable(boolean enable) {

    }
}
