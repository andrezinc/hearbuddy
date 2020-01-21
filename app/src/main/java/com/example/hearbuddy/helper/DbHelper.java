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

    private static final int version = 15;
    private static final String name = "dbHearbuddy";
    // --Commented out by Inspection (21/01/2020 14:51):private Context mContext;
    private static com.example.hearbuddy.helper.listeners.OnDatabaseChangedListener mOnDatabaseChangedListener;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TEXT = " INTEGER";
    private static final String COMMA_SEP = ",";


    private static final String TABLE_NAME = "tbCronograma";
    public static final String ID = "idCronograma";
    public static final String TODO = "todo";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String DISCIPLINA = "idCronogramaxDisciplina";

    private static final String createQueryCronograma="CREATE TABLE tbCronograma (\n" +
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

    static abstract class DBHelperItem implements BaseColumns {
        static final String TABLE_NAME = "tbAudio";
        static final String COLUMN_NAME_ID = "idAudio";
        static final String COLUMN_NAME_RECORDING_NAME = "recording_name";
        static final String COLUMN_NAME_RECORDING_FILE_PATH = "file_path";
        static final String COLUMN_NAME_RECORDING_LENGTH = "length";
        static final String COLUMN_NAME_TIME_ADDED = "time_added";
        static final String COLUMN_NAME_DISCIPLINA = "idAudioxDisciplina";
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

// --Commented out by Inspection START (21/01/2020 14:51):
//    public Context getContext() {
//        return mContext;
//    }
// --Commented out by Inspection STOP (21/01/2020 14:51)



    public void addRecording(String recordingName, String filePath, long length) {

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


