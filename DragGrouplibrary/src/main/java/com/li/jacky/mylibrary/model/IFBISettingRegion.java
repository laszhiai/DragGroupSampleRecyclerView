package com.li.jacky.mylibrary.model;


import static com.li.jacky.mylibrary.Constant.X_DIMENSION;
import static com.li.jacky.mylibrary.Constant.X_TARGETS;
import static com.li.jacky.mylibrary.Constant.Y_DIMENSION;
import static com.li.jacky.mylibrary.Constant.Y_TARGETS;
import static com.li.jacky.mylibrary.Constant.Z_TARGETS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2017/6/2.
 *
 */

public class IFBISettingRegion {

    private String mRegion;
    private List<IFBISettingItemModel> mItems = new ArrayList<>();
    private IFBISettingTitleModel mTitleModel;
    private boolean mIsExpand = true;

    public IFBISettingRegion(String region) {
        this.mRegion = region;
    }

    public boolean add(IFBISettingItemModel item){
        return mItems.add(item);
    }

    public boolean remove(IFBISettingItemModel item){
        return mItems.remove(item);
    }

    public String getRegion() {
        return mRegion;
    }

    public boolean isTarget(){
        return mRegion.equals(X_TARGETS) || mRegion.equals(Y_TARGETS)|| mRegion.equals(Z_TARGETS);
    }

    public boolean isCategory(){
        return mRegion.equals(X_DIMENSION);
    }

    public boolean isSeries(){
        return mRegion.equals(Y_DIMENSION);
    }

    public  int getSelectedCount(){
        int count = 0;
        for (IFBISettingItemModel item : mItems) {
            if (item.isUsed())
                count++;
        }
        return count;
    }

    public void clearSelected(){
        for (IFBISettingItemModel item : mItems) {
            if (item.isUsed())
                item.setUsed(false);
        }
    }

    public boolean has(IFBISettingItemModel model) {
        for (IFBISettingItemModel item : mItems) {
            if (model == item)
                return true;
        }
        return false;
    }

    public List<IFBISettingItemModel> values(){
        return mItems;
    }

    public boolean isExpand() {
        return mIsExpand;
    }

    public void setExpand(boolean expand) {
        mIsExpand = expand;
    }

    public IFBISettingTitleModel getTitleModel() {
        return mTitleModel;
    }

    public void setTitleModel(IFBISettingTitleModel titleModel) {
        mTitleModel = titleModel;
    }

}
