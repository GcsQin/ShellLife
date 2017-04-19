package com.ustb.shellbox.shelllife.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.alert_act.LinkCampusWebAct;
import com.ustb.shellbox.shelllife.alert_act.LinkEducationSystemAct;
import com.ustb.shellbox.shelllife.alert_act.LinkEportalAct;
import com.ustb.shellbox.shelllife.alert_act.LinkVolunteerAct;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.databean.ConvenientBannerBean;
import com.ustb.shellbox.shelllife.databean.TeachNotifyBean;
import com.ustb.shellbox.shelllife.fragment.LeftMenuFragment;
import com.ustb.shellbox.shelllife.fragment.RightMenuFragment;
import com.ustb.shellbox.shelllife.iactivity.IJobActivity;
import com.ustb.shellbox.shelllife.iactivity.ILibraryActivity;
import com.ustb.shellbox.shelllife.iactivity.IOfficialWebListActivity;
import com.ustb.shellbox.shelllife.more_wonderful.USTBliveStreamingActivity;
import com.ustb.shellbox.shelllife.webactivity.WebJobActivity;
import com.ustb.shellbox.shelllife.webactivity.WebTeachNotifyActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends SlidingFragmentActivity {
    private long systemTimes;
    //View
    private String[] grid_itemName;
    private int[] grid_itemIcon;
    private ConvenientBanner convenientBanner;
    //netWork
    private OkHttpClient okHttpClient;
    private LayoutInflater mInflater;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPref_diaolog;
    //list
    private ArrayList<TeachNotifyBean> listOfTeachNotify;
    private ArrayList<Map<String,Object>> listItems= new ArrayList<Map<String,Object>>();
    private ArrayList<String> listOfBannerImg=new ArrayList<>();
    private ArrayList<ConvenientBannerBean> listOfBannerImgInfo=new ArrayList<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case  Constants.HANDLER_UPDATA_VIEWPAGER:
                    initListView();
                    break;
                case Constants.HANDLER_UPDATA_LISTVIEW:
                    initConvenientBanner();
                    break;
                case Constants.HANDELER_SHOW_TOAST:
                    Toast.makeText(MainActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置无标题栏效果
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        sharedPref_diaolog=getSharedPreferences("dialog",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("usetips",false)==false){
            sharedPreferences.edit().putBoolean("usetips",true).commit();
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            final AlertDialog dialog=builder.create();
            View dialogView=View.inflate(getApplicationContext(),R.layout.layout_custom_dialog_usetips,null);
            ImageButton imageButton=(ImageButton)dialogView.findViewById(R.id.imgbtn_cancle_right);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.setView(dialogView);
            dialog.show();
        }
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//        getSystemTime();
        initData();
        initViewPagerImg();
        initLeftRightMenu();
        initGridView();
        initListViewItemData();
    }
    private void getSystemTime() {
        //获取系统时间戳的几种方式
        systemTimes = System.currentTimeMillis();
//        systemTimes=new Date().getTime();
//        systemTimes= Calendar.getInstance().getTime()
        Log.e("系统时间戳",systemTimes+"");

    }
    private void initData(){
        grid_itemName=getResources().getStringArray(R.array.main_act_gridview_itemname);
        grid_itemIcon=new int[]{R.drawable.main_gv_campusweb_48,R.drawable.main_gv_netcost_48,R.drawable.main_gv_campuslib_48,
                R.drawable.main_gv_edusystem_48,R.drawable.main_gv_schoolinfo_48,R.drawable.main_gv_volunteer_48,R.drawable.main_gv_job_48,R.drawable.main_gv_more_48
        };
        //view
        convenientBanner=(ConvenientBanner)findViewById(R.id.convenientbanner);
        //net
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        okHttpClient=builder.connectTimeout(10,TimeUnit.SECONDS).readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS).build();
        listOfTeachNotify=new ArrayList<TeachNotifyBean>();
    }
    private void initViewPagerImg(){
        Request request=new Request.Builder().url(Constants.URL_JOB).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    Message message=handler.obtainMessage();
                    message.what=Constants.HANDELER_SHOW_TOAST;
                    message.obj="加载图片失败"+e.toString();
                    handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    String responseData=response.body().string();
                    Document document=Jsoup.parse(responseData);
                    Element div=document.getElementsByAttributeValueContaining("class","ws_images").first();
                    Elements lis=div.getElementsByTag("li");
                    for (int i=0;i<lis.size();i++){
                        Element li=lis.get(i);
                        Element a=li.getElementsByTag("a").first();
                        String bannerHref=a.attr("href");
                        Element img=li.getElementsByTag("img").first();
                        String bannerTitle=img.attr("title");
                        String bannerImgSrc=img.attr("src");
                        listOfBannerImgInfo.add(new ConvenientBannerBean(bannerHref,bannerTitle,bannerImgSrc));
                        listOfBannerImg.add(bannerImgSrc);
                    }
                    handler.sendEmptyMessage(Constants.HANDLER_UPDATA_LISTVIEW);
            }
        });
    }
    //**********************************************************************************************
    // SlidingMenu
    private  void initLeftRightMenu(){
        //Fragment存在几个不同的包中，我们需要导入的v4包中的fragment
        //开始设置左侧菜单
        Fragment leftFragment=new LeftMenuFragment();
        setBehindContentView(R.layout.layout_menu_replace);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_replace,leftFragment).commit();
        final SlidingMenu slidingMenu=getSlidingMenu();//单例模式?
