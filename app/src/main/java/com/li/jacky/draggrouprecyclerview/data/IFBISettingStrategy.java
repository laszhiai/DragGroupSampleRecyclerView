package com.li.jacky.draggrouprecyclerview.data;


import static com.li.jacky.draggrouprecyclerview.Constant.TEXT_COLOR_BLUE;

import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import com.li.jacky.draggrouprecyclerview.Constant.REGION;
import com.li.jacky.draggrouprecyclerview.helper.IFBISettingItem;
import com.li.jacky.draggrouprecyclerview.model.IFBIBaseWidgetModel;
import com.li.jacky.draggrouprecyclerview.model.IFBISettingItemModel;
import com.li.jacky.draggrouprecyclerview.model.IFBISettingRegion;
import com.li.jacky.draggrouprecyclerview.model.IFBISettingTitleModel;
import com.li.jacky.draggrouprecyclerview.model.dimension.IFBIDimensionGroup;
import com.li.jacky.draggrouprecyclerview.model.dimension.IFBIDimensionModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jacky on 2017/5/31.
 *  图表类型对应的策略，多对一
 */

public abstract class IFBISettingStrategy {

    private List<IFBISettingItem> mData = new ArrayList<>();//record position
    private List<IFBISettingRegion> mRegions = new ArrayList<>();
    private int division;
    private IFBIBaseWidgetModel mWidgetModel;
    IFBISettingRegion region1;
    IFBISettingRegion region2;
    IFBISettingRegion region3;
    IFBISettingRegion region4;
    IFBISettingRegion region5;

    public IFBISettingStrategy(IFBIBaseWidgetModel widgetModel) {
        mWidgetModel = widgetModel;
        initRegions();
        initData();
    }

    private void initRegions() {
        IFBIDimensionGroup dimension1 = mWidgetModel.getDimension1();
        IFBIDimensionGroup dimension2 = mWidgetModel.getDimension2();
        IFBIDimensionGroup target1 = mWidgetModel.getTarget1();
        IFBIDimensionGroup target2 = mWidgetModel.getTarget2();
        IFBIDimensionGroup target3 = mWidgetModel.getTarget3();

        region1 = new IFBISettingRegion(REGION.DIMENSION1);
        region2 = new IFBISettingRegion(REGION.DIMENSION2);
        region3 = new IFBISettingRegion(REGION.TARGET1);
        region4 = new IFBISettingRegion(REGION.TARGET2);
        region5 = new IFBISettingRegion(REGION.TARGET3);

        for (IFBIDimensionModel item : dimension1.values()) {
            IFBISettingItemModel model = new IFBISettingItemModel(item, true, false);
            region1.add(model);
        }

        for (IFBIDimensionModel item : dimension2.values()) {
            IFBISettingItemModel model = new IFBISettingItemModel(item, true, false);
            region2.add(model);
        }

        for (IFBIDimensionModel item : target1.values()) {
            IFBISettingItemModel model = new IFBISettingItemModel(item, true, true);
            region3.add(model);
        }

        for (IFBIDimensionModel item : target2.values()) {
            IFBISettingItemModel model = new IFBISettingItemModel(item, true, true);
            region4.add(model);
        }

        for (IFBIDimensionModel item : target3.values()) {
            IFBISettingItemModel model = new IFBISettingItemModel(item, true, true);
            region5.add(model);
        }

        mRegions.add(region1);
        mRegions.add(region2);
        mRegions.add(region3);
        mRegions.add(region4);
        mRegions.add(region5);
    }

    private void initData() {
        mData.clear();

        addDimension1Title();
        if (region1.isExpand()) {
            for (IFBISettingItemModel item : region1.values()) {
                mData.add(item);
            }
        }

        addDimension2Title();
        if (region2.isExpand()) {
            for (IFBISettingItemModel item : region2.values()) {
                mData.add(item);
            }
        }

        division = mData.size();
        addTarget1Title();
        if (region3.isExpand()) {
            for (IFBISettingItemModel item : region3.values()) {
                mData.add(item);
            }
        }

        addTarget2Title();
        if (region4.isExpand()) {
            for (IFBISettingItemModel item : region4.values()) {
                mData.add(item);
            }
        }

        addTarget3Title();
        if (region5.isExpand()) {
            for (IFBISettingItemModel item : region5.values()) {
                mData.add(item);
            }
        }
    }

    protected void addTarget3Title() {
    }

    protected void addTarget2Title() {
    }

