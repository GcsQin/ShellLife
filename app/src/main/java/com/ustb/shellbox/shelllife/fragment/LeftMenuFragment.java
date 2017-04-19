package com.ustb.shellbox.shelllife.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.customview.DividerItemDecoration;
import com.ustb.shellbox.shelllife.databean.AccountInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LeftMenuFragment extends Fragment {
    private View leftMenuView;
    private RecyclerView recyclerView;
    private List<AccountInfo> accountInfoList=new ArrayList<AccountInfo>();
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("initView","view");
        if(leftMenuView==null ){
            initView(inflater,container);
        }
        return leftMenuView;
    }
    private void initView(LayoutInflater inflater,ViewGroup container){
        leftMenuView=inflater.inflate(R.layout.layout_leftmenu,container,false);
        recyclerView=(RecyclerView)leftMenuView.findViewById(R.id.left_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ListCycleAdapter listCycleAdapter =   new ListCycleAdapter();
        recyclerView.setAdapter(listCycleAdapter);
//        listCycleAdapter.setOnItemClickListener(new onRecycleViwItemClickListener() {
//            @Override
//            public void onItemClick(final View view, final AccountInfo tag) {
//                AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
//                final AlertDialog dialog=builder.create();
//                final View viewDialog=View.inflate(getContext(), R.layout.layout_custom_dialog_login, null);
//                final TextView textView=(TextView)viewDialog.findViewById(R.id.tv_title_dialog);
//                textView.setText(tag.getWebName());
//                Button button=(Button)viewDialog.findViewById(R.id.btn_saveInfo);
//                dialog.setView(viewDialog);
//                dialog.show();
//                button.setOnClickListener(new View.OnClickListener() {
//                    EditText text_dialog_ac=(EditText)viewDialog.findViewById(R.id.login_username);
//                    EditText text_dialog_pw=(EditText)viewDialog.findViewById(R.id.login_password);
//                    TextView editText_ac=(TextView)view.findViewById(R.id.et_account_item_leftmenu);
//                    TextView editText_pw=(TextView)view.findViewById(R.id.et_password_item_leftmenu);
//                    @Override
//                    public void onClick(View v) {
//                        switch (tag.getAccountType()){
//                            case 1:
//                                String user_edu=text_dialog_ac.getText().toString().trim();
//                                String pass_edu=text_dialog_pw.getText().toString().trim();
//                                sharedPreferences.edit().putString("eduUser",user_edu).commit();
//                                sharedPreferences.edit().putString("eduPass",pass_edu).commit();
//                                tag.setUserName(user_edu);
//                                tag.setPassword(pass_edu);
//                                editText_ac.setText(tag.getUserName());
//                                editText_pw.setText(tag.getPassword());
//                                dialog.dismiss();
//                                break;
//                            case 2:
//                                String user_zyz=text_dialog_ac.getText().toString().trim();
//                                String pass_zyz=text_dialog_pw.getText().toString().trim();
//                                sharedPreferences.edit().putString("zyzUser",user_zyz).commit();
//                                sharedPreferences.edit().putString("zyzPass",pass_zyz).commit();
//                                tag.setUserName(user_zyz);
//                                tag.setPassword(pass_zyz);
//                                editText_ac.setText(tag.getUserName());
//                                editText_pw.setText(tag.getPassword());
//                                dialog.dismiss();
//                                break;
//                            case 3:
//                                String user_net=text_dialog_ac.getText().toString().trim();
//                                String pass_net=text_dialog_pw.getText().toString().trim();
//                                sharedPreferences.edit().putString("netUser",user_net).commit();
//                                sharedPreferences.edit().putString("netPass",pass_net).commit();
//                                tag.setUserName(user_net);
//                                tag.setPassword(pass_net);
//                                editText_ac.setText(tag.getUserName());
//                                editText_pw.setText(tag.getPassword());
//                                dialog.dismiss();
//                                break;
//                            case 4:
//                                String user_lib=text_dialog_ac.getText().toString().trim();
//                                String pass_lib=text_dialog_pw.getText().toString().trim();
//                                sharedPreferences.edit().putString("libUser",user_lib).commit();
//                                sharedPreferences.edit().putString("libPass",pass_lib).commit();
//                                tag.setUserName(user_lib);
//                                tag.setPassword(pass_lib);
//                                editText_ac.setText(tag.getUserName());
//                                editText_pw.setText(tag.getPassword());
//                                dialog.dismiss();
//                                break;
//                            case 5:
//                                String user_eustb=text_dialog_ac.getText().toString().trim();
//                                String pass_eustb=text_dialog_pw.getText().toString().trim();
//                                sharedPreferences.edit().putString("eustbUser",user_eustb).commit();
//                                sharedPreferences.edit().putString("eustbPass",pass_eustb).commit();
//                                tag.setUserName(user_eustb);
//                                tag.setPassword(pass_eustb);
//                                editText_ac.setText(tag.getUserName());
//                                editText_pw.setText(tag.getPassword());
//                                dialog.dismiss();
//                                break;
//                        }
//
//                    }
//                });
//            }
//        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("oncreate","....create");
        initData();
    }
    private void  initData(){
        sharedPreferences=getContext().getSharedPreferences("AccountInfo", Context.MODE_PRIVATE);
        String[] webNames=new String[]{"新版教务系统","志愿服务中心","校园上网认证","图书馆","信息门户/职员服务"};
        String eduUser=sharedPreferences.getString("eduUser","");
        String eduPass=sharedPreferences.getString("eduPass","");
        Log.e("edu",eduUser+"#"+eduPass);
        String zyzUser=sharedPreferences.getString("zyzUser","");
        String zyzPass=sharedPreferences.getString("zyzPass","");
        Log.e("zyz",zyzUser+"#"+zyzPass);
        String netUser=sharedPreferences.getString("netUser","");
        String netPass=sharedPreferences.getString("netPass","");
        Log.e("net",netUser+"#"+netPass);
        String libUser=sharedPreferences.getString("libUser","");
        String libPass=sharedPreferences.getString("libPass","");
        Log.e("lib",libUser+"#"+libPass);
        String eustbUser=sharedPreferences.getString("eustbUser","");
        String eustbPass=sharedPreferences.getString("eustbPass","");
        Log.e("eustb",eustbUser+"#"+eustbPass);
        String[] userNames=new String[]{eduUser,zyzUser,netUser,libUser,eustbUser};
        String[] passWord=new String[]{eduPass,zyzPass,netPass,libPass,eduPass};
        int[] accountType=new int[]{1,2,3,4,5};
        for (int i=0;i<webNames.length;i++){
            AccountInfo accountInfo=new AccountInfo(webNames[i],userNames[i],passWord[i],accountType[i],
                    R.drawable.web_iocn,R.drawable.un_leftmenu,R.drawable.pw_leftmenu,R.drawable.info_leftmenu);
            if(accountInfo!=null) {
                accountInfoList.add(accountInfo);
            }else {

            }
        }
    }
    public class ListCycleAdapter extends RecyclerView.Adapter<ListCycleAdapter.MyViewHolder>{
        private onRecycleViwItemClickListener recycleViwItemClickListener=null;
        //创建childView
        @Override
        public ListCycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //获取item的布局
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leftmenu_list_recycler,parent,false);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (recycleViwItemClickListener != null) {
//                        recycleViwItemClickListener.onItemClick(v, (AccountInfo) v.getTag());
//                    }
//                }
//            });
            MyViewHolder holder= new MyViewHolder(view);
            return holder;
        }
        //将数据绑定到一个childView中
        @Override
        public void onBindViewHolder(ListCycleAdapter.MyViewHolder holder, int position) {
            holder.textViewWeb.setText(accountInfoList.get(position).getWebName());
            holder.textViewWeb.getPaint().setFakeBoldText(true);//中文设置粗体
            holder.editTextAccount.setText(accountInfoList.get(position).getUserName());
            holder.editTextPassword.setText(accountInfoList.get(position).getPassword());
            holder.itemView.setTag(accountInfoList.get(position));

        }
        //得到child的数量
        @Override
        public int getItemCount() {
            return accountInfoList.size();
        }
        //通过holder的方式初始化每一个childView
        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView textViewWeb;
            TextView editTextAccount;
            TextView editTextPassword;
            public MyViewHolder(View itemView) {
                super(itemView);
                textViewWeb=(TextView) itemView.findViewById(R.id.tv_web_item_leftmenu);
                editTextAccount=(TextView) itemView.findViewById(R.id.et_account_item_leftmenu);
                editTextPassword=(TextView) itemView.findViewById(R.id.et_password_item_leftmenu);
            }
        }
        //自定义的child点击事件。
        public void setOnItemClickListener(onRecycleViwItemClickListener listener){
            this.recycleViwItemClickListener=listener;
        }
    }
    public  interface onRecycleViwItemClickListener{
        void onItemClick(View view,AccountInfo tag);
    }

}
