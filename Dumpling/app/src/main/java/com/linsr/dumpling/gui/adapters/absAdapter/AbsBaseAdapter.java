package com.linsr.dumpling.gui.adapters.absAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;


/**
 * Created by Linsr on 12/7.
 *
 * @author Linsr
 */
public abstract class AbsBaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Context mContext;
    protected List<T> mList;
    protected LayoutInflater mInflater;

    public AbsBaseAdapter(Context context,
                          List<T> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

}
