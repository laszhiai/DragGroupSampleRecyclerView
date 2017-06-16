package com.li.jacky.mylibrary.model;

import android.text.SpannableStringBuilder;
import com.li.jacky.mylibrary.helper.IFBISettingItem;

/**
 * Created by Jacky on 2017/6/6.
 */

public class IFBISettingTitleModel implements IFBISettingItem {
    private String mView;
    private String mName;
    private boolean mIsTarget;
    private SpannableStringBuilder mTitleText;

    public IFBISettingTitleModel(String view) {
        mView = view;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getView() {
        return mView;
    }

    public void setView(String view) {
        mView = view;
    }

    @Override
    public boolean isDimension() {
        return false;
    }

    public boolean isTarget() {
        return mIsTarget;
    }

    public void setTarget(boolean target) {
        mIsTarget = target;
    }

    public SpannableStringBuilder getTitleText() {
        return mTitleText;
    }

    public void setTitleText(SpannableStringBuilder titleText) {
        mTitleText = titleText;
    }
}
