package com.ustb.shellbox.shelllife.more_wonderful;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.activity.MainActivity;
import com.ustb.shellbox.shelllife.alert_act.VitamioLiveActivity;

public class USTBliveStreamingActivity extends BaseSetupWizardActivity {
    GridView gridView;
    String[] gv_itemnames;
    int[] gv_itemImgIds;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ustblive_streaming);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initData();
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        startActivity(new Intent(USTBliveStreamingActivity.this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initData(){
        gv_itemnames=getResources().getStringArray(R.array.ustblive_act_gridview_itemname);
        gv_itemImgIds=new int[]{R.drawable.ustblive_cctv1,R.drawable.ustblive_cctv3,R.drawable.ustblive_cctv5,R.drawable.ustblive_cctv5h,
                R.drawable.ustblive_cctv6,R.drawable.ustblive_cctv8,R.drawable.ustblive_cctv_13,R.drawable.ustblive_cctvmovie,
                R.drawable.ustblive_tv_beijing,R.drawable.ustblive_btv_wenyi,R.drawable.ustblive_btv_pe,R.drawable.ustblive_btv_jishi,
                R.drawable.ustblive_tv_anhui,R.drawable.ustblive_tv_dongfang,R.drawable.ustblive_tv_guangdong,R.drawable.ustblive_tv_heilongjiang,
                R.drawable.ustblive_tv_hubei,R.drawable.ustblive_tv_hunan,R.drawable.ustblive_tv_jiangsu,R.drawable.ustblive_tv_liaoning,
                R.drawable.ustblive_tv_shandong,R.drawable.ustblive_tv_shenzhen,R.drawable.ustblive_tv_tianjin,R.drawable.ustblive_tv_zhejiang
        };
        gridView=(GridView)findViewById(R.id.gv_ustblive_showtvDetials);
        gridView.setAdapter(new GridViewAdapter(USTBliveStreamingActivity.this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        path="rtmp://202.204.52.116/iptv/cctv1hd";
                        enterVitamioActivity(path);
                        break;
                    case 1:
                        path="rtmp://202.204.52.116/iptv/cctv3hd";
                        enterVitamioActivity(path);
                        break;
                    case 2:
                        path="rtmp://202.204.52.116/iptv/cctv5hd";
                        enterVitamioActivity(path);
                        break;
                    case 3:
                        path="rtmp://202.204.52.116/iptv/cctv5phd";
                        enterVitamioActivity(path);
                        break;
                    case 4:
                        path="rtmp://202.204.52.116/iptv/cctv6hd";
                        enterVitamioActivity(path);
                        break;
                    case 5:
                        path="rtmp://202.204.52.116/iptv/cctv8hd";
                        enterVitamioActivity(path);
                        break;
                    case 6:
                        path="rtmp://202.204.52.116/iptv/cctv13";
                        enterVitamioActivity(path);
                        break;
                    case 7:
                        path="rtmp://202.204.52.116/iptv/chchd";
                        enterVitamioActivity(path);
                        break;
                    case 8:
                        path="rtmp://202.204.52.116/iptv/btv1hd";
                        enterVitamioActivity(path);
                        break;
                    case 9:
                        path="rtmp://202.204.52.116/iptv/btv2hd";
                        enterVitamioActivity(path);
                        break;
                    case 10:
                        path="rtmp://202.204.52.116/iptv/btv6hd";
                        enterVitamioActivity(path);
                        break;
                    case 11:
                        path="rtmp://202.204.52.116/iptv/btv11hd";
                        enterVitamioActivity(path);
                        break;
                    case 12:
                        path="rtmp://202.204.52.116/iptv/ahhd";
                        enterVitamioActivity(path);
                        break;
                    case 13:
                        path="rtmp://202.204.52.116/iptv/dfhd";
                        enterVitamioActivity(path);
                        break;
                    case 14:
                        path="rtmp://202.204.52.116/iptv/gdhd";
                        enterVitamioActivity(path);
                        break;
                    case 15:
                        path="rtmp://202.204.52.116/iptv/hljhd";
                        enterVitamioActivity(path);
                        break;
                    case 16:
                        path="rtmp://202.204.52.116/iptv/hbhd";
                        enterVitamioActivity(path);
                        break;
                    case 17:
                        path="rtmp://202.204.52.116/iptv/hunanhd";
                        enterVitamioActivity(path);
                        break;
                    case 18:
                        path="rtmp://202.204.52.116/iptv/jshd";
                        enterVitamioActivity(path);
                        break;
                    case 19:
                        path="rtmp://202.204.52.116/iptv/lnhd";
                        enterVitamioActivity(path);
                        break;
                    case 20:
                        path="rtmp://202.204.52.116/iptv/sdhd";
                        enterVitamioActivity(path);
                        break;
                    case 21:
                        path="rtmp://202.204.52.116/iptv/szhd";
                        enterVitamioActivity(path);
                        break;
                    case 22:
                        path="rtmp://202.204.52.116/iptv/tjhd";
                        enterVitamioActivity(path);
                        break;
                    case 23:
                        path="rtmp://202.204.52.116/iptv/zjhd";
                        enterVitamioActivity(path);
                        break;
                }
            }
        });

    }
    class GridViewAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;

        public GridViewAdapter(Context context) {
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return gv_itemnames.length;
        }

        @Override
        public Object getItem(int position) {
            return gv_itemnames[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder myViewHolder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.gv_item_new_edu_sys, null);
                myViewHolder = new MyViewHolder();
                myViewHolder.gvImageView = (ImageView) convertView.findViewById(R.id.item_grid_icon_new);
                myViewHolder.gvTextView = (TextView) convertView.findViewById(R.id.item_grid_name_new);
                convertView.setTag(myViewHolder);
            } else {
                myViewHolder = (MyViewHolder) convertView.getTag();
            }
            myViewHolder.gvImageView.setImageResource(gv_itemImgIds[position]);
            myViewHolder.gvTextView.setText(gv_itemnames[position]);
            return convertView;
        }
    }
    class MyViewHolder {
        public ImageView gvImageView;
        public TextView gvTextView;
    }
    private void enterVitamioActivity(String path){
        Intent intent=new Intent(USTBliveStreamingActivity.this, VitamioLiveActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("path",path);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(USTBliveStreamingActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
