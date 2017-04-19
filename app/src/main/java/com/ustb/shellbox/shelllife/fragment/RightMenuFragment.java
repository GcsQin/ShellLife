package com.ustb.shellbox.shelllife.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.utils.DataCleanManager;

/**
 * Created by 37266 on 2016/12/22.
 */
public class RightMenuFragment extends Fragment {
    private  View rightMenuView;
    TextView tv_clearCache;
    TextView tv_clearAccount;
//    TextView tv_lockAct;
    TextView tv_useTips;
    TextView tv_aboutUs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rightMenuView==null){
            initView(inflater,container);
        }
        return rightMenuView;
    }

    private void initView(LayoutInflater inflater,ViewGroup container){
        rightMenuView=inflater.inflate(R.layout.layout_rightmenu,container,false);
        tv_clearCache=(TextView)rightMenuView.findViewById(R.id.tv_clearCache);
        tv_clearAccount=(TextView)rightMenuView.findViewById(R.id.tv_clearAccount);
//        tv_lockAct=(TextView)rightMenuView.findViewById(R.id.tv_lockAct);
        tv_useTips=(TextView)rightMenuView.findViewById(R.id.tv_useTips);
        tv_aboutUs=(TextView)rightMenuView.findViewById(R.id.tv_aboutUs);
        tv_clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCache();
            }
        });
        tv_clearAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAccount();
            }
        });
        tv_useTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useTips();
            }
        });
        tv_aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutUS();
            }
        });
    }
//    private void viewOnclickFunction(){
//        tv_clearCache.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
//                Dialog dialog=builder.create();
////                dialog.
//                clearCache();
//            }
//        });
//        tv_clearAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clearAccount();
//            }
//        });
//        tv_lockAct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//    }
    private void clearCache(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        AlertDialog dialog=builder.create();
        dialog.setTitle("提示");
        dialog.setMessage("你确定要清除所有缓存吗?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataCleanManager.cleanInternetCache(getContext());
                DataCleanManager.cleanExternalCache(getContext());
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }
    private void clearAccount(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        AlertDialog dialog=builder.create();
        dialog.setTitle("提示");
        dialog.setMessage("你确定要清除所有用户信息吗?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences=getContext().getSharedPreferences("AccountInfo",Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }
    private void useTips(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        final AlertDialog dialog=builder.create();
        View dialogView=View.inflate(getContext(),R.layout.layout_custom_dialog_usetips,null);
        ImageButton imageButton=(ImageButton)dialogView.findViewById(R.id.imgbtn_cancle_right);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
    }
    private void aboutUS(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        final AlertDialog dialog=builder.create();
        View dialogView=View.inflate(getContext(),R.layout.layout_custom_dialog_about_us,null);
        ImageButton imageButton=(ImageButton)dialogView.findViewById(R.id.imgbtn_cancle_right_us);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
    }
}
