package com.ustb.shellbox.shelllife.alert_act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.more_wonderful.USTBliveStreamingActivity;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VitamioLiveActivity extends AppCompatActivity {
    private VideoView videoView;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        path=bundle.getString("path");
        //检查Vitamio框架是否可以使用
        if(!LibsChecker.checkVitamioLibs(VitamioLiveActivity.this)){
            return;
        }

        setContentView(R.layout.activity_vitamio_live);
        //初始化加载库文件
        if(Vitamio.isInitialized(this)){
            videoView=(VideoView)findViewById(R.id.vitamio);
            videoView.setVideoPath(path);
            videoView.setMediaController(new MediaController(VitamioLiveActivity.this));
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setPlaybackSpeed(1.0f);
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(VitamioLiveActivity.this, USTBliveStreamingActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
