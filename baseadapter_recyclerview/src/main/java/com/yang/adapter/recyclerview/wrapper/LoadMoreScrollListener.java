package com.yang.adapter.recyclerview.wrapper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2018/1/5.
 * 用于监听列表滑动到底部
 */

public abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "LoadMoreScrollListener";
    private int lastVisibleItemPosition;
    private boolean isLoading = true;

    public abstract void loadMore();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    }
    /**
     * 获取RecyclerView可见的最后一项
     *
     * @return 可见的最后一项position
     */
    public int getLastVisiblePosition(RecyclerView mRecycleView) {
        int position;
        if (mRecycleView.getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) mRecycleView.getLayoutManager()).findLastVisibleItemPosition();
        } else if (mRecycleView.getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) mRecycleView.getLayoutManager()).findLastVisibleItemPosition();
        } else if (mRecycleView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mRecycleView.getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = mRecycleView.getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions 获得最大的位置
     * @return 获得最大的位置
     */
    private int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        lastVisibleItemPosition=getLastVisiblePosition(recyclerView);
        Log.d(TAG,"isLoading="+isLoading);
        if (isLoading) {
            //和之前数据的数目进行比较，判断是否加载完毕，重置加载状态
        }
        //RecyclerView滑动停止，若显示到最后的位置，则开始加载
        if (visibleItemCount > 0 && totalItemCount - 1 == lastVisibleItemPosition && newState == RecyclerView.SCROLL_STATE_IDLE&&isFull(recyclerView)) {
            Log.d(TAG,"loadMore");
            loadMore();
        }
    }
    /**检测是否满屏*/
    private boolean isFull(RecyclerView recyclerView){
        int childCount = recyclerView.getChildCount();
        View lastChildView = recyclerView.getChildAt(childCount - 1);
        View firstChildView = recyclerView.getChildAt(0);
        int top = firstChildView.getTop();
        int bottom = lastChildView.getBottom();
        //recycleView显示itemView的有效区域的bottom坐标Y
        int bottomEdge = recyclerView.getHeight() - recyclerView.getPaddingBottom();
        //recycleView显示itemView的有效区域的top坐标Y
        int topEdge = recyclerView.getPaddingTop();
        //第一个view的顶部小于top边界值,说明第一个view已经部分或者完全移出了界面
        //最后一个view的底部小于bottom边界值,说明最后一个view已经完全显示在界面
        //若满足这两个条件,说明所有子view已经填充满了recycleView,recycleView可以"真正地"滑动
        if (bottom <= bottomEdge && top < topEdge) {
            //满屏的recyceView
            return true;
        }
        return false;
    }
}