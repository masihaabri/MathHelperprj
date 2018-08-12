package com.a000webhostapp.mathhelperapp.www.mathhelperprj;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.afollestad.materialdialogs.MaterialDialog;

public class Video extends AppCompatActivity {
    MediaController mc;
    MaterialDialog md;
    String videourl;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getSupportActionBar().hide();
        Intent i=getIntent();
        videourl=i.getStringExtra("linkk");
        videoView=findViewById(R.id.video);
        md=new MaterialDialog.Builder(Video.this)
                .content(R.string.content)
                .cancelable(true)
                .progress(true,0)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();
                    }
                })
                .show();
        try {
            mc=new MediaController(Video.this);
            mc.setAnchorView(videoView);
            Uri v=Uri.parse(videourl);
            videoView.setMediaController(mc);
            videoView.setVideoURI(v);
        }catch (Exception ex){
            Log.e("Error",ex.getMessage());
            ex.printStackTrace();
        }
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                md.dismiss();
                videoView.start();
            }
        });
    }
}
