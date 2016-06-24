package com.linsr.dumpling.gui.fragment;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linsr.dumpling.gui.activity.BaseActivity;
import com.linsr.dumpling.log.Log;
import com.linsr.dumpling.log.LogImpl;

/**
 * description
 *
 * @author Linsr
 */
public abstract class BaseFragment extends Fragment {

    protected String TAG;
    protected Context mContext;
    protected Activity mActivity;
    protected Log mLog;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        TAG = getClass().getSimpleName();
        mActivity = context;
        mContext = mActivity.getApplicationContext();
        mLog = LogImpl.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    public void setTitleText(CharSequence title) {
        if (mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).setTitleText(title);
        }
    }

    public void setTitleText(int title) {
        if (mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).setTitleText(title);
        }
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
