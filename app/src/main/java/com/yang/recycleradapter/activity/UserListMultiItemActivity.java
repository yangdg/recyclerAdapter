package com.yang.recycleradapter.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yang.adapter.recyclerview.MultiItemTypeAdapter;
import com.yang.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.yang.recycleradapter.adapter.UserListMultiItemAdapter;
import com.yang.recycleradapter.bean.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 * 列表多布局示例
 */

public class UserListMultiItemActivity extends BaseRecyclerViewActivity<UserEntity> {
    private static String TAG = "UserListMultiItemActivity";
    private int count=0;
    public static void launch(Context context) {
        Log.d(TAG,"launch");
        Intent intent = new Intent(context, UserListMultiItemActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initDefaultToolBar() {

    }


    @Override
    public MultiItemTypeAdapter.OnItemClickListener initClickListener() {
        //返回条目点击事件
        return new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Log.d(TAG, "position=" + position);
                UserEntity chatgroupsEntity = getItem(position);
                if (chatgroupsEntity != null) {
                    Log.d(TAG, " OnItemClickListener chatgroupsEntity=" + chatgroupsEntity.toString());
                    Toast.makeText(UserListMultiItemActivity.this,"chatgroupsEntity="+chatgroupsEntity.toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        };
    }

    @Override
    public MultiItemTypeAdapter<UserEntity> onCreateAdapter(List<UserEntity> data) {
        //返回adapter
        return new UserListMultiItemAdapter(this, data);
    }

    @Override
    public void getData(int page) {
        getMsgList(page);
    }

    @Override
    protected HeaderAndFooterWrapper getHeaderWrapper(RecyclerView.Adapter adapter) {
        //不需要头尾部
        return null;
    }

    /**
     * 测试数据
     */
    private void testData(int page) {
        if(page==1){
            count=0;
        }else{
            count++;
        }
        if(count>=3){
            upDataListview(null);
        }else{
            List<UserEntity> entities = new ArrayList<>();
            entities.add(new UserEntity("", "老板"));
            entities.add(new UserEntity("1", "刘xx",false));
            entities.add(new UserEntity("", "经理"));
            entities.add(new UserEntity("1", "赵xx"));
            entities.add(new UserEntity("1", "王xx"));
            entities.add(new UserEntity("1", "李xx",false));
            entities.add(new UserEntity("", "员工"));
            entities.add(new UserEntity("1", "员工1"));
            entities.add(new UserEntity("1", "员工2"));
            entities.add(new UserEntity("1", "员工3"));
            entities.add(new UserEntity("1", "员工4"));
            entities.add(new UserEntity("1", "员工5"));
            upDataListview(entities);
        }

    }

    private void getMsgList(final int page) {
        //模拟数据获取
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                testData(page);
            }
        }, 1000);

    }


}
