package com.example.hearbuddy.ui;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hearbuddy.R;
import com.example.hearbuddy.adapter.AdaptadorLembrete;
import com.example.hearbuddy.helper.DbHelper;
import com.example.hearbuddy.model.CronogramaModel;

import java.util.ArrayList;


public class CronogramaHome extends AppCompatActivity {


    private ListView listView;

    private AdaptadorLembrete arrayAdapter;
    private final ArrayList<CronogramaModel> arrayListRem= new ArrayList<>();
    private final ArrayList<Integer> idArrayList= new ArrayList<>();

    private boolean flagEditAlarm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronograma_home);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();
        fetchDatabaseToArrayList();
    }


        private void init () {
            listView = findViewById(R.id.listView);
        }

// --Commented out by Inspection START (21/01/2020 14:51):
//    private void setListeners() {
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//            }
//        });
//    }
// --Commented out by Inspection STOP (21/01/2020 14:51)

    private void fetchDatabaseToArrayList() {
        arrayListRem.clear();
        idArrayList.clear();
        DbHelper mySqliteOpenHelper = new DbHelper(this);
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        int k=0;

        Cursor cursor = DbHelper.select(db, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String todo = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                char alpha=todo.charAt(0);
                char alphaU=Character.toUpperCase(alpha);

                CronogramaModel todopojo = new CronogramaModel();
                todopojo.setName(todo);
                todopojo.setDate(date);
                todopojo.setTime(time);
                todopojo.setAlpha(""+alphaU);
                if(k%2==0) {
                    todopojo.setImageRes(R.drawable.redback);
                    k++;
                }
                else {
                    todopojo.setImageRes(R.drawable.redback2);
                    k++;
                }
                arrayListRem.add(todopojo);
                idArrayList.add(id);
            }
            cursor.close();
            db.close();

            arrayAdapter = new AdaptadorLembrete(CronogramaHome.this, R.layout.listview_back, arrayListRem);
            listView.setAdapter(arrayAdapter);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(flagEditAlarm) {
            fetchDatabaseToArrayList();
            long id= CronogramaEditar.idUpdate;

            flagEditAlarm=false;
        }
    }
}
