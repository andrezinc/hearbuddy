package com.example.hearbuddy;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hearbuddy.adapter.AdaptadorLembrete;
import com.example.hearbuddy.helper.DbHelper;
import com.example.hearbuddy.model.CronogramaModel;
import com.example.hearbuddy.model.DisciplinaModel;

import java.util.ArrayList;
import java.util.Calendar;


public class CronogramaDisciplina extends AppCompatActivity {

    public static boolean playing=false;
    Button buttonStop;
    TextView textViewBack;
    Button buttonDate, buttonTime;
    EditText editTextAbout;
    ImageButton imageButtonAdd;
    ListView listView;
    String timeET,dateET,todoET;
    Long disciplinaET;
    AdaptadorLembrete arrayAdapter;
    ArrayList<CronogramaModel> arrayListRem= new ArrayList<>();
    ArrayList<Integer> idArrayList= new ArrayList<>();
    AlarmManager alarmManager;
    DisciplinaModel disciplinaRecebida;
    boolean flagDeleteAlarm = false;
    boolean flagEditAlarm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronograma_disciplina);

        disciplinaRecebida = (DisciplinaModel) getIntent().getSerializableExtra("disciplinaSelecionada");


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        init();

        setListeners();

        fetchDatabaseToArrayList();

        stopAlarm();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView);
        imageButtonAdd = (ImageButton) findViewById(R.id.imageButtonAdd);
        editTextAbout = (EditText) findViewById(R.id.editTextAbout);
        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonTime = (Button) findViewById(R.id.buttonTime);
        textViewBack = (TextView) findViewById(R.id.textViewBack);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonStop.setVisibility(View.INVISIBLE);
        textViewBack.setVisibility(View.INVISIBLE);
    }


    private void setListeners() {
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
                    Toast.makeText(CronogramaDisciplina.this, "Adicionar nome", Toast.LENGTH_SHORT).show();
                }
                else if(textDate.equals("Selecione a data")) {
                    Toast.makeText(CronogramaDisciplina.this, "Adicionar data", Toast.LENGTH_SHORT).show();
                }
                else if(textTime.equals("Selecione a hora")) {
                    Toast.makeText(CronogramaDisciplina.this, "Adicionar hora", Toast.LENGTH_SHORT).show();
                }
                else {
                    long id=insertDatabase();
                    setAlarm(id);
                    fetchDatabaseToArrayList();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showListDialog(position);
            }
        });
    }

    private long insertDatabase() {

        DbHelper helper = new DbHelper(CronogramaDisciplina.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(helper.TODO, todoET);
        cv.put(helper.DATE, dateET);
        cv.put(helper.TIME, timeET);
        cv.put(helper.DISCIPLINA, disciplinaRecebida.getId());

        long id = helper.insert(db, cv);
        if (id>0) {
            Toast.makeText(CronogramaDisciplina.this, "Lembrete adicionado", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(CronogramaDisciplina.this, "Tente novamente", Toast.LENGTH_SHORT).show();
        }
        db.close();

        editTextAbout.setText("");
        buttonTime.setText("Selecione a hora");
        buttonDate.setText("Selecione a data");
        return id;
    }

    private void setAlarm(long id) {
        DbHelper helper = new DbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection = DbHelper.ID +" = '"+id+"'";
        Cursor cursor = helper.select(db,selection);
        String dateET[]=new String[3];
        String timeET[]=new String[2];
        String date = null,time=null,name = null;

        if(cursor!=null) {
            while(cursor.moveToNext()) {
                date = cursor.getString(2);
                time = cursor.getString(3);
                name = cursor.getString(1);
            }
        }

        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent i = new Intent();
        i.setAction("i.love.myself");
        i.putExtra("ID",id);
        i.putExtra("TODO",name);
        i.putExtra("DATE",date);
        i.putExtra("TIME",time);
        i.putExtra("DISCIPLINA",disciplinaRecebida.getId());

        PendingIntent pendingIntent= PendingIntent.getBroadcast(this,(int)id,i,0);

        if(flagDeleteAlarm==true) {
            alarmManager.cancel(pendingIntent);
            flagDeleteAlarm=false;
        }
        else {
            int k=0;
            for(String s: date.split("-")) {
                dateET[k++]=s;
            }
            k=0;
            for(String s: time.split(":")) {
                timeET[k++]=s;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(dateET[2]),Integer.parseInt(dateET[1])-1,Integer.parseInt(dateET[0]),Integer.parseInt(timeET[0]),Integer.parseInt(timeET[1]));
            long mili = calendar.getTimeInMillis();
            calendar.setTimeInMillis(mili);

            Calendar calendarCurrent = Calendar.getInstance();
            long miliCurrent = calendarCurrent.getTimeInMillis();
            calendarCurrent.setTimeInMillis(miliCurrent);
            long diff = mili - miliCurrent;

            long currentTime = System.currentTimeMillis();
            alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + diff, pendingIntent);
            diff=diff/1000;
            Toast.makeText(this, "Alarme definido para " +diff+" segundos" , Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchDatabaseToArrayList() {
        arrayListRem.clear();
        idArrayList.clear();
        DbHelper mySqliteOpenHelper = new DbHelper(this);
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        int k=0;

        String selection = DbHelper.DISCIPLINA +" = '"+disciplinaRecebida.getId()+"'";
        Cursor cursor = DbHelper.select(db, selection );
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
                todopojo.setIdCronogramaxDisciplina(disciplinaRecebida.getId());
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

            arrayAdapter = new AdaptadorLembrete(CronogramaDisciplina.this, R.layout.listview_back, arrayListRem);
            listView.setAdapter(arrayAdapter);
        }

    }


    private void showCalendar() {
        Calendar calendar= Calendar.getInstance();
        final int date=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog=new DatePickerDialog(CronogramaDisciplina.this, new DatePickerDialog.OnDateSetListener() {
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
        TimePickerDialog timePickerDialog=new TimePickerDialog(CronogramaDisciplina.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                buttonTime.setText(""+hourOfDay+":"+minute);
                timeET=""+hourOfDay+":"+minute;
            }
        },hour,minute,true);
        timePickerDialog.show();
    }
    private void showListDialog(final int pos) {

        String[] arr = {"Deletar","Editar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CronogramaDisciplina.this);
        builder.setTitle("Opções");
        builder.setItems(arr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                if(position==0) {
                    showDelDialog(pos);
                }
                else {
                    int id = idArrayList.get(pos);
                    Intent i=new Intent(CronogramaDisciplina.this, CronogramaEditar.class);
                    i.putExtra("ID",id);
                    flagEditAlarm=true;
                    startActivity(i);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    public void showDelDialog(final int position) {
        AlertDialog.Builder builder= new AlertDialog.Builder(CronogramaDisciplina.this);
        builder.setTitle("Confirmação");
        builder.setMessage("Deseja apagar esta data?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DbHelper helper = new DbHelper(CronogramaDisciplina.this);
                SQLiteDatabase db=helper.getWritableDatabase();
                int id = idArrayList.get(position);
                String whereCluase = DbHelper.ID +" = '"+id+"'";

                int flag = DbHelper.delete(db, whereCluase);
                if (flag > 0) {
                    Toast.makeText(CronogramaDisciplina.this, "Apagado", Toast.LENGTH_SHORT).show();
                    arrayListRem.clear();
                    idArrayList.clear();
                    fetchDatabaseToArrayList();
                    arrayAdapter.notifyDataSetChanged();
                    flagDeleteAlarm = true;
                    setAlarm(id);
                }
                else {
                    Toast.makeText(CronogramaDisciplina.this, "Não foi possível apagar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNeutralButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog deleteDialog=builder.create();
        deleteDialog.show();
    }


    private void stopAlarm() {
        if(playing==true) {
            textViewBack.setVisibility(View.VISIBLE);
            buttonStop.setVisibility(View.VISIBLE);
            buttonStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CronogramaAlarme.player.pause();
                    textViewBack.setVisibility(View.INVISIBLE);
                    buttonStop.setVisibility(View.INVISIBLE);
                    playing=false;
                }
            });
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(flagEditAlarm==true) {
            fetchDatabaseToArrayList();
            long id= CronogramaEditar.idUpdate;
            setAlarm(id);
            flagEditAlarm=false;
        }
    }
}
