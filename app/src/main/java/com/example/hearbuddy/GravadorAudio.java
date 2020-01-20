package com.example.hearbuddy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;
import com.example.hearbuddy.model.DisciplinaModel;
import com.example.hearbuddy.fragments.ListaFragment;
import com.example.hearbuddy.fragments.GravarFragment;


public class GravadorAudio extends AppCompatActivity {

    private static final String LOG_TAG = GravadorAudio.class.getSimpleName();

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private DisciplinaModel disciplinaAtual;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearbuddy_audio);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);

       // disciplinaAtual = (DisciplinaModel) getIntent().getSerializableExtra("disciplinaSelecionada");
       // Log.i("Dabber", disciplinaAtual.getNomeDisciplina());



    }


    public class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = { getString(R.string.tab_title_record),
                getString(R.string.tab_title_saved_recordings) };

        public MyAdapter(FragmentManager fm) {
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
