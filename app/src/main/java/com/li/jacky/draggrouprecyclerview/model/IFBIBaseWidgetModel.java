package com.li.jacky.draggrouprecyclerview.model;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.li.jacky.draggrouprecyclerview.Constant.REGION;
import com.li.jacky.draggrouprecyclerview.model.dimension.IFBIDimensionGroup;
import com.li.jacky.draggrouprecyclerview.model.dimension.IFBIDimensionModel;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 基础的BI控件参数模型类,还有些不合理的地方,比如有些是专门针对表格的一些属性不该在这里解析,应定义在子类
 * Created by neo on 15/9/7.
 * Update by bob
 */
public class IFBIBaseWidgetModel {

    private JSONObject mRawData;//原始数据
    //以下五个维度都是有序的
    private IFBIDimensionGroup mDimension1;
    private IFBIDimensionGroup mDimension2;
    private IFBIDimensionGroup mTarget1;
    private IFBIDimensionGroup mTarget2;
    private IFBIDimensionGroup mTarget3;
    //所有维度的总列表可以无序
    private Map<String, IFBIDimensionModel> mAllDimensions;


    public IFBIBaseWidgetModel(JSONObject options) {
        parse(options);
    }

    public void parse(@NonNull JSONObject config) {
        mRawData = config;
        parseDimensions(config.optJSONObject("view"), config.optJSONObject("dimensions"));
    }

    @NonNull
    public JSONObject toJSON() {
        //暂时在原来的options基础上修改,否则可能一些未确定的参数会被遗漏
        JSONObject object = mRawData == null ? new JSONObject() : mRawData;
        try {
            JSONObject dimensions = new JSONObject();
            for (IFBIDimensionModel dimensionModel : mAllDimensions.values()) {
                if (dimensionModel != null) {
                    dimensions.put(dimensionModel.getID(), dimensionModel.toJSON());
                }
            }
            object.put("dimensions", dimensions);
            object.put("view", createViewObject());
        } catch (JSONException e) {
        }

        return object;
    }

    private JSONObject createViewObject() throws JSONException {
        JSONObject view = new JSONObject();
        putToView(view, REGION.DIMENSION1, mDimension1.toViewArray());
        putToView(view, REGION.DIMENSION2, mDimension2.toViewArray());
        putToView(view, REGION.TARGET1, mTarget1.toViewArray());
        putToView(view, REGION.TARGET2, mTarget2.toViewArray());
        putToView(view, REGION.TARGET3, mTarget3.toViewArray());
        return view;
    }

    private void putToView(JSONObject view, String id, @Nullable JSONArray array) throws JSONException {
        if (array == null) {
            return;
        }
        view.put(id, array);
    }

    private void parseDimensions(JSONObject view, JSONObject dimensions) {
        mDimension1 = new IFBIDimensionGroup();
        mDimension2 = new IFBIDimensionGroup();
        mTarget1 = new IFBIDimensionGroup();
        mTarget2 = new IFBIDimensionGroup();
        mTarget3 = new IFBIDimensionGroup();
        mAllDimensions = new HashMap<>();
        if (dimensions == null || dimensions.length() == 0 || view == null || view.length() == 0) {
            return;
        }
        Iterator<String> keys = dimensions.keys();
        while (keys.hasNext()) {
            String id = keys.next();
            JSONObject object = dimensions.optJSONObject(id);
            IFBIDimensionModel dimensionModel = new IFBIDimensionModel(id, object);
            mAllDimensions.put(id, dimensionModel);
        }

        parseDimensionsToList(view.optJSONArray(REGION.DIMENSION1), mDimension1);
        parseDimensionsToList(view.optJSONArray(REGION.DIMENSION2), mDimension2);
        parseDimensionsToList(view.optJSONArray(REGION.TARGET1), mTarget1);
        parseDimensionsToList(view.optJSONArray(REGION.TARGET2), mTarget2);
        parseDimensionsToList(view.optJSONArray(REGION.TARGET3), mTarget3);

    }

    private void parseDimensionsToList(@Nullable JSONArray viewArray, @NonNull IFBIDimensionGroup group) {
        if (viewArray == null) {
            return;
        }
        List<String> list = new ArrayList<>();
        for (int i = 0, n = viewArray.length(); i < n; i++) {
            String id = viewArray.optString(i);
            IFBIDimensionModel dimen = mAllDimensions.get(id);
            if (dimen == null) {
                continue;
            }
            list.add(id);
            group.add(dimen);
        }
        group.setOriginalIDList(list);
    }

    /**
     * 获取维度1,即view 10000对应的维度
     */
    public IFBIDimensionGroup getDimension1() {
        return mDimension1;
    }

    /**
     * 获取维度2,即view 20000对应的维度
     */
    public IFBIDimensionGroup getDimension2() {
        return mDimension2;
    }

    /**
     * 获取指标1,即view 30000对应的维度
     */
    public IFBIDimensionGroup getTarget1() {
        return mTarget1;
    }

    /**
     * 获取指标2,即view 40000对应的维度
     */
    public IFBIDimensionGroup getTarget2() {
        return mTarget2;
    }

    /**
     * 获取指标3,即view 40000对应的维度
     */
    public IFBIDimensionGroup getTarget3() {
        return mTarget3;
    }

    /**
     * 获取包含所有维度的映射表,
     * 键为维度id
     */
    public Map<String, IFBIDimensionModel> getAllDimensionMap() {
        return mAllDimensions;
    }


    @Override
    public String toString() {
        return toJSON().toString();
    }

}
