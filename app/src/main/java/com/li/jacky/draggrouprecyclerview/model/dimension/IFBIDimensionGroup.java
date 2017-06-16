package com.li.jacky.draggrouprecyclerview.model.dimension;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 备注:
 * 维度的分组
 * @author bob.huang
 * @version 1.0
 * @since 16/12/23
 */

public class IFBIDimensionGroup {
    /**
     * 原始的id列表,这个列表是不可以改变的,是维度在模版中真实的维度分组,
     * 在类似钻取等情况下会有分类维度和系列维度互相转移的情况,但要保证能还原回其原始属性
     */
    private List<String> mOriginalIDList;

    private List<IFBIDimensionModel> mDimensions = new ArrayList<>();

    /**
     * 设置此分组的原始ID集合,也就是options中view字段的id集合,
     * 此方法应该只在初始化是被调用,后面不应再修改这个id列表
     */
    public void setOriginalIDList(@NonNull List<String> idList){
        mOriginalIDList = idList;
    }

    public void add(@NonNull IFBIDimensionModel dimensionModel){
        mDimensions.add(dimensionModel);
    }

    public void add(int i,@NonNull IFBIDimensionModel dimen){
        if (i < 0 || i >= mDimensions.size()){
            return;
        }
        mDimensions.add(i,dimen);
    }

    public boolean isEmpty(){
        return mDimensions.isEmpty();
    }

    public IFBIDimensionModel remove(@NonNull String id){
        for (IFBIDimensionModel dimen : mDimensions){
            if (dimen.getID().equals(id)){
                mDimensions.remove(dimen);
                return dimen;
            }
        }
        return null;
    }

    public IFBIDimensionModel remove(@NonNull IFBIDimensionModel dimension){
        return remove(dimension.getID());
    }

    public IFBIDimensionModel find(String id){
        if (id.length() == 0){
            return null;
        }
        for (IFBIDimensionModel dimen : mDimensions){
            if (dimen.getID().equals(id)){
                return dimen;
            }
        }
        return null;
    }

    public List<String> keys(){
        List<String> list = new ArrayList<>();
        for (IFBIDimensionModel dimen : mDimensions){
            list.add(dimen.getID());
        }
        return list;
    }

    public List<IFBIDimensionModel> values(){
        return mDimensions;
    }

    public IFBIDimensionModel get(int index){
        if (index < 0 || index >= mDimensions.size()){
            return null;
        }
        return mDimensions.get(index);
    }

    /**
     * 获得维度列表中第一个处于可见状态的维度,或者null
     */
    @Nullable
    public IFBIDimensionModel getFirstUsed(){
        for (IFBIDimensionModel dimen : mDimensions){
            if (dimen.isUsed()){
                return dimen;
            }
        }
        return null;
    }

    @Nullable
    public IFBIDimensionModel getLastUsed(){
        for(int i = mDimensions.size() - 1;i>=0;i--){
            IFBIDimensionModel dimen = mDimensions.get(i);
            if (dimen.isUsed()){
                return dimen;
            }
        }
        return null;
    }

    @NonNull
    public List<IFBIDimensionModel> getAllUsed(){
        List<IFBIDimensionModel> list = new ArrayList<>();
        for (IFBIDimensionModel dimen : mDimensions){
            if (dimen.isUsed()){
                list.add(dimen);
            }
        }
        return list;
    }

    /**
     * 判断一个维度在性质上是否是属于当前分组,比如某个维度原本是分类维度的,但由于钻取等原因会被移动到系列分组,
     * 和{@link #contains}方法是有区别的。
     * @return true:如果该维度在定义时属于此分组,和它当前所在分组无关,否则为false
     */
    public boolean isBelongTo(@NonNull IFBIDimensionModel dimen){
        return mOriginalIDList != null && mOriginalIDList.contains(dimen.getID());
    }

    /**
     * 判断目标维度当前是否在本分组中
     */
    public boolean contains(@NonNull IFBIDimensionModel dimension){
        return find(dimension.getID()) != null;
    }
    /**
     * 判断目标维度当前是否在本分组中
     */
    public boolean contains(String id){
        return find(id) != null;
    }

    /**
     * 导出view字段
     */
    public JSONArray toViewArray(){
        //mOriginalIDList == null表明服务器返回的就没有这个字段,不是[],
        // 反序列化时不能返回改分组对应字段,否则类似气泡图、散点图、对比柱状图等图表会出现错误
        if (mOriginalIDList == null && mDimensions.isEmpty()){
            return null;
        }
        JSONArray array = new JSONArray();
        for (IFBIDimensionModel dimen : mDimensions){
            array.put(dimen.getID());
        }
        return array;
    }

    /**
     * 导出dimension字段,只是一部分
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject dimensions = new JSONObject();
        for (IFBIDimensionModel dimen : mDimensions) {
            if (dimen != null) {
                dimensions.put(dimen.getID(), dimen.toJSON());
            }
        }
        return dimensions;
    }

    public void setDimensions(List<IFBIDimensionModel> mDimensions) {
        this.mDimensions = mDimensions;
    }
}
