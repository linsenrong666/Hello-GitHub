package com.linsr.dumpling.gui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linsr.dumpling.R;
import com.linsr.dumpling.gui.adapters.absAdapter.HeaderRecyclerViewAdapter;
import com.linsr.dumpling.zhihu.provider.Zhihu;

/**
 * description
 *
 * @author Linsr
 */
public class DailyNewsAdapter extends HeaderRecyclerViewAdapter<DailyNewsAdapter.ContentHolder> {

    public static final String[] MAPPING = {Zhihu.DailyNews._ID,//0
            Zhihu.DailyNews.ID,//1
            Zhihu.DailyNews.TITLE,//2
            Zhihu.DailyNews.IMAGE_URL,//3

    };

    public static final String DAILY_NEWS_WHERE = Zhihu.DailyNews.DATE + " =? ";

    public static final String DAILY_NEWS_SORT = Zhihu.DailyNews.DATE + " DESC ";

    public DailyNewsAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    protected ContentHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_zhihu_daily, parent, false);
        return new ContentHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(final ContentHolder holder, Cursor cursor) {
        String id = cursor.getString(1);
        String title = cursor.getString(2);
        String url = cursor.getString(3);

        holder.title.setText(title);
        Glide.with(mContext).load(url).into(holder.image);
        holder.layout.setTag(R.id.daily_id, id);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.layout);
                }
            }
        });

    }

    @Override
    protected ContentHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected void onBindHeaderViewHolder(ContentHolder holder, int position) {

    }

    @Override
    protected boolean hasHeader() {
        return false;
    }

    static class ContentHolder extends RecyclerView.ViewHolder {

        private CardView layout;
        private TextView title;
        private ImageView image;

        public ContentHolder(View itemView) {
            super(itemView);
            layout = (CardView) itemView.findViewById(R.id.daily_card);
            title = (TextView) itemView.findViewById(R.id.daily_tv_title);
            image = (ImageView) itemView.findViewById(R.id.daily_iv_image);
        }
    }
}
