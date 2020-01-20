package com.example.hearbuddy.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.DateUtils;

import com.example.hearbuddy.R;
import com.example.hearbuddy.helper.DbHelper;
import com.example.hearbuddy.helper.listeners.OnDatabaseChangedListener;
import com.example.hearbuddy.model.AudioModel;
import com.example.hearbuddy.fragments.PlaybackFragment;
import com.example.hearbuddy.model.DisciplinaModel;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;


public class AdaptadorAudio extends RecyclerView.Adapter<AdaptadorAudio.RecordingsViewHolder>
    implements OnDatabaseChangedListener {

    private static final String LOG_TAG = "AdaptadorAudio";

    private DbHelper mDatabase;
    private DisciplinaModel disciplinaRecebida;

    AudioModel item;
    Context mContext;
    LinearLayoutManager llm;

    public AdaptadorAudio(Context context, LinearLayoutManager linearLayoutManager, DisciplinaModel disciplinaModel) {
        super();
        mContext = context;
        mDatabase = new DbHelper(mContext);
        mDatabase.setOnDatabaseChangedListener(this);
        llm = linearLayoutManager;
        disciplinaRecebida = disciplinaModel;
    }

    @Override
    public void onBindViewHolder(final RecordingsViewHolder holder, int position) {

//        Log.i("DabberAdapterAntesdoIF", disciplinaRecebida.getNomeDisciplina());
           // String selection = DbHelper.DBHelperItem.COLUMN_NAME_DISCIPLINA + " = '" + disciplinaRecebida.getId() + "'";

            item = getItem(position, null);
            long itemDuration = item.getLength();

            long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                    - TimeUnit.MINUTES.toSeconds(minutes);

            holder.vName.setText(item.getName());
            holder.vLength.setText(String.format("%02d:%02d", minutes, seconds));
            holder.vDateAdded.setText(
                    DateUtils.formatDateTime(
                            mContext,
                            item.getTime(),
                            DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR
                    )
            );



        // define an on click listener to open PlaybackFragment
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PlaybackFragment playbackFragment =
                            new PlaybackFragment().newInstance(getItem(holder.getPosition(), null));

                    FragmentTransaction transaction = ((FragmentActivity) mContext)
                            .getSupportFragmentManager()
                            .beginTransaction();

                    playbackFragment.show(transaction, "dialog_playback");

                } catch (Exception e) {
                    Log.e(LOG_TAG, "exception", e);
                }
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ArrayList<String> entrys = new ArrayList<String>();
                entrys.add(mContext.getString(R.string.dialog_file_rename));
                entrys.add(mContext.getString(R.string.dialog_file_delete));

                final CharSequence[] items = entrys.toArray(new CharSequence[entrys.size()]);


                // File delete confirm
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Opções");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            renameFileDialog(holder.getPosition());
                        } else if (item == 1) {
                            deleteFileDialog(holder.getPosition());
                        }
                    }
                });
                builder.setCancelable(true);
                builder.setNegativeButton(mContext.getString(R.string.dialog_action_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        });

    }

    @Override
    public RecordingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_view, parent, false);

        mContext = parent.getContext();

        return new RecordingsViewHolder(itemView);
    }

    public static class RecordingsViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vLength;
        protected TextView vDateAdded;
        protected View cardView;

        public RecordingsViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.file_name_text);
            vLength = (TextView) v.findViewById(R.id.file_length_text);
            vDateAdded = (TextView) v.findViewById(R.id.file_date_added_text);
            cardView = v.findViewById(R.id.card_view);
        }
    }

    @Override
    public int getItemCount() {
        return mDatabase.getCount();
    }

    public AudioModel getItem(int position, String selection) {
       return mDatabase.getItemAt(position, selection);
    }



    @Override
    public void onNewDatabaseEntryAdded() {
        //item added to top of the list
        notifyItemInserted(getItemCount() - 1);
        llm.scrollToPosition(getItemCount() - 1);
    }

    @Override
    //TODO
    public void onDatabaseEntryRenamed() {

    }

    public void remove(int position) {
        //remove item from database, recyclerview and storage

        //delete file from storage
        File file = new File(getItem(position, null).getFilePath());
        file.delete();
        mDatabase.removeItemWithId(getItem(position, null).getId());
        notifyItemRemoved(position);
    }

    //TODO
    public void removeOutOfApp(String filePath) {
        //user deletes a saved recording out of the application through another application
    }

    public void rename(int position, String name) {
        //rename a file

        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFilePath += "/SoundRecorder/" + name;
        File f = new File(mFilePath);

        if (f.exists() && !f.isDirectory()) {
            //file name is not unique, cannot rename file.
            Toast.makeText(mContext,
                    String.format(mContext.getString(R.string.toast_file_exists), name),
                    Toast.LENGTH_SHORT).show();

        } else {
            //file name is unique, rename file
            File oldFilePath = new File(getItem(position,null).getFilePath());
            oldFilePath.renameTo(f);
            mDatabase.renameItem(getItem(position,null ), name, mFilePath);
            notifyItemChanged(position);
        }
    }


    public void renameFileDialog (final int position) {
        // File rename dialog
        AlertDialog.Builder renameFileBuilder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_rename_file, null);

        final EditText input = (EditText) view.findViewById(R.id.new_name);

        renameFileBuilder.setTitle(mContext.getString(R.string.dialog_title_rename));
        renameFileBuilder.setCancelable(true);
        renameFileBuilder.setPositiveButton(mContext.getString(R.string.dialog_action_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            String value = input.getText().toString().trim() + ".mp4";
                            rename(position, value);

                        } catch (Exception e) {
                            Log.e(LOG_TAG, "exception", e);
                        }

                        dialog.cancel();
                    }
                });
        renameFileBuilder.setNegativeButton(mContext.getString(R.string.dialog_action_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        renameFileBuilder.setView(view);
        AlertDialog alert = renameFileBuilder.create();
        alert.show();
    }

    public void deleteFileDialog (final int position) {
        // File delete confirm
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(mContext);
        confirmDelete.setTitle(mContext.getString(R.string.dialog_title_delete));
        confirmDelete.setMessage(mContext.getString(R.string.dialog_text_delete));
        confirmDelete.setCancelable(true);
        confirmDelete.setPositiveButton(mContext.getString(R.string.dialog_action_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            //remove item from database, recyclerview, and storage
                            remove(position);

                        } catch (Exception e) {
                            Log.e(LOG_TAG, "exception", e);
                        }

                        dialog.cancel();
                    }
                });
        confirmDelete.setNegativeButton(mContext.getString(R.string.dialog_action_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = confirmDelete.create();
        alert.show();
    }
}
