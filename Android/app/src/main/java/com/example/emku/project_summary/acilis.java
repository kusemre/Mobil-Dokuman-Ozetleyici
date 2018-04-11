package com.example.emku.project_summary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class acilis extends AppCompatActivity {
    public static Boolean server_status = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis);

        Animasyon_Calistir calis = new Animasyon_Calistir();
        calis.execute(4000);
        ImageView img = (ImageView) findViewById(R.id.img_acilis);
        Animation anim = AnimationUtils.loadAnimation(acilis.this, R.anim.anim_start);
        img.startAnimation(anim);

    }

    public class Animasyon_Calistir extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... ıntegers) {
            try {
                Thread.sleep(ıntegers[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Intent in = new Intent(acilis.this, anasayfa.class);
                startActivity(in);
                finish();
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onStop();
    }
}

