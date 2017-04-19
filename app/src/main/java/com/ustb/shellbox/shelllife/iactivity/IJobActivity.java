package com.ustb.shellbox.shelllife.iactivity;




import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;

import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.MainActivity;
import com.ustb.shellbox.shelllife.customview.DepthPageTransformer;
import com.ustb.shellbox.shelllife.customview.TabBaseView;
import com.ustb.shellbox.shelllife.customview.ZhaoPinHuiTabs;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.databean.JobInfo;
import com.ustb.shellbox.shelllife.store.PersistentCookieStore;
import com.ustb.shellbox.shelllife.webactivity.WebJobActivity;
import com.ustb.shellbox.shelllife.webactivity.WebJobDetialsActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IJobActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private ListView listView_zhaopin;
    private ListView listView_danwei;
    private ListView listView_shixi;
    private  ArrayList<ZhaoPinHuiTabs>  tabBaseViewArrayList;
    public OkHttpClient okHttpClient_job;
    private  ArrayList<JobInfo> jobInfos=new ArrayList<>();
    private ArrayList<JobInfo> danweiInfos=new ArrayList<>();
    private ArrayList<JobInfo> shixiInfos=new ArrayList<>();
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.HANDELER_SHOW_TOAST:
                    Toast.makeText(IJobActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                case Constants.HANDLER_UPDATA_VIEWPAGER:
                    initViewPager();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijob);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initView();
        initData();
        getZhaoPinInfo();
    }
    private void initData(){
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.cookieJar(new JobCookierManager()).readTimeout(10, TimeUnit.SECONDS).connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS);
        okHttpClient_job=builder.build();
    }
    private void initView(){
        radioGroup=(RadioGroup)findViewById(R.id.rg_job_tabs);
        radioGroup.check(R.id.rb_tab1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_tab1:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_tab2:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_tab4:
                        viewPager.setCurrentItem(2);
                        break;
                }
            }
        });
        viewPager=(ViewPager)findViewById(R.id.viewpager_job_tabs);
    }
    private void initViewPager(){
        //初始化四个子页面
        tabBaseViewArrayList=new ArrayList<ZhaoPinHuiTabs>();
        ZhaoPinHuiTabs zhaoPinHuiTabs=new ZhaoPinHuiTabs(IJobActivity.this,jobInfos);
        ZhaoPinHuiTabs zhaoPinHuiTabs1=new ZhaoPinHuiTabs(IJobActivity.this,danweiInfos);
        ZhaoPinHuiTabs zhaoPinHuiTabs3=new ZhaoPinHuiTabs(IJobActivity.this,shixiInfos);
        tabBaseViewArrayList.add(zhaoPinHuiTabs);
        tabBaseViewArrayList.add(zhaoPinHuiTabs1);
        tabBaseViewArrayList.add(zhaoPinHuiTabs3);
        viewPager.setAdapter(new ViewPagerAdapter());
        viewPager.setPageTransformer(true,new DepthPageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                switch (position){
//                    case 0:
//                        radioGroup.check(R.id.rb_tab1);
//                        break;
//                    case 1:
//                        radioGroup.check(R.id.rb_tab2);
//                        break;
//                    case 2:
//                        radioGroup.check(R.id.rb_tab4);
//                        break;
//
//                }
            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    radioGroup.check(R.id.rb_tab1);
                }else if(position==1){
                    radioGroup.check(R.id.rb_tab2);
                }else if(position==2){
                    radioGroup.check(R.id.rb_tab4);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        listView_zhaopin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String href=jobInfos.get(position).getHref();
                Intent intent=new Intent(IJobActivity.this, WebJobDetialsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("jobUrl",href);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        listView_danwei.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String href=danweiInfos.get(position).getHref();
                Intent intent=new Intent(IJobActivity.this, WebJobDetialsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("jobUrl",href);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
    private void getZhaoPinInfo(){
        Request request=new Request.Builder().url(Constants.URL_JOB).build();
        okHttpClient_job.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=handler.obtainMessage();
                message.obj="信息加载失败"+e.toString();
                message.what=Constants.HANDELER_SHOW_TOAST;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                Document document= Jsoup.parse(responseData);
                Element div_zhaopin=document.getElementsByAttributeValueContaining("id","tab_zp1").first();
                Element ul_zhaopin=div_zhaopin.getElementsByTag("ul").first();
                Elements lis_zhaopin=ul_zhaopin.getElementsByTag("li");
                for(int i=0;i<lis_zhaopin.size();i++){
                    Element li=lis_zhaopin.get(i);
                    String content=li.getElementsByTag("font").first().text();
                    String time=li.getElementsByTag("span").first().text();
                    String href="http://job.ustb.edu.cn"+li.getElementsByTag("a").get(0).attr("href");
                    Log.e("jobInfos",href);
                    jobInfos.add(new JobInfo(content,href,time));
                }
                Element div_danwei=document.getElementsByAttributeValueContaining("id","tab_zp2").first();
                Element ul_danwei=div_danwei.getElementsByTag("ul").first();
                Elements lis_danwei=ul_danwei.getElementsByTag("li");
                for(int i=0;i<lis_danwei.size();i++){
                    Element li=lis_danwei.get(i);
                    String content=li.getElementsByTag("a").first().text();
                    String time=li.getElementsByTag("span").first().text();
                    String href="http://job.ustb.edu.cn"+li.getElementsByTag("a").get(0).attr("href");
                    Log.e("danweiInfos",href);
                    danweiInfos.add(new JobInfo(content,href,time));
                }
                Element div_shixi=document.getElementsByAttributeValueContaining("id","tab_zp8").first();
                Element ul_shixi=div_shixi.getElementsByTag("ul").first();
                Elements lis_shixi=ul_shixi.getElementsByTag("li");
                for(int i=0;i<lis_shixi.size();i++){
                    Element li=lis_shixi.get(i);
                    String content=li.getElementsByTag("a").first().text();
                    String time=li.getElementsByTag("span").first().text();
                    String href="http://job.ustb.edu.cn"+li.getElementsByTag("a").get(0).attr("href");
                    Log.e("shixiInfos",href);
                    shixiInfos.add(new JobInfo(content,href,time));
                }
                handler.sendEmptyMessage(Constants.HANDLER_UPDATA_VIEWPAGER);
            }
        });
    }
    class ViewPagerAdapter extends PagerAdapter{
        //决定viewpager数量
        @Override
        public int getCount() {
            return  tabBaseViewArrayList.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
        //每次滑动的时候生成组件

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ZhaoPinHuiTabs zhaoPinHuiTabs=tabBaseViewArrayList.get(position);
            container.addView(zhaoPinHuiTabs.rootView);
            zhaoPinHuiTabs.initData();
            if(position==0){
                listView_zhaopin=tabBaseViewArrayList.get(position).listView;
            }else if(position==1){
                listView_danwei=tabBaseViewArrayList.get(position).listView;
            }else if(position==2){
                listView_shixi=tabBaseViewArrayList.get(position).listView;
                listView_shixi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String href=shixiInfos.get(position).getHref();
                        Intent intent=new Intent(IJobActivity.this, WebJobDetialsActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("jobUrl",href);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            return zhaoPinHuiTabs.rootView;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(IJobActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    class JobCookierManager implements CookieJar {
        final PersistentCookieStore cookieStore = new PersistentCookieStore(getApplicationContext());
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }
}
