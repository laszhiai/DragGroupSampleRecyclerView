package com.li.jacky.draggrouprecyclerview.model.dimension;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * dimension的模型类
 * Created by slynero on 16-5-28.
 */
public class IFBIDimensionModel {

    private String mID;

    private Settings mSettings;//对应settings字段
    private String mName;//对应name字段
    private boolean isUsed;//对应used字段
    private int mDimensionType;//对应type字段
    private Src mSrc;//对应_src字段
    private JSONObject mDimensionMap;//对应dimension_map字段
    private Sort mSort;//对应sort字段
    private JSONObject mFilterValue;//对应filter_value
    private JSONObject mRawData;//原始数据
    private Group mGroup;
    private Hyperlink mHyperlink;//对应明细表的超链接,暂时只发现明细表使用
    private JSONObject mOriginalFilterValue;

    public IFBIDimensionModel(String id) {
        this.mID = id;
    }

    public IFBIDimensionModel(String id, @NonNull JSONObject json) {
        this.mID = id;
        parse(json);
    }

    public void parse(@NonNull JSONObject json) {
        mRawData = json;
        mName = json.optString("name");
        mDimensionType = json.optInt("type");
        mSrc = Src.parse(json.optJSONObject("_src"));
        mDimensionMap = json.optJSONObject("dimension_map");
        isUsed = json.optBoolean("used");

        mHyperlink = Hyperlink.parse(json);

        mFilterValue = json.optJSONObject("filter_value");
        try {
            mOriginalFilterValue = mFilterValue == null ? new JSONObject() : new JSONObject(mFilterValue.toString());
        } catch (JSONException e) {
        }

        mGroup = Group.parse(json.optJSONObject("group"));

        mSettings = Settings.parse(json.optJSONObject("settings"));

        mSort = Sort.parse(json.optJSONObject("sort"));

    }

    /**
     * 还原为JSON对象,可用于向后台请求的参数,可以精简掉一些不需要传递的参数
     *
     * @throws JSONException
     */
    public JSONObject toJSON() throws JSONException {
        //暂时在原来的options基础上修改,否则可能一些未确定的参数会被遗漏
        JSONObject object = mRawData == null ? new JSONObject() : mRawData;
        object.put("did", mID);
        object.put("name", mName);
        object.put("type", mDimensionType);
        object.put("_src", mSrc.toJSON());
        object.put("dimension_map", mDimensionMap);
        object.put("used", isUsed);
        object.put("filter_value", mFilterValue);
        object.put("group", mGroup.toJSON());
        if (mSort != null) {
            object.put("sort", mSort.toJSON());
        }
        if (mSettings != null) {
            object.put("settings", mSettings.toJSON());//后续如果改为按需传递参数,这个参数可能不是必须的
        }

        return object;
    }

    /**
     * 维度id,在原始数据中作为键名
     */
    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    /**
     * 维度名称
     */
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    /**
     * @return 维度样式设置信息
     */
    public Settings getSettings() {
        return mSettings;
    }

    public void setSettings(Settings settings) {
        mSettings = settings;
    }

    /**
     */
    public int getDimensionType() {
        return mDimensionType;
    }

    public void setDimensionType(int dimensionType) {
        mDimensionType = dimensionType;
    }

    /**
     * 维度关联表,暂时保留原始JSON数据,没有转换的必要
     */
    public JSONObject getDimensionMap() {
        return mDimensionMap;
    }

    public void setDimensionMap(JSONObject dimensionMap) {
        mDimensionMap = dimensionMap;
    }

    /**
     * @return 排序规则
     */
    public Sort getSort() {
        return mSort;
    }

    public void setSort(Sort sort) {
        mSort = sort;
    }

    public Group getGroup() {
        return mGroup;
    }

    /**
     * 数据库中的信息,暂时保留原始JSON数据,没有转换的必要
     */
    public Src getSrc() {
        return mSrc;
    }

    public void setSrc(Src _src) {
        this.mSrc = _src;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        this.isUsed = used;
    }

    public JSONObject getFilterValue() {
        return mFilterValue;
    }

    public void setFilterValue(JSONObject filterValue) {
        mFilterValue = filterValue;
    }

    public JSONObject getOriginalFilterValue() {
        return mOriginalFilterValue;
    }

    public Hyperlink getHyperlink() {
        return mHyperlink;
    }

