package com.linsr.dumpling.gui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linsr.dumpling.R;
import com.linsr.dumpling.gui.adapters.absAdapter.AbsBaseAdapter;
import com.linsr.dumpling.gui.adapters.absAdapter.OnItemClickListener;
import com.linsr.dumpling.zhihu.model.StoriesPojo;

import java.util.List;

/**
 * description
 *
 * @author Linsr
 */
public class ZhihuDailyAdapter extends AbsBaseAdapter<StoriesPojo, ZhihuDailyAdapter.DailyHolder> {

    public ZhihuDailyAdapter(Context context, List<StoriesPojo> list) {
        super(context, list);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public DailyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_zhihu_daily, parent, false);
        return new DailyHolder(view);
    }

    @Override
    public void onBindViewHolder(DailyHolder holder, int position) {
        StoriesPojo storiesPojo = mList.get(position);
        holder.title.setText(storiesPojo.getTitle());

        Glide.with(mContext).load(storiesPojo.getImages()[0]).into(holder.image);

        holder.layout.setTag(R.id.daily_id, storiesPojo.getId());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v);
                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    static class DailyHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView image;
        private CardView layout;

        public DailyHolder(View itemView) {
            super(itemView);
            layout = (CardView) itemView.findViewById(R.id.daily_card);
            title = (TextView) itemView.findViewById(R.id.daily_tv_title);
            image = (ImageView) itemView.findViewById(R.id.daily_iv_image);
        }
    }
}
