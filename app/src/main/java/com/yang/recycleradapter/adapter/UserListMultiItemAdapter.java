package com.yang.recycleradapter.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.yang.adapter.recyclerview.MultiItemTypeAdapter;
import com.yang.adapter.recyclerview.base.ItemViewDelegate;
import com.yang.adapter.recyclerview.base.ViewHolder;
import com.yang.recycleradapter.R;
import com.yang.recycleradapter.bean.UserEntity;

import java.util.List;


/**
 * Created by Administrator on 2017/9/6.
 */

public class UserListMultiItemAdapter extends MultiItemTypeAdapter<UserEntity> {
    private static String TAG = "UserListMultiItemAdapter";
    public UserListMultiItemAdapter(Context context, List<UserEntity> datas) {
        super(context, datas);
        addItemViewDelegate(new TitleItem());
        addItemViewDelegate(new NormalItem());
    }

    /**内容item*/
    private static class NormalItem implements ItemViewDelegate<UserEntity> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.chatgroup_item_layout;
        }

        @Override
        public boolean isForViewType(UserEntity item, int position) {
            if(!TextUtils.isEmpty(item.getId())){
                return true;
            }
            return false;
        }

        @Override
        public void convert(ViewHolder holder, UserEntity item, int position) {
            Log.d(TAG, "position=" + position);
            holder.setText(R.id.group_name, item.getName());
            ImageView imageView=holder.getView(R.id.group_image);//使用加载框架加载图像即可
            if(item.isHasUnderline()){
                holder.setVisible(R.id.item_divider_line,true);
            }else{
                holder.setVisible(R.id.item_divider_line,false);
            }
        }
    }
    /**标题title item*/
    private static class TitleItem implements ItemViewDelegate<UserEntity> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.chatgroup_title_item_layout;
        }

        @Override
        public boolean isForViewType(UserEntity item, int position) {
            if(TextUtils.isEmpty(item.getId())){
                return true;
            }
            return false;
        }

        @Override
        public void convert(ViewHolder holder, UserEntity item, int position) {
            holder.setText(R.id.group_title, item.getName());
        }
    }
}