    public void setHyperlink(Hyperlink hyperlink) {
        mHyperlink = hyperlink;
    }

    /**
     * 维度的一些样式设置
     */
    public static class Settings {

        private String unit;//对应unit字段,
        private boolean num_separators;//对应num_separators字段
        private int format;//对应format字段
        private int num_level;//对应num_level字段

        private int mIconStyle = 1;//对应icon_style字段
        private String mark;//对应mark字段

        private List<JSONObject> conditions;//对应conditions字段,原始值为JSONArray类型

        /**
         * 解析Settings对象
         *
         * @param json dimension中的settings字段
         */
        public static Settings parse(JSONObject json) {
            if (json == null || json.length() == 0) {
                return null;
            }
            Settings settings = new Settings();
            settings.setUnit(json.optString("unit"));
            settings.setNumSeparators(json.optBoolean("num_separators"));
            settings.setFormat(json.optInt("format"));
            settings.setNumLevel(json.optInt("num_level", 1));
            settings.setIconStyle(json.optInt("icon_style"));
            settings.setMark(json.optString("mark"));
            List<JSONObject> conditions = new ArrayList<>();
            JSONArray conditionArray = json.optJSONArray("conditions");
            if (conditionArray != null) {
                for (int i = 0, n = conditionArray.length(); i < n; i++) {
                    conditions.add(conditionArray.optJSONObject(i));
                }
            }
            settings.setConditions(conditions);
            return settings;
        }

        /**
         * 还原成JSON对象
         */
        public JSONObject toJSON() throws JSONException {
            JSONObject object = new JSONObject();
            object.put("unit", unit);
            object.put("num_separators", num_separators);
            object.put("format", format);
            object.put("num_level", num_level);
            object.put("icon_style", mIconStyle);
            object.put("mark", mark);
            JSONArray array = new JSONArray();
            for (JSONObject condition : conditions) {
                array.put(condition);
            }
            object.put("conditions", array);
            return object;
        }

        /**
         * @return 单位
         */
        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        /**
         * 指标图标类型
         *
         * @return 其值是1~3,分别表示无、圆点图标、箭头图标
         */
        public int getIconStyle() {
            return mIconStyle;
        }

        public void setIconStyle(int iconStyle) {
            mIconStyle = iconStyle;
        }

        /**
         * 是否显示千分符
         */
        public boolean hasNumSeparators() {
            return num_separators;
        }

        public void setNumSeparators(boolean num_separators) {
            this.num_separators = num_separators;
        }

        /**
         * 数字格式,比如保留几位小数,
         *
         * @return 其值为1~4,对应常规、0、0.0、0.00
         */
        public int getFormat() {
            return format;
        }

        public void setFormat(int format) {
            this.format = format;
        }

        /**
         * 数字显示的数量级,
         *
         * @return 其值为1~5,对应个、万、百万、亿、%
         */
        public int getNumLevel() {
            return num_level;
        }

        public void setNumLevel(int num_level) {
            this.num_level = num_level;
        }

        /**
         * 指标图标标记的分界值
         *
         * @return 当指定需要图标标记功能是, 该值决定使用何种图标的临界值, 一般是个数字
         */
        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        /**
         * 文字颜色条件,每个条件形如:
         * <br>
         * {
         * "color":"#09abe9",
         * "range":{
         * "min":"0",
         * "max":"100",
         * "closemin":false,
         * "closemax":false
         * },
         * "cid":"263d30e99bdab4b7"
         * }
         */
        public List<JSONObject> getConditions() {
            return conditions;
        }

        public void setConditions(List<JSONObject> conditions) {
            this.conditions = conditions;
        }
    }

    public static class Src {

        private JSONObject mRawData;
        private String mFiledId;
        private Expression mExpression;

        public static Src parse(JSONObject json) {
            Src src = new Src();
            src.mRawData = json;
            if (json == null || json.length() == 0) {
                return src;
            }
            if (json.has("expression")) {
                src.mExpression = Expression.parse(json.optJSONObject("expression"));
            } else {
                src.mFiledId = json.optString("filed_id");
            }
            return src;
        }

        public JSONObject toJSON() {
            return mRawData;
        }

        public JSONObject getRawData() {
            return mRawData;
        }

        public String getFiledId() {
            return mFiledId;
        }