//       左右双开模式，一共有左，右，左右双开三种模式,第三种模式必须在加上setSecondaryMenu()这个方法
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置触摸屏幕的模式
//        slidingMenu.setShadowWidth(80);//设置左侧菜单阴影图片的宽度
//        slidingMenu.setShadowDrawable(R.drawable.bg_splash);//设置左侧菜单阴影图片
        slidingMenu.setBehindOffsetRes(R.dimen.left_menu_width);//设置左侧菜单完全划出后的主界面的宽度
        //设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.6f);
        /*设置右侧菜单（二级菜单）
        * */
        Fragment rightFragment=new RightMenuFragment();
        slidingMenu.setSecondaryMenu(R.layout.layout_menu_replace_sec);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_replace_sec,rightFragment).commit();
    }

    public void showLeftMenu(View view){//展示左边菜单
        getSlidingMenu().showMenu();
    }

    public void showRightMenu(View view){//展示右边菜单
        getSlidingMenu().showSecondaryMenu();
    }
    //**********************************************************************************************
    //ConvenientBanner
    private void initConvenientBanner(){
                convenientBanner.startTurning(5000);//设置自动切换以及自动切换的时间间隔
                convenientBanner.setPointViewVisible(true);//设置指示器（切换小圆点）是否可见
                convenientBanner.setManualPageable(true);//设置手势滑动是否能影响切换,true为可以
                convenientBanner.setPageIndicator(new int[]{R.drawable.dot_focus,R.drawable.dot_unfocus});

//        convenientBanner.stopTurning()关闭自动切换
//      convenientBanner.setPageIndicator()这个方法可以让我们指定两个drawable图片来作为我们的指示器
//        convenientBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)//设置指示器位置
//        convenientBanner.setPageTransformer(new Default);//设置切换效果,不需要翻页效果可以不设置

                convenientBanner.setPages(new CBViewHolderCreator() {
                    @Override
                    public Object createHolder() {
                        return new NetWorkImageHolderView();
                    }
                },listOfBannerImg);
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String href="http://job.ustb.edu.cn"+listOfBannerImgInfo.get(position).getBannerHref();
                Log.e("href",href);
                Intent intent=new Intent(MainActivity.this,WebJobActivity.class);
                Bundle notifyBundle=new Bundle();
                notifyBundle.putString("jobUrl",href);
                intent.putExtras(notifyBundle);
                startActivity(intent);
                finish();
            }
        });
    }

    public  class NetWorkImageHolderView implements Holder<String>{
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView=new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }
        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(MainActivity.this).load(data).into(imageView);
        }
    }
    //**********************************************************************************************
    //GridView
    public void initGridView(){
        GridView gridView=(GridView)findViewById(R.id.gv_main);
        gridView.setAdapter(new GridAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showOfficialWeb();
                        break;
                    case 1:
                        alertLinkCampusLoginAct();
                        break;
                    case 2:
                        enterLibraryActivity();
                        break;
                    case 3:
                        alertLinkEducationSystemAct();
                        break;
                    case 4:
                        alertLinkEportalAct();
                        break;
                    case 5:
                        alertLinkVolunteerAct();
                        break;
                    case 6:
                        enterIJobActivity();
                        break;
                    case 7:
                        enterMoreActitivity();
                        break;

                }
            }
        });
    }
    class GridAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return grid_itemIcon.length;
        }

        @Override
        public Object getItem(int position) {
            return grid_itemName[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            mInflater=LayoutInflater.from(MainActivity.this);
            MainViewHolder mainViewHolder;
            if(convertView==null) {
                convertView= mInflater.inflate(R.layout.item_gv_main,null);
                mainViewHolder=new MainViewHolder();
                mainViewHolder.holderImageView = (ImageView)convertView.findViewById(R.id.item_grid_icon);
                mainViewHolder.holderTextView= (TextView) convertView.findViewById(R.id.item_grid_name);
                convertView.setTag(mainViewHolder);
            }else {
                mainViewHolder=(MainViewHolder)convertView.getTag();
            }
            mainViewHolder.holderTextView.setText(grid_itemName[position]);
            mainViewHolder.holderImageView.setImageResource(grid_itemIcon[position]);
            return convertView;
        }
    }
    class MainViewHolder{
        private ImageView holderImageView;
        private TextView holderTextView;
    }
    //GridView的Item点击事件
    private void showOfficialWeb(){//0
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setMessage("该模块部分功能需要连接校园网络,请确保您链接了校园网络，以便我们为您提供更方便的服务");
//        builder.setNegativeButton("好,我知道了", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
        builder.setPositiveButton("好,我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this,IOfficialWebListActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        Dialog dialog=builder.create();
        if(sharedPref_diaolog.getBoolean("officialDialog",false)==false){
            dialog.show();
            sharedPref_diaolog.edit().putBoolean("officialDialog",true).commit();
        }else {
            startActivity(new Intent(MainActivity.this,IOfficialWebListActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }
    private void alertLinkCampusLoginAct(){//1
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setMessage("该模块部分功能需要连接校园网络,请确保您链接了校园网络，以便我们为您提供更方便的服务");
//        builder.setNegativeButton("好,我知道了", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(MainActivity.this, LinkCampusWebAct.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                finish();
//                dialog.dismiss();
//            }
//        });
        builder.setPositiveButton("好,我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, LinkCampusWebAct.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        Dialog dialog=builder.create();
        if(sharedPref_diaolog.getBoolean("webcampusDialog",false)==false){
            dialog.show();
            sharedPref_diaolog.edit().putBoolean("webcampusDialog",true).commit();
        }else {
            startActivity(new Intent(MainActivity.this, LinkCampusWebAct.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }
    private void enterLibraryActivity(){//2
        startActivity(new Intent(MainActivity.this,ILibraryActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
    private void alertLinkEducationSystemAct(){//3
        startActivity(new Intent(this,LinkEducationSystemAct.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    private void alertLinkEportalAct(){//4
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setMessage("该模块部分功能需要连接校园网络,请确保您链接了校园网络，以便我们为您提供更方便的服务");
//        builder.setNegativeButton("好,我知道了", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(MainActivity.this,LinkEportalAct.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                finish();
//                dialog.dismiss();
//            }
//        });
        builder.setPositiveButton("好,我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this,LinkEportalAct.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        Dialog dialog=builder.create();
        if(sharedPref_diaolog.getBoolean("eportalDialog",false)==false){
            dialog.show();
            sharedPref_diaolog.edit().putBoolean("eportalDialog",true).commit();
        }else {
            startActivity(new Intent(MainActivity.this,LinkEportalAct.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }
    private void alertLinkVolunteerAct(){//5
        startActivity(new Intent(this, LinkVolunteerAct.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    private void enterIJobActivity(){//6
        startActivity(new Intent(this,IJobActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    private void enterMoreActitivity(){//7
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setMessage("该模块部分功能需要连接校园网络,请确保您链接了校园网络，以便我们为您提供更方便的服务");
//        builder.setNegativeButton("好,我知道了", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(MainActivity.this,USTBliveStreamingActivity.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                finish();
//                dialog.dismiss();
//            }
//        });
        builder.setPositiveButton("好,我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this,USTBliveStreamingActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        Dialog dialog=builder.create();
        if(sharedPref_diaolog.getBoolean("liveDialog",false)==false){
            dialog.show();
            sharedPref_diaolog.edit().putBoolean("liveDialog",true).commit();
        }else {
            startActivity(new Intent(MainActivity.this,USTBliveStreamingActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }
    //**********************************************************************************************
    //ListView
    private void initListView(){
        ListView listView=(ListView)findViewById(R.id.lv_main);
        for (int i=0;i<listOfTeachNotify.size();i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("content",listOfTeachNotify.get(i).getContent());
            listItem.put("data",listOfTeachNotify.get(i).getData());
            listItem.put("href",listOfTeachNotify.get(i).getHref());
            listItems.add(listItem);
        }
        listView.setAdapter(new SimpleAdapter(MainActivity.this,listItems,R.layout.item_lv_main_teach_notify,
                new String[]{"content","data"},new int[]{R.id.tv_list_notify_content,R.id.tv_list_notify_data}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String href=listOfTeachNotify.get(position).getHref();
                Intent intent=new Intent(MainActivity.this, WebTeachNotifyActivity.class);
                Bundle notifyBundle=new Bundle();
                notifyBundle.putString("notifyUrl",href);
                intent.putExtras(notifyBundle);
                startActivity(intent);
                finish();
            }
        });
    }
    private void initListViewItemData(){
        final Request request=new Request.Builder().url(Constants.URL_TEACH_USTB).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] bytes= response.body().bytes();
                String html=new String(bytes,"gb2312");
                Document document=Jsoup.parse(html);
                Elements tdEles=document.getElementsByAttributeValue("height", "198");
                Element td=tdEles.get(0);
                Elements tbodys=td.getElementsByTag("tbody");
                Element tbodyIneed=tbodys.get(1);
                Element tdineed=tbodyIneed.child(0).child(0);
                Elements tableNotifys=tdineed.getElementsByTag("table");
                for (int i=0;i<tableNotifys.size();i++){
                    Element tableNotify=tableNotifys.get(i);
                    Element trNotify=tableNotify.child(0).child(0);
                    String content=trNotify.child(0).getElementsByTag("a").get(0).text();
                    String href=trNotify.child(0).getElementsByTag("a").get(0).attr("href");
                    String data=trNotify.child(1).text();
                    listOfTeachNotify.add(new TeachNotifyBean(content,data,href));
                }
                Message message=handler.obtainMessage();
                message.what=Constants.HANDLER_UPDATA_VIEWPAGER;
                handler.sendMessage(message);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        convenientBanner.startTurning(5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        sharedPref_diaolog.edit().clear().commit();
//        Log.e("destory","dd");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            sharedPref_diaolog.edit().clear().commit();
            Log.e("back","bb");
        }
        return super.onKeyDown(keyCode, event);

    }
}
