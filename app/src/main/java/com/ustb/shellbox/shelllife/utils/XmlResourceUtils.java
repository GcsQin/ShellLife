package com.ustb.shellbox.shelllife.utils;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

//import com.ustb.shellbox.shelllife.R;
//import com.ustb.shellbox.shelllife.databean.BannerBean;
//import com.ustb.shellbox.shelllife.databean.Constants;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 37266 on 2017/3/7.
 */
public class XmlResourceUtils {
//    public int[] getDrawableIDAdday(int resId){
//        TypedArray typedArray=
//        return
//    }
class Parent{
    public Parent() {//构造方法
        System.out.print("parent");
    }
    public void parentFun(){
        System.out.print("parentFun");
    }
}
class Child extends Parent{
    public Child() {
        super();//调用父类构造方法
        super.parentFun();//调用父类成员方法
        System.out.print("child");
    }

    @Override
    public void parentFun() { //重写父类方法
        super.parentFun();//调用父类成员方法
        System.out.print("childFun");
    }
}
    private void viewpagerM(){

//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }                /*
//            * 初始化viewPager
//            * */
//        private void initViewPager(){
//            viewPager=(ViewPager)findViewById(R.id.vp_main);
//            linear_dot=(LinearLayout)findViewById(R.id.ll_banner_dot);
//        }
//
//        /*
//        * 初始化viewPager的数据
//        * */
//        private void initViewPagerData(){
//            initDot();
//            viewPager.setAdapter(new BannerPagerAdapter());
//            int middleValue=Integer.MAX_VALUE/2;
//            int yushu=Integer.MAX_VALUE%bannerBeanArrayList.size();
//            viewPager.setCurrentItem(middleValue-yushu);
//            updateDot();
//            Message message=handler.obtainMessage();
//            message.what= Constants.HANDLER_UPDATA_VIEWPAGER;
//            handler.sendMessageDelayed(message, 5000);
//        }
//        /*
//        * 初始化ViewPager滑动个数
//        * */
//        private void initDot(){
//            for(int i=0;i<bannerBeanArrayList.size();i++){
//                View view=new View(this);
//                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(8,8);
//                if(i!=0){
//                    layoutParams.leftMargin=5;
//                }
//                view.setLayoutParams(layoutParams);
//                view.setBackgroundResource(R.drawable.select_dot);
//                linear_dot.addView(view);
//            }
//        }
//        private void initViewPagerListener(){
//            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                }
//
//                /*Page被选中的时候*/
//                @Override
//                public void onPageSelected(int position) {
//                    updateDot();
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//
//                }
//            });
//        }
//        private void updateDot(){
//            int currentPage=viewPager.getCurrentItem()%bannerBeanArrayList.size();
//            for(int i=0;i<linear_dot.getChildCount();i++){
//                if(i==currentPage){
//                    linear_dot.getChildAt(i).setEnabled(true);
//                }else {
//                    linear_dot.getChildAt(i).setEnabled(false);
//                }
//            }
//        }
//        /*
//        * 重写PagerAdapter
//        * */
//        class BannerPagerAdapter extends PagerAdapter {
//            /*getCount()
//            * 缓存的Page数量
//            * */
//            @Override
//            public int getCount() {
//                return Integer.MAX_VALUE;
//            }
//            /*判断当前滑动的View是否是即将展示的View(超过一半就出去)
//            * 返回true表示滑动View是即将展示的View,使用缓存，不创建。flase:重新创建一个即将展示的View对象
//            * */
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//                return view==object;
//            }
//            /*类似于BaseAdapter的getView
//            * 在调用这个方法前,会对position进行一次判断,如果position为getCount-1，就加载最后一个页面。
//            * 如果ViewPager继续向右滑动,因为position=getConut-1,就不会继续向右滑动了。
//            * */
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
//                View view=View.inflate(MainActivity.this,R.layout.item_viewpager,null);
//                ImageView imageView=(ImageView)findViewById(R.id.iv_viewpager_item);
//                Log.e("viewpager", bannerBeanArrayList.size() + "");
////                imageView.setImageBitmap(bannerBeanArrayList.get(position % bannerBeanArrayList.size()).getBannerImage());
//                container.addView(view);
//
//                return view;
//            }
//        /*ViewPager的预加载机制：最多缓存3个View.这个函数的作用是销毁page不在缓存的Page的
//        * container:ViewPager是继承ViewGroup的，所以这个container可以理解为Viewpager
//        * position：当前需要销毁第几个age
//        * object:当前需要销毁的page
//        * */
//            @Override
//    }
    }
    private void loadIMG(){
//        private void initBannerImage(){
//            for (int i=0;i<urlBannerImage.length;i++){
//                loadImagerFromUrl(urlBannerImage[i]);
//            }
//        }
            /*
            * 根据传进来的图片路径下载网络图片,并将网络图片封装到集合中，用于ViewPager从集合中读取图片，需要添加访问网络权限。
            * */
//    private void loadImagerFromUrl(final String path){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    URL url=new URL(path);
//                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(5000);
//                    connection.setReadTimeout(5000);
//                    connection.connect();
//                    if(connection.getResponseCode()==200){
//                        InputStream inputStream=connection.getInputStream();
//                        if(inputStream!=null){
//                            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                            bannerBeanArrayList.add(new BannerBean(bitmap));
//                            Log.e("Main",""+bannerBeanArrayList.size());
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }finally {
//                    Message message=handler.obtainMessage();
//                    message.what=Constants.HANDLER_IMAGE_DOWNLOAD_FINISH;
//                    handler.sendMessage(message);
//                }
//            }
//        }).start();
//    }
    }
}
