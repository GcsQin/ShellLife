package com.ustb.shellbox.shelllife.new_edu_sys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.ustb.shellbox.shelllife.R;

import okhttp3.FormBody;

public class CourseListActivity extends AppCompatActivity {
    Bundle bundle;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        spinner=(Spinner)findViewById(R.id.spinner_choice_data);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    private void  searchCourseList(){
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("uid",bundle.getString("uid"));

    }
}
