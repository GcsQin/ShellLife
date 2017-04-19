package com.ustb.shellbox.shelllife.iactivity;

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
import com.ustb.shellbox.shelllife.new_edu_sys.CourseListActivity;
import com.ustb.shellbox.shelllife.new_edu_sys.CourseScoreActivity;
import com.ustb.shellbox.shelllife.new_edu_sys.CxxfCreditsActivity;
import com.ustb.shellbox.shelllife.new_edu_sys.GermanEnglishActivity;
import com.ustb.shellbox.shelllife.new_edu_sys.TeachPlanActivity;


public class INewEduSysActivity extends BaseSetupWizardActivity {
    private int[] gvIconIdArray;
    private String[] gvItemName;
    private Bundle bundle;
    private String bundleString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edu_sys);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initData();
        Intent intent=getIntent();
        bundle=intent.getExtras();
        bundleString=bundle.getString("uid");
        initGridView();
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        startActivity(new Intent(INewEduSysActivity.this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initData(){
        gvItemName=getResources().getStringArray(R.array.newedusys_act_gridview_itemname);
        gvIconIdArray = new int[]{R.drawable.star_48px, R.drawable.acheivement_48px,
                R.drawable.teach_plan_48px, R.drawable.german_english_48px,};

    }
    private void initGridView() {
        GridView gridView = (GridView) findViewById(R.id.new_edu_sys_gv);
        gridView.setAdapter(new GridViewAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        EnterCxxfCreditsActivity();
                        break;
                    case 1:
                        EnterCourseScoreActivity();
                        break;
                    case 2:
                        EnterTeachPlanActivity();
                        break;
                    case 3:
                        EnterCetActivity();
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
            return gvItemName.length;
        }

        @Override
        public Object getItem(int position) {
            return gvItemName[position];
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
            myViewHolder.gvImageView.setImageResource(gvIconIdArray[position]);
            myViewHolder.gvTextView.setText(gvItemName[position]);
            return convertView;
        }
    }
    class MyViewHolder {
        public ImageView gvImageView;
        public TextView gvTextView;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(INewEduSysActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    //itemFunction
    private void EnterCxxfCreditsActivity(){
        EnterTagerActivity(CxxfCreditsActivity.class);
    }
    private void EnterCourseScoreActivity(){
        EnterTagerActivity(CourseScoreActivity.class);
    }
    private void EnterTeachPlanActivity(){
        EnterTagerActivity(TeachPlanActivity.class);
    }
    private void EnterCourseListActivity(){
        EnterTagerActivity(CourseListActivity.class);
    }
    private  void EnterCetActivity(){
        EnterTagerActivity(GermanEnglishActivity.class);
    }
    private void EnterTagerActivity(Class<?> cls ){
        Intent intent=new Intent(INewEduSysActivity.this, cls);
        Bundle bundle=new Bundle();
        bundle.putString("uid",bundleString);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
