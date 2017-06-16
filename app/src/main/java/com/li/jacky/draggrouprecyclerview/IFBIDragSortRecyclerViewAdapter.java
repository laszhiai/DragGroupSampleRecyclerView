package com.li.jacky.draggrouprecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.li.jacky.draggrouprecyclerview.data.IFBISettingStrategy;
import com.li.jacky.draggrouprecyclerview.helper.IFBIItemTouchHelperAdapter;
import com.li.jacky.draggrouprecyclerview.helper.IFBISettingItem;
import com.li.jacky.draggrouprecyclerview.model.IFBISettingItemModel;
import com.li.jacky.draggrouprecyclerview.model.IFBISettingTitleModel;

/**
 * Created by Jacky on 2017/5/25.
 *
 */

public class IFBIDragSortRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> implements IFBIItemTouchHelperAdapter {

    private Context mContext;
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_ITEM = 1;
    private IFBISettingStrategy mData;

    public IFBIDragSortRecyclerViewAdapter(@NonNull Context context ,IFBISettingStrategy data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){
            final View item = LayoutInflater.from(mContext).inflate(R.layout.fr_bi_setting_item, parent, false);
            final ItemViewHolder itemViewHolder = new ItemViewHolder(item);
            itemViewHolder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.onSelectItem(itemViewHolder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
            return itemViewHolder;
        }else {
            View sectionHead = LayoutInflater.from(mContext).inflate(R.layout.fr_bi_setting_section_head, parent, false);
            final TitleViewHolder titleViewHolder = new TitleViewHolder(sectionHead);
            sectionHead.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.onSelectTitle(titleViewHolder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
            return titleViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            IFBISettingItemModel item = (IFBISettingItemModel) mData.getItem(position);
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.title.setText(item.getName());
            itemViewHolder.selectIcon.setVisibility(item.isUsed() ? View.VISIBLE : View.INVISIBLE);
        } else {
            IFBISettingTitleModel item = (IFBISettingTitleModel) mData.getItem(position);
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.sectionHead.setText(item.getTitleText());
            titleViewHolder.itemView.setBackgroundColor(item.isTarget() ? Constant.BACKGROUND_COLOR_FIELD_TARGET : Constant.BACKGROUND_COLOR_FIELD_DIMENSION);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.getSize();
    }

    @Override
    public int getItemViewType(int position) {
        IFBISettingItem item = mData.getItem(position);
        return item.isDimension() ? TYPE_ITEM :TYPE_TITLE;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        mData.onItemMove(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public boolean canMove(int source, int target) {
        if (getItemViewType(source) == TYPE_TITLE) {
            return false;
        }
        return mData.canMove(source, target);
    }

    public static class ItemViewHolder extends ViewHolder {

        TextView title;
        ImageView selectIcon;

        public ItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.fr_bi_setting_item_title);
            selectIcon = (ImageView) itemView.findViewById(R.id.fr_bi_setting_selected_icon);
        }
    }

    public static class TitleViewHolder extends ViewHolder {

        TextView sectionHead;

        public TitleViewHolder(View itemView) {
            super(itemView);
            sectionHead = (TextView) itemView.findViewById(R.id.fr_bi_setting_section_head);

        }
    }
}
