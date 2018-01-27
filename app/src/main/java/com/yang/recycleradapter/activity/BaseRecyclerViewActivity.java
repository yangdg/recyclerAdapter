package com.yang.recycleradapter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import com.yang.adapter.recyclerview.MultiItemTypeAdapter;
import com.yang.adapter.recyclerview.wrapper.EmptyWrapper;
import com.yang.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.yang.adapter.recyclerview.wrapper.LoadMoreWrapperNew;
import com.yang.recycleradapter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 * 通用RecyclerView列表页面基类
 */

public abstract class BaseRecyclerViewActivity<T> extends AppCompatActivity {
    private static String TAG = "BaseRecyclerViewActivity";
    protected Context mContext;
    private SwipeRefreshLayout refreshLayout;
    protected RecyclerView recyclerView;
    private List<T> datalist = new ArrayList<>();
    private MultiItemTypeAdapter<T> mOriginalAdapter;
    private RecyclerView.Adapter mTargetAdapter;
    private LoadMoreWrapperNew loadMoreWrapper;
    private HeaderAndFooterWrapper headerAndFooterWrapper;

    protected boolean isRefreshing = false;//是否下拉刷新
    private int currentpageNo = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        mContext = this;
        setContentView(R.layout.activity_recyclerview_layout);
        initview();
        initData();
    }
    private void initview() {
        initToolBar();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        initListener();
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFristPage();
            }
        });
    }
    /**更新第一页数据*/
    public void refreshFristPage(){
        isRefreshing = true;
        currentpageNo = 2;
        getData(1);
    }

    /**
     * 设置导航栏
     */
    private void initToolBar() {
//        heanview = (HeanView) findViewById(R.id.head_back_action);
        initDefaultToolBar();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initRecyclerView();
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(true);
                    refreshFristPage();
                }
            }, 10);
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        refreshLayout.setColorSchemeResources(
                R.color.colorPrimary);
        initAdapter();
    }

    private void initAdapter() {
        //创建多布局adapter
        mOriginalAdapter = onCreateAdapter(datalist);
        mOriginalAdapter.setOnItemClickListener(initClickListener());
        //添加空布局
        EmptyWrapper mEmptyWrapper = getEmptyWrapper(mOriginalAdapter);
        mTargetAdapter = getTargetAdapter(mEmptyWrapper);
        recyclerView.setAdapter(mTargetAdapter);
    }

    /**
     * 获取目标adapter
     */
    private RecyclerView.Adapter getTargetAdapter(RecyclerView.Adapter adapter) {
        RecyclerView.Adapter targetadapter = adapter;
        headerAndFooterWrapper = getHeaderWrapper(adapter);
        if (headerAndFooterWrapper != null) {
            targetadapter = headerAndFooterWrapper;
            loadMoreWrapper = getLoadMoreWrapper(headerAndFooterWrapper);
        } else {
            loadMoreWrapper = getLoadMoreWrapper(adapter);
        }
        if (loadMoreWrapper != null) {
            targetadapter = loadMoreWrapper;
        }
        return targetadapter;
    }


    /**
     * 更新列表数据
     *
     * @param templist
     * @param pageTotal
     */
    public void upDataListview(List<T> templist, int pageTotal) {
        //是否加载更多
        Log.d(TAG, "isRefreshing=" + isRefreshing);
        if (isRefreshing) {
            //下拉刷新
            onRefreshData(templist);
        } else {
            //加载更多
            onLoadMoreData(templist, pageTotal);
        }
        if (mTargetAdapter != null) {
            mTargetAdapter.notifyDataSetChanged();
        }
        onComplete();
    }

    /**
     * 更新列表数据
     *
     * @param templist
     */
    public void upDataListview(List<T> templist) {
        upDataListview(templist, 0);
    }

    /**
     * 处理下拉刷新数据
     */
    private void onRefreshData(List<T> templist) {
        if (datalist != null) {
            datalist.clear();
            if (loadMoreWrapper != null) {
                loadMoreWrapper.setState(LoadMoreWrapperNew.STATE_HIDE);
            }
            if (!IsListIsEmpty(templist)) {
                datalist.addAll(templist);
            }
        }
    }

    /**
     * 处理加载更多数据
     */
    private void onLoadMoreData(List<T> templist, int pageTotal) {
        if (currentpageNo >= pageTotal && IsListIsEmpty(templist)) {
            //没有更多
            Log.d(TAG, "nomore currentpageNo=" + currentpageNo);
            if (loadMoreWrapper != null) {
                loadMoreWrapper.setState(LoadMoreWrapperNew.STATE_NO_MORE);
            }
            onComplete();
        } else {
            //还有更多
            if (datalist != null) {
                datalist.addAll(templist);
            }
            currentpageNo++;
            Log.d(TAG, "hasmore currentpageNo=" + currentpageNo);
        }

    }

    /**
     * 下拉加载完成
     */
    public void onComplete() {
        refreshLayout.setRefreshing(false);
        isRefreshing = false;
    }

    /**
     * 获取空布局包裹
     */
    protected EmptyWrapper getEmptyWrapper(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            return null;
        }
        EmptyWrapper mEmptyWrapper = new EmptyWrapper(adapter);
        View emptyview = LayoutInflater.from(this).inflate(R.layout.listview_emptyview_layout, recyclerView, false);
        mEmptyWrapper.setEmptyView(emptyview);
        return mEmptyWrapper;
    }

    /**
     * 获取头尾布局包裹
     */
    protected HeaderAndFooterWrapper getHeaderWrapper(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            return null;
        }
        HeaderAndFooterWrapper mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        View mHeaderView = LayoutInflater.from(this).inflate(R.layout.listview_headview_line_layout, recyclerView, false);
        View mFootView = LayoutInflater.from(this).inflate(R.layout.listview_headview_line_layout, recyclerView, false);
        mHeaderAndFooterWrapper.addHeaderView(mHeaderView);
        mHeaderAndFooterWrapper.addFootView(mFootView);
        return mHeaderAndFooterWrapper;
    }

    /**
     * 获取加载更多包裹
     */
    protected LoadMoreWrapperNew getLoadMoreWrapper(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            return null;
        }
        LoadMoreWrapperNew mLoadMoreWrapper = new LoadMoreWrapperNew(adapter);
        mLoadMoreWrapper.setOnLoadListener(new LoadMoreWrapperNew.OnLoadListener() {
            @Override
            public void onLoadMore() {
                if (!isRefreshing) {
                    recyclerView.post(new Runnable() {
                        public void run() {
                            Log.d(TAG, "onLoadMore currentpageNo~" + currentpageNo);
                            getData(currentpageNo);
                        }
                    });
                }
            }
        });
        return mLoadMoreWrapper;
    }

    /**
     * 获取条目
     */
    public T getItem(int position) {
        if (datalist == null || datalist.size() == 0) {
            return null;
        }
        int p = getIndex(position);
        if (p < 0 || p >= datalist.size()) {
            return null;
        }
        return datalist.get(getIndex(position));
    }

    /**
     * 获取真实的position
     */
    private int getIndex(int position) {
        if (headerAndFooterWrapper != null) {
            return position - headerAndFooterWrapper.getHeadersCount();
        }
        return position;
    }

    /**
     * 更新单条列表
     */
    public void updateItem(int position) {
        if (mTargetAdapter != null) {
            if (mTargetAdapter.getItemCount() > position) {
                mTargetAdapter.notifyItemChanged(position);
            }
        }
    }

    /**
     * 更新全部列表
     */
    public void updateAll() {
        if (mTargetAdapter != null) {
            mTargetAdapter.notifyDataSetChanged();
        }
    }
    //list判空
    public boolean IsListIsEmpty(List list){
        if(list==null||list.size()==0){
            return true;
        }
        return false;
    }

    /**
     * 设置导航栏
     */
    public abstract void initDefaultToolBar();

    /**
     * 获取条目点击事件
     */
    public abstract MultiItemTypeAdapter.OnItemClickListener initClickListener();

    /**
     * 获取具体的adapter
     */
    public abstract MultiItemTypeAdapter<T> onCreateAdapter(List<T> data);

    /**
     * 获取数据
     */
    public abstract void getData(int page);

}