    protected void addTarget1Title() {
    }

    protected void addDimension2Title() {
    }

    protected void addDimension1Title(){
    }

    protected void addTitleItem(String view, String name) {
        IFBISettingRegion region = getRegion(view);
        if (region == null) {
            return;
        }
        IFBISettingTitleModel model;
        if (region.getTitleModel() == null) {
            model = new IFBISettingTitleModel(view);
            model.setName(name);
            model.setTitleText(new SpannableStringBuilder(name));
            switch (view) {
                case REGION.DIMENSION1:
                    model.setTarget(false);
                    region1.setTitleModel(model);
                    break;
                case REGION.DIMENSION2:
                    model.setTarget(false);
                    region2.setTitleModel(model);
                    break;
                case REGION.TARGET1:
                    model.setTarget(true);
                    region3.setTitleModel(model);
                    break;
                case REGION.TARGET2:
                    model.setTarget(true);
                    region4.setTitleModel(model);
                    break;
                case REGION.TARGET3:
                    model.setTarget(true);
                    region5.setTitleModel(model);
                    break;
            }
        }   else {
            model = region.getTitleModel();
        }
        mData.add(model);
    }


    public IFBISettingItem getItem(int position) {
        return mData.get(position);
    }

    public int getSize(){
        return mData.size();
    }

    /**
     * 选择维度
     * @param position 位置
     */
    public void onSelectItem(int position) {
        IFBISettingItemModel item = (IFBISettingItemModel) mData.get(position);
        IFBISettingRegion region = getRegion(item);
        if (region == null) {
            return;
        }
        String sectionName = region.getRegion();
        switch (sectionName) {
            case REGION.DIMENSION1:
                onClickXDimension(item);
                break;
            case REGION.DIMENSION2:
                onClickYDimension(item);
                break;
            //指标
            default:
                onClickTargets(item);
                break;
        }
    }

