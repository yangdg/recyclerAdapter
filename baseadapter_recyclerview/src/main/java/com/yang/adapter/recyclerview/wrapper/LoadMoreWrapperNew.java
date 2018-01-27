package com.yang.adapter.recyclerview.wrapper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yang.adapter.recyclerview.utils.WrapperUtils;
import com.yang.baseadapter_recyclerview.R;


/**
 * Created by Administrator on 2018/1/5.
 * 加载更多,1.解决类LoadMoreWrapper不满屏时自动加载更多
 *         2.加入多种加载状态
 */

public class LoadMoreWrapperNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "LoadMoreWrapperNew";
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;
    public static final int STATE_NO_MORE = 1;
    public static final int STATE_LOAD_MORE = 2;
    public static final int STATE_INVALID_NETWORK = 3;
    public static final int STATE_HIDE = 5;
    public static final int STATE_REFRESHING = 6;
    public static final int STATE_LOAD_ERROR = 7;
    public static final int STATE_LOADING = 8;
    protected int mState = STATE_HIDE;

    private boolean mIsOnLoading = false;

    private boolean mCanLoadMore = true;

    private boolean mHasMore = true;

    private RecyclerView.Adapter mInnerAdapter;
    private LoadMoreScrollListener mLoadMoreScrollListener;
    private boolean isHaveStatesView = true;

    public LoadMoreWrapperNew(RecyclerView.Adapter adapter) {
        this.mInnerAdapter = adapter;
        //创建滑动到底部的加载监听，在onAttachedToRecyclerView里给RecyclerView添加上
        mLoadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void loadMore() {
                if (mOnLoadListener != null&&mState!=STATE_NO_MORE) {
                    showLoadMore();
                    Log.d(TAG, "loadMore");
                    mOnLoadListener.onLoadMore();
                }
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && isHaveStatesView) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_footer_view, parent,
                    false);
            return new FooterViewHolder(itemView);
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ITEM_TYPE_LOAD_MORE: {
                FooterViewHolder fvh = (FooterViewHolder) holder;
                fvh.itemView.setVisibility(View.VISIBLE);
                Log.d(TAG,"mState="+mState);
                switch (mState) {
                    case STATE_INVALID_NETWORK:
                        fvh.tv_footer.setText("网络错误");
                        fvh.pb_footer.setVisibility(View.GONE);
                        break;
                    case STATE_LOAD_MORE:
                    case STATE_LOADING:
                        fvh.tv_footer.setText("正在加载...");
                        fvh.pb_footer.setVisibility(View.VISIBLE);
                        break;
                    case STATE_NO_MORE:
                        fvh.tv_footer.setText("没有更多数据");
                        fvh.pb_footer.setVisibility(View.GONE);
                        break;
                    case STATE_REFRESHING:
                        fvh.tv_footer.setText("正在刷新");
                        fvh.pb_footer.setVisibility(View.GONE);
                        break;
                    case STATE_LOAD_ERROR:
                        fvh.tv_footer.setText("加载失败");
                        fvh.pb_footer.setVisibility(View.GONE);
                        break;
                    case STATE_HIDE:
                        fvh.itemView.setVisibility(View.GONE);
                        break;
                }
                break;
            }
            default:
                mInnerAdapter.onBindViewHolder(holder, position);
                break;
        }
    }
    /**设置加载状态 如没有更多等*/
    public void setState(int mState) {
        Log.d(TAG, "setState mState=" + mState + " getItemCount()=" + getItemCount());
        this.mState = mState;
        notifyItemChanged(getItemCount()-1);
    }

    public void showLoadMore() {
        setState(STATE_LOADING);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (position == getItemCount() - 1 && isHaveStatesView) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });


        recyclerView.addOnScrollListener(mLoadMoreScrollListener);
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        if (holder.getLayoutPosition() == getItemCount() - 1 && isHaveStatesView) {
            //针对StaggeredGridLayoutManager的设置，让加载状态的View独占一行
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + (isHaveStatesView ? 1 : 0);
    }

    public interface OnLoadListener {
        void onLoadMore();
    }

    private OnLoadListener mOnLoadListener;

    public LoadMoreWrapperNew setOnLoadListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
        return this;
    }
    /**加载更多Holder*/
    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pb_footer;
        public TextView tv_footer;

        public FooterViewHolder(View view) {
            super(view);
            pb_footer = (ProgressBar) view.findViewById(R.id.pb_footer);
            tv_footer = (TextView) view.findViewById(R.id.tv_footer);
        }
    }
}