        public Expression getExpression() {
            return mExpression;
        }

        public static class Expression {

            private List<String> mIds = new ArrayList<>();
            private String mFormulaValue;

            public static Expression parse(JSONObject json) {
                Expression expression = new Expression();
                JSONArray ids = json.optJSONArray("ids");
                for (int i = 0; i < ids.length(); i++) {
                    expression.mIds.add(ids.optString(i));
                }
                expression.mFormulaValue = json.optString("formula_value");
                return expression;
            }

            public String getFormulaValue() {
                return mFormulaValue;
            }

            public List<String> getIds() {
                return mIds;
            }
        }
    }

    /**
     * 排序规则
     * <br>
     * 对应sort字段
     */
    public static class Sort {
        private int mType;//对应sort.type字段
        private String mTarget;//对应sort.sort_target字段
        private JSONArray mDetails;

        public static Sort parse(JSONObject json) {
            if (json == null || json.length() == 0) {
                return null;
            }
            Sort sort = new Sort();
            sort.mTarget = json.optString("sort_target", "");
            sort.mType = json.optInt("type", 3);
            sort.mDetails = json.optJSONArray("details");
            return sort;
        }

        public JSONObject toJSON() throws JSONException {
            JSONObject object = new JSONObject();
            if (mTarget.length() != 0) {
                object.put("sort_target", mTarget);
            }
            if (mDetails != null) {
                object.put("details", mDetails);
            }
            object.put("type", mType);
            return object;
        }

        /**
         */
        public int getType() {
            return mType;
        }

        public void setType(int type) {
            this.mType = type;
        }

        /**
         * 表示作为排序基准的维度的ID,如果无值表示以自身为基准作为排序,有值则是按该值为ID的维度来排序。
         */
        public String getTarget() {
            return mTarget;
        }

        public void setTarget(String target) {
            mTarget = target;
        }
    }

    public static class Group{

        private JSONObject mRawData;
        private int mType;
        private GroupValue mGroupValue;

        /**
         */
        private int unGroup2Other;
        private String unGroup2OtherName;
        private List<DetailEntry> mDetails = new ArrayList<>();

        public static Group parse(JSONObject json){
            Group group = new Group();
            group.mRawData = json;
            if (json == null || json.length() == 0){
                return group;
            }
            group.mType = json.optInt("type");

            group.mGroupValue = GroupValue.parse(json.optJSONObject("group_value"));

            group.unGroup2Other = json.optInt("ungroup2Other");
            group.unGroup2OtherName = json.optString("ungroup2OtherName");
            group.mDetails = DetailEntry.parse(json.optJSONArray("details"));

            return group;
        }

        /**
         */
        public int getType() {
            return mType;
        }

        public void setType(int type) {
            mType = type;
        }

        public int getUnGroup2Other() {
            return unGroup2Other;
        }

        public void setUnGroup2Other(int unGroup2Other) {
            this.unGroup2Other = unGroup2Other;
        }

        public String getUnGroup2OtherName() {
            return unGroup2OtherName;
        }

        public void setUnGroup2OtherName(String unGroup2OtherName) {
            this.unGroup2OtherName = unGroup2OtherName;
        }

        public List<DetailEntry> getDetails() {
            return mDetails;
        }

        public void setDetails(List<DetailEntry> details) {
            mDetails = details;
        }

        public GroupValue getGroupValue() {
            return mGroupValue;
        }

        public void setGroupValue(GroupValue groupValue) {
            mGroupValue = groupValue;
        }

        public JSONObject toJSON(){
            return mRawData;
        }

        public static class DetailEntry{
            private String mID;
            private String mValue;
            private List<String> mContent = new ArrayList<>();


            public static List<DetailEntry> parse(JSONArray details) {
                List<DetailEntry> list = new ArrayList<>();
                if (details == null) {
                    return list;
                }
                for (int i = 0, n = details.length(); i < n; i++) {
                    JSONObject obj = details.optJSONObject(i);
                    if (obj == null || obj.length() == 0) {
                        continue;
                    }
                    DetailEntry entry = new DetailEntry();
                    entry.mID = obj.optString("id");
                    entry.mValue = obj.optString("value");
                    JSONArray content = obj.optJSONArray("content");
                    if (content != null) {
                        for (int j = 0, m = content.length(); j < m; j++) {
                            JSONObject contentObj = content.optJSONObject(j);
                            if (contentObj == null || contentObj.length() == 0) {
                                continue;
                            }
                            entry.mContent.add(contentObj.optString("value"));
                        }
                    }

                    list.add(entry);
                }
                return list;
            }

