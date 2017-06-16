package com.li.jacky.draggrouprecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import com.li.jacky.draggrouprecyclerview.data.SampleStrategy;
import com.li.jacky.draggrouprecyclerview.helper.IFBISimpleItemTouchHelperCallback;
import com.li.jacky.draggrouprecyclerview.model.IFBIBaseWidgetModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SampleStrategy sampleStrategy = initStrategy();
        IFBIDragSortRecyclerViewAdapter adapter = new IFBIDragSortRecyclerViewAdapter(this,sampleStrategy );
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new IFBISimpleItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * 根据自身数据结构定制
     * {@link com.li.jacky.draggrouprecyclerview.data.IFBISettingStrategy#initRegions}
     */
    @NonNull
    private SampleStrategy initStrategy() {
        IFBIBaseWidgetModel model = null;
        try {
            model = new IFBIBaseWidgetModel(new JSONObject(Constant.DATA));
        } catch (JSONException e) {
        }
        return new SampleStrategy(model);
    }
}
