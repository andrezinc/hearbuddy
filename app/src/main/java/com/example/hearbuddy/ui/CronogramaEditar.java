package com.example.hearbuddy.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hearbuddy.R;
import com.example.hearbuddy.helper.DbHelper;

import java.util.Calendar;




public class CronogramaEditar extends AppCompatActivity {
    private Button buttonDate;
    private Button buttonTime;
    private EditText editTextAbout;
    private ImageButton imageButtonAdd;
    private String timeET;
    private String dateET;
    private String todoET;
    static long idUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        init();

        Intent i=getIntent();
        int id=i.getIntExtra("ID",0);

        DbHelper mySqliteOpenHelper = new DbHelper(this);
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor = DbHelper.select(db, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idData = cursor.getInt(0);
                if (idData == id) {
                    todoET = cursor.getString(1);
                    dateET = cursor.getString(2);
                    timeET = cursor.getString(3);
                    break;
                }
            }
            cursor.close();
            db.close();
        }
        editTextAbout.setText(todoET);
        buttonDate.setText(dateET);
        buttonTime.setText(timeET);
        setListeners(id);
    }


    private void init() {
        imageButtonAdd = findViewById(R.id.imageButtonAddU);
        editTextAbout = findViewById(R.id.editTextAboutU);
        buttonDate = findViewById(R.id.buttonDateU);
        buttonTime = findViewById(R.id.buttonTimeU);
    }


    private void setListeners(final int id) {
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClocK();
            }
        });
        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoET = editTextAbout.getText().toString();
                String textTime=buttonTime.getText().toString();
                String textDate=buttonDate.getText().toString();
                if(todoET.equals("")) {
                    Toast.makeText(CronogramaEditar.this, "Adicionar nome", Toast.LENGTH_SHORT).show();
                }
                else if(textDate.equals("Selecione a data") || textDate.equals("")) {
                    Toast.makeText(CronogramaEditar.this, "Adicionar data", Toast.LENGTH_SHORT).show();
                }
                else if(textTime.equals("Selecione a hora") || textTime.equals("")) {
                    Toast.makeText(CronogramaEditar.this, "Adicionar hora", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateDatabase(id);
                }
            }
        });
    }

    private void updateDatabase(int id) {


        DbHelper helper = new DbHelper(CronogramaEditar.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.TODO, todoET);
        cv.put(DbHelper.DATE, dateET);
        cv.put(DbHelper.TIME, timeET);

        String whereClause = DbHelper.ID +" = '"+id+"'";
        int l = DbHelper.update(db, whereClause,cv);
        if (l>0) {
            Toast.makeText(CronogramaEditar.this, "Atualizado", Toast.LENGTH_SHORT).show();
            idUpdate=id;
            finish();
        }
        else {
            Toast.makeText(CronogramaEditar.this, "Tente novamente", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    private void showCalendar() {
        Calendar calendar= Calendar.getInstance();
        final int date=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog=new DatePickerDialog(CronogramaEditar.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                buttonDate.setText(""+dayOfMonth+"-"+month+"-"+year);
                dateET=""+dayOfMonth+"-"+month+"-"+year;
            }
        },year,month,date);
        datePickerDialog.show();
    }
    private void showClocK() {
        Calendar calendar= Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog=new TimePickerDialog(CronogramaEditar.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                buttonTime.setText(""+hourOfDay+":"+minute);
                timeET=""+hourOfDay+":"+minute;
            }
        },hour,minute,true);
        timePickerDialog.show();
    }
}