            public String getID() {
                return mID;
            }

            public void setID(String ID) {
                mID = ID;
            }

            public String getValue() {
                return mValue;
            }

            public void setValue(String value) {
                mValue = value;
            }

            public List<String> getContent() {
                return mContent;
            }

            public void setContent(List<String> content) {
                mContent = content;
            }
        }

        public static class GroupValue{
            private String mUseOther;
            private List<GroupNode> mGroupNodes = new ArrayList<>();
            public static GroupValue parse(JSONObject value){
                if (value == null || value.length() == 0){
                    return null;
                }
                GroupValue groupValue = new GroupValue();
                groupValue.mUseOther = value.optString("use_other");
                groupValue.mGroupNodes = GroupNode.parse(value.optJSONArray("group_nodes"));
                return groupValue;
            }

            public String getUseOther() {
                return mUseOther;
            }

            public void setUseOther(String useOther) {
                mUseOther = useOther;
            }

            public List<GroupNode> getGroupNodes() {
                return mGroupNodes;
            }

            public void setGroupNodes(List<GroupNode> groupNodes) {
                mGroupNodes = groupNodes;
            }
        }
        public static class GroupNode{
            private JSONObject mRawData;
            private int mMin;
            private int mMax;
            private String mGroupName;
            private String mID;
            private boolean isCloseMin;
            private boolean isCloseMax;

            public static List<GroupNode> parse(JSONArray nodes){
                List<GroupNode> list = new ArrayList<>();
                if (nodes == null || nodes.length() == 0){
                    return list;
                }
                for(int i = 0,n = nodes.length();i<n;i++){
                    JSONObject obj = nodes.optJSONObject(i);
                    GroupNode node = new GroupNode();
                    node.mRawData = obj;
                    node.mID = obj.optString("id");
                    node.mMin = obj.optInt("min");
                    node.mMax = obj.optInt("max");
                    node.isCloseMin = obj.optBoolean("closemin");
                    node.isCloseMax = obj.optBoolean("closemax");
                    node.mGroupName = obj.optString("group_name");

                    list.add(node);
                }
                return list;
            }

            public int getMin() {
                return mMin;
            }

            public void setMin(int min) {
                mMin = min;
            }

            public int getMax() {
                return mMax;
            }

            public void setMax(int max) {
                mMax = max;
            }

            public String getGroupName() {
                return mGroupName;
            }

            public void setGroupName(String groupName) {
                mGroupName = groupName;
            }

            public String getID() {
                return mID;
            }

            public void setID(String ID) {
                mID = ID;
            }

            public boolean isCloseMin() {
                return isCloseMin;
            }

            public void setCloseMin(boolean closeMin) {
                isCloseMin = closeMin;
            }

            public boolean isCloseMax() {
                return isCloseMax;
            }

            public void setCloseMax(boolean closeMax) {
                isCloseMax = closeMax;
            }

            public JSONObject toJSON(){
                return mRawData;
            }
        }
    }

    /**
     * 对应hyperlink字段,暂时只发现明细表在使用
     */
    public static class Hyperlink{
        private boolean isUsed = false;
        private String mExpression;
        public static Hyperlink parse(JSONObject json){
            Hyperlink hyperlink = new Hyperlink();
            if (json == null){
                return hyperlink;
            }
            JSONObject obj = json.optJSONObject("hyperlink");
            if (obj != null){
                hyperlink.isUsed = obj.optBoolean("used",false);
            }
            return hyperlink;
        }

        public boolean isUsed() {
            return isUsed;
        }

        public void setUsed(boolean used) {
            isUsed = used;
        }

        public String getExpression() {
            return mExpression;
        }

        public void setExpression(String expression) {
            mExpression = expression;
        }
    }

    /*
    //数据库信息
    public static class DBInfo {
        private String field_id;
        private String id;
        private String table_id;

        public String getFieldId() {
            return field_id;
        }

        public void setFieldId(String field_id) {
            this.field_id = field_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTableId() {
            return table_id;
        }

        public void setTableId(String table_id) {
            this.table_id = table_id;
        }
    }
    */
}
