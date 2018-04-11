package com.example.emku.project_summary;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class anasayfa extends AppCompatActivity implements dosya_sec.OnFragmentInteractionListener,veri_gir.OnFragmentInteractionListener {

    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Sekme bağlantısı yapıldı.Dosya seç ve veri gir başlıklı sekme eklendi.
        TabLayout tablayout = (TabLayout)findViewById(R.id.tabs);
        tablayout.addTab(tablayout.newTab().setText("DOSYA SEÇ"));
        tablayout.addTab(tablayout.newTab().setText("VERİ GİR"));

        // Xml dosyasındaki viewpager ile bağlantıyı yaptık
        final ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tablayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        //Sekmelerden biri seçildiğinde yapılan işlemler
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //Sekme seçildiğinde yapılacak işlemler
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Sekme seçilmediğinde yapılacak işlemler
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Aynı sekme 2 kere veya daha fazla seçildiğinde yapılacak işlemler
            }
        });
    }

    // Çıkış
    @Override
    public void finish() {
        AlertDialog.Builder alert_close = new AlertDialog.Builder(anasayfa.this);
        alert_close.setTitle("Uyarı");
        alert_close.setMessage("Uygulamadan çıkmak istediğinize emin misiniz?");

        alert_close.setPositiveButton("Evet",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        System.exit(1);
                    }
                });

        alert_close.setNegativeButton("Hayır",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        alert_close.show();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}