    public void onSelectTitle(int position) {
        IFBISettingTitleModel item = (IFBISettingTitleModel) mData.get(position);
        IFBISettingRegion region = getRegion(item);
        if (region != null){
            region.setExpand(!region.isExpand());
            int count = region.values().size();
            if (!region.isExpand() || count == 0) {
                item.setTitleText(new SpannableStringBuilder(item.getName()));
            }   else {
                String title = item.getName() + "(" + count + ")";
                int start = title.indexOf("(") + 1;
                int end = title.indexOf(")");
                SpannableStringBuilder style = new SpannableStringBuilder(title);
                style.setSpan(new ForegroundColorSpan(TEXT_COLOR_BLUE), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                item.setTitleText(style);
            }
        }
        initData();
    }

    protected abstract void onClickXDimension(IFBISettingItemModel item);

    protected void onClickYDimension(IFBISettingItemModel item){}

    protected abstract void onClickTargets(IFBISettingItemModel item);

    /**
     * 分类与系列的可选性时候被指标影响，各自实现
     */
    protected void checkTargetRelate(){}

    protected void multiSelect(IFBISettingItemModel item) {
        item.setUsed(!item.isUsed());
    }

    /**
     * 此项为多选
     * 并受到指标选中数影响其是否可选
     */
    protected void multiSelectWithCheck(IFBISettingItemModel item) {
        item.setUsed(!item.isUsed());
        checkTargetRelate();
    }

    /**
     * 点击时，此项受到指标的影响，指标选中数小于2时，可选
     * 此项为单选
     */
    protected void targetRelated(IFBISettingItemModel item) {
        if (getTargetSelectedCount() < 2) {
            singleSelect(item);
        }
    }


    protected void singleSelect(IFBISettingItemModel selectItem) {
        IFBISettingRegion region = getRegion(selectItem);
        if (region != null){
            boolean selected = !selectItem.isUsed();
            region.clearSelected();
            selectItem.setUsed(selected);
        }
    }

    protected int getTargetSelectedCount() {
        int count = 0;
        for (IFBISettingRegion region : mRegions) {
            if (region.isTarget())
                count += region.getSelectedCount();
        }
        return count;
    }

    protected int getCategorySelectedCount() {
        IFBISettingRegion region = mRegions.get(0);
        if (region != null) {
            return region.getSelectedCount();
        }
        return 0;
    }

    protected int getSeriesSelectedCount() {
        IFBISettingRegion region = mRegions.get(1);
        if (region != null) {
            return region.getSelectedCount();
        }
        return 0;
    }

    /**
     * 拖拽维度
     */
    public void onItemMove(int fromPosition, int toPosition) {
        IFBISettingItemModel fromModel = (IFBISettingItemModel) mData.get(fromPosition);
        IFBISettingItem ifbiSettingItem = mData.get(toPosition);
        Collections.swap(mData, fromPosition, toPosition);
        if (ifbiSettingItem instanceof IFBISettingTitleModel) {
            int view = Integer.parseInt(((IFBISettingTitleModel)ifbiSettingItem).getView());
            boolean up = fromPosition > toPosition;
            refreshRegion(fromModel, view, up, (IFBISettingTitleModel)ifbiSettingItem);
            switch (view) {
                case 20000:
                    if (up) {
                        onMoveToXDimension(fromModel);
                    }else {
                        onMoveToYDimension(fromModel);
                    }
                    break;
                case 40000:
                    if (up) {
                        onMoveToXTarget(fromModel);
                    }else {
                        onMoveToYTarget(fromModel);
                    }
                    break;
                case 50000:
                    if (up) {
                        onMoveToYTarget(fromModel);
                    }else {
                        onMoveToZTarget(fromModel);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void refreshRegion(IFBISettingItemModel fromModel, int view, boolean up, IFBISettingTitleModel titleModel) {
        int i = view / 10000;
        if (up) {
            mRegions.get(i - 1).remove(fromModel);
            IFBISettingRegion target = mRegions.get(i - 2);
            target.add(fromModel);
            if (!target.isExpand()) {
                target.setExpand(true);
                titleModel.setTitleText(new SpannableStringBuilder(titleModel.getName()));
                initData();
            }
        } else {
            mRegions.get(i - 2).remove(fromModel);
            IFBISettingRegion target = mRegions.get(i - 1);
            target.add(fromModel);
            if (!target.isExpand()) {
                target.setExpand(true);
                titleModel.setTitleText(new SpannableStringBuilder(titleModel.getName()));
                initData();
            }
        }
    }

    protected void onMoveToZTarget(IFBISettingItemModel fromModel){}

    protected void onMoveToYTarget(IFBISettingItemModel fromModel){}

    protected void onMoveToXTarget(IFBISettingItemModel fromModel){}

    protected void onMoveToYDimension(IFBISettingItemModel fromModel){}

    protected void onMoveToXDimension(IFBISettingItemModel fromModel){}

    /**
     * @param model item
     * @return model所在的region
     */
    @Nullable
    protected IFBISettingRegion getRegion(IFBISettingItemModel model){
        for (IFBISettingRegion region : mRegions){
            if (region.has(model))
                return region;
        }
        return null;
    }

    @Nullable
    protected IFBISettingRegion getRegion(IFBISettingTitleModel title){
        for (IFBISettingRegion region : mRegions){
            if (region.getTitleModel() == title)
                return region;
        }
        return null;
    }

    @Nullable
    protected IFBISettingRegion getRegion(String view){
        for (IFBISettingRegion region : mRegions){
            if (region.getRegion().equals(view))
                return region;
        }
        return null;
    }

    /**
     * 所在region为单选
     * @param fromModel
     */
    protected void checkSingleSelect(IFBISettingItemModel fromModel) {
        if (fromModel.isUsed()) {
            IFBISettingRegion region = getRegion(fromModel);
            if (region != null && region.getSelectedCount() > 1)
                fromModel.setUsed(false);
        }
    }

    public boolean canMove(int source, int target) {
        if (target == 0 || target == division) {
            return false;
        }
        IFBISettingItem start =  mData.get(source);
        IFBISettingItem end = mData.get(target);
        //指标与维度不可跨越
        return start.isTarget() && end.isTarget() || !start.isTarget() && !end.isTarget();
    }

    /**
     * 这个方法适配IFBISortActivity传入的数据，
     * 与业务强耦合
     */
    public JSONArray getTargets(IFBISettingItemModel model) {
        JSONArray result = new JSONArray();
        try {
            JSONObject json = new JSONObject();
            json.put("key", model.getID());
            json.put("name", model.getName());
            result.put(json);
            for (IFBISettingRegion region : mRegions) {
                for (IFBISettingItemModel item : region.values()) {
                    if (item.isTarget()) {
                        json = new JSONObject();
                        json.put("key", item.getID());
                        json.put("name", item.getName());
                        result.put(json);
                    }
                }
            }
        }  catch (JSONException e) {
        }
        return result;
    }
}
