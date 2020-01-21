package com.example.hearbuddy.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.example.hearbuddy.R;
import com.example.hearbuddy.fragment.GravarFragment;
import com.example.hearbuddy.fragment.ListaFragment;


public class GravadorAudio extends AppCompatActivity {

    // --Commented out by Inspection (21/01/2020 14:51):private static final String LOG_TAG = GravadorAudio.class.getSimpleName();

    // --Commented out by Inspection (21/01/2020 14:51):private DisciplinaModel disciplinaAtual;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearbuddy_audio);

        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabs = findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);

       // disciplinaAtual = (DisciplinaModel) getIntent().getSerializableExtra("disciplinaSelecionada");
       // Log.i("Dabber", disciplinaAtual.getNomeDisciplina());



    }


    class MyAdapter extends FragmentPagerAdapter {
        private final String[] titles = { getString(R.string.tab_title_record),
                getString(R.string.tab_title_saved_recordings) };

        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:{
                    return GravarFragment.newInstance(position, null);
                }
                case 1:{
                    return ListaFragment.newInstance(position, null);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    public GravadorAudio() {
    }
}
