package com.ustb.shellbox.shelllife.customview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.databean.JobInfo;
import com.ustb.shellbox.shelllife.webactivity.WebJobActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Created by 37266 on 2017/3/21.
 */

public class ZhaoPinHuiTabs extends TabBaseView{
    public ListView listView;
    private ArrayList<JobInfo> arrayList;
    private List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
    public ZhaoPinHuiTabs(Activity activity,ArrayList<JobInfo> list) {
        super(activity);
        this.arrayList=list;
        initView();
    }
    @Override
    public void initView() {
        rootView= View.inflate(activity, R.layout.tabs_zhaopinhui,null);
        listView=(ListView) rootView.findViewById(R.id.lv_tabs_zhaopinhui);
    }
    @Override
    public void initData() {
        for (int i=0;i<arrayList.size();i++){
            Map<String,Object> map=new HashMap<String,Object>();
            Log.e(arrayList.get(i).getContent(),arrayList.get(i).getHref()+" "+arrayList.get(i).getTime());
            map.put("content",arrayList.get(i).getContent());
            map.put("time",arrayList.get(i).getTime());
            map.put("href",arrayList.get(i).getHref());
            mapList.add(map);
        }
        listView.setAdapter(new SimpleAdapter(activity,mapList,R.layout.item_lv_tabs_zhaopinhui,new String[]{"content","time"}
                ,new int[]{R.id.tv_tabs_zhaopinhui_content,R.id.tv_tabs_zhaopinhui_time}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String href=arrayList.get(position).getHref();
                Intent intent=new Intent(activity, WebJobActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("jobUrl",href);
                intent.putExtras(bundle);
            }
        });
    }
}
