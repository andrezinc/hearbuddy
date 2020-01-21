package com.example.hearbuddy.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.hearbuddy.model.AudioModel;

public class DbHelper extends SQLiteOpenHelper {

    public static int version = 15;
    public static String name = "dbHearbuddy";
    private Context mContext;
    private static com.example.hearbuddy.helper.listeners.OnDatabaseChangedListener mOnDatabaseChangedListener;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TEXT = " INTEGER";
    private static final String COMMA_SEP = ",";


    public static String TABLE_NAME = "tbCronograma";
    public static String ID = "idCronograma";
    public static String TODO = "todo";
    public static String DATE = "date";
    public static String TIME = "time";
    public static String DISCIPLINA = "idCronogramaxDisciplina";

    public static String createQueryCronograma="CREATE TABLE tbCronograma (\n" +
            "    idCronograma INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "    todo TEXT, " +
            "    date TEXT, " +
            "    time TEXT, " +
            "    idCronogramaxDisciplina INTEGER, " +
            " FOREIGN KEY (idCronogramaxDisciplina) REFERENCES tbDisciplina(idDisciplina)"+
            ")";



    public DbHelper(Context context) {
        super(context, name, null, version);
    }

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "tbAudio";
        public static final String COLUMN_NAME_ID = "idAudio";
        public static final String COLUMN_NAME_RECORDING_NAME = "recording_name";
        public static final String COLUMN_NAME_RECORDING_FILE_PATH = "file_path";
        public static final String COLUMN_NAME_RECORDING_LENGTH = "length";
        public static final String COLUMN_NAME_TIME_ADDED = "time_added";
        public static final String COLUMN_NAME_DISCIPLINA = "idAudioxDisciplina";
        private static final String EXTERNAL = " FOREIGN KEY (idAudioxDisciplina) REFERENCES tbDisciplina(idDisciplina)";

    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS tbDisciplina(" +
                    "idDisciplina INTEGER PRIMARY KEY AUTOINCREMENT, nomeDisciplina TEXT NOT NULL)"
                    );
            Log.i("INFODB", "Sucesso Disciplina");
        }catch(Exception e){e.printStackTrace();             Log.i("INFODB", "Erro disciplina"); }

        try {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS tbDocumento(" +
                    "idDocumento INTEGER PRIMARY KEY AUTOINCREMENT, nomeDocumento TEXT NOT NULL, textoDocumento TEXT NOT NULL," +
                    "idDocumentoxDisciplina INTEGER, " +
                    "FOREIGN KEY (idDocumentoxDisciplina) REFERENCES tbDisciplina(idDisciplina))");
            Log.i("INFODB", "Sucesso");
        }catch(Exception e){e.printStackTrace();}


        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(createQueryCronograma);


    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBHelperItem.TABLE_NAME + " (" +
                    DBHelperItem.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_RECORDING_NAME + TEXT_TYPE + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_DISCIPLINA + INTEGER_TEXT + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH + TEXT_TYPE + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_RECORDING_LENGTH + " INTEGER " + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_TIME_ADDED + " INTEGER " +COMMA_SEP+
                    DBHelperItem.EXTERNAL + ")";

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tbDisciplina");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tbDocumento");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tbNuvem");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tbAudio");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tbCronograma");

        this.onCreate(sqLiteDatabase);
    }

    public static void setOnDatabaseChangedListener(com.example.hearbuddy.helper.listeners.OnDatabaseChangedListener listener) {
        mOnDatabaseChangedListener = listener;
    }

    public AudioModel getItemAt(int position, String selection) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DBHelperItem.COLUMN_NAME_ID,
                DBHelperItem.COLUMN_NAME_RECORDING_NAME,
                DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH,
                DBHelperItem.COLUMN_NAME_RECORDING_LENGTH,
                DBHelperItem.COLUMN_NAME_TIME_ADDED,
                DBHelperItem.COLUMN_NAME_DISCIPLINA

        };



        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, selection , null, null, null, null);
        if (c.moveToPosition(position)) {
            AudioModel item = new AudioModel();
                item.setId(c.getInt(c.getColumnIndex(DBHelperItem.COLUMN_NAME_ID)));
                item.setName(c.getString(c.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_NAME)));
                item.setFilePath(c.getString(c.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH)));
                item.setLength(c.getInt(c.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH)));
                item.setTime(c.getLong(c.getColumnIndex(DBHelperItem.COLUMN_NAME_TIME_ADDED)));
                item.setIdDisciplinaAssociada(c.getLong(c.getColumnIndex(DBHelperItem.COLUMN_NAME_DISCIPLINA)));
                c.close();


            return item;

        }
        return null;
    }

    public void removeItemWithId(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = { String.valueOf(id) };
        db.delete(DBHelperItem.TABLE_NAME, "idAudio=?", whereArgs);
    }

    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { DBHelperItem.COLUMN_NAME_ID };
        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public Context getContext() {
        return mContext;
    }



    public long addRecording(String recordingName, String filePath, long length) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, filePath);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH, length);
        cv.put(DBHelperItem.COLUMN_NAME_TIME_ADDED, System.currentTimeMillis());
       // cv.put(DBHelperItem.COLUMN_NAME_DISCIPLINA, disciplinaModel.getId());
        long rowId = db.insert(DBHelperItem.TABLE_NAME, null, cv);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewDatabaseEntryAdded();
        }

        return rowId;
    }

    public void renameItem(AudioModel item, String recordingName, String filePath) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, filePath);
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_ID + "=" + item.getId(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onDatabaseEntryRenamed();
        }
    }


    public static long insert (SQLiteDatabase db, ContentValues cv) {
        return db.insert(TABLE_NAME, null, cv);
    }
    public static Cursor select(SQLiteDatabase db,String selection) {
        return db.query(TABLE_NAME, null, selection, null, null, null, null, null);
    }
    public static int delete(SQLiteDatabase db,String whereClause) {
        return db.delete(TABLE_NAME, whereClause, null);
    }
    public static int update(SQLiteDatabase db,String whereClause,ContentValues cv) {
        return db.update(TABLE_NAME,cv,whereClause,null);
    }




}


