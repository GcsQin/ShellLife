package com.ustb.shellbox.shelllife.new_edu_sys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.iactivity.INewEduSysActivity;

public class TeachPlanActivity extends BaseSetupWizardActivity {
    Bundle bundle;
    String bundleString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach_plan);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        bundleString=bundle.getString("uid");
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        Intent intent=new Intent(TeachPlanActivity.this, INewEduSysActivity.class);
        Bundle bundleBack=new Bundle();
        bundleBack.putString("uid",bundle.getString("uid"));
        intent.putExtras(bundleBack);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void enterMustCourseActivity(View view){
        Intent intent=new Intent(TeachPlanActivity.this, MustCourseActivity.class);
        Bundle bundleInside=new Bundle();
        bundleInside.putString("uid",bundleString);
        intent.putExtras(bundleInside);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    public void enterSpecialCourseActivity(View view){
        Intent intent=new Intent(TeachPlanActivity.this, SpecialCourseActivity.class);
        Bundle bundleInside=new Bundle();
        bundleInside.putString("uid",bundleString);
        intent.putExtras(bundleInside);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    public void enterPublicCourseActivity(View view){
        Intent intent=new Intent(TeachPlanActivity.this, PublicCourseActivity.class);
        Bundle bundleInside=new Bundle();
        bundleInside.putString("uid",bundleString);
        intent.putExtras(bundleInside);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(TeachPlanActivity.this, INewEduSysActivity.class);
            Bundle bundleBack=new Bundle();
            bundleBack.putString("uid",bundle.getString("uid"));
            intent.putExtras(bundleBack);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